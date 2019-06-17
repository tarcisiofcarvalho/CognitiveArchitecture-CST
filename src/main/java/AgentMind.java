/*****************************************************************************
 * Copyright 2007-2015 DCA-FEEC-UNICAMP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *    Klaus Raizer, Andre Paraense, Ricardo Ribeiro Gudwin
 *****************************************************************************/

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.entities.Mind;
import codelets.behaviors.EatClosestApple;
import codelets.behaviors.Forage;
import codelets.behaviors.GetDesiredJewel;
import codelets.behaviors.GoToClosestApple;
import codelets.behaviors.GoToDesiredJewel;
import codelets.behaviors.GoToTarget;
import codelets.behaviors.HideUndesiredJewel;
import codelets.motor.HandsActionCodelet;
import codelets.motor.LegsActionCodelet;
import codelets.perception.AppleDetector;
import codelets.perception.ClosestAppleDetector;
import codelets.perception.ClosestDesiredJewelDetector;
import codelets.perception.ClosestUndesiredJewelDetector;
import codelets.perception.DesiredJewelDetector;
import codelets.perception.JewelGoalAchievedDetector;
import codelets.perception.LowFuelDetector;
import codelets.sensors.InnerSense;
import codelets.sensors.Vision;
import support.JewelControl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import memory.CreatureInnerSense;
import support.GoalAchieved;
import support.MindView;
import ws3dproxy.model.Thing;

/**
 *
 * @author rgudwin
 */
public class AgentMind extends Mind {
    
    private static int creatureBasicSpeed=3;
    private static int reachDistance=70;
    
    public AgentMind(Environment env) {
                super();
                
                // Declare Memory Objects
	        MemoryObject legsMO;
	        MemoryObject handsMO;
                MemoryObject visionMO;
                MemoryObject innerSenseMO;
                MemoryObject closestAppleMO;
                MemoryObject knownApplesMO;
                MemoryObject lowFuelMO;
                MemoryObject desiredJewelMO;
                MemoryObject jewelControlMO;
                MemoryObject closestDesiredJewelMO;
                MemoryObject closestUndesiredJewelMO;
                MemoryObject goalAchievedMO;
                
                //Initialize Memory Objects
                GoalAchieved goalAchieved = new GoalAchieved();
                goalAchievedMO=createMemoryObject("GOAL_ACHIEVED",goalAchieved);
                
                JewelControl jewelControl = new JewelControl();
                jewelControlMO=createMemoryObject("JEWEL_CONTROL", jewelControl);
                
                List<Thing> desired_jewels_list = Collections.synchronizedList(new ArrayList<Thing>());
                desiredJewelMO=createMemoryObject("DESIRED_JEWELS", desired_jewels_list);

                List<Thing> closest_desired_jewels_list = Collections.synchronizedList(new ArrayList<Thing>());
                closestDesiredJewelMO=createMemoryObject("CLOSEST_DESIRED_JEWELS", closest_desired_jewels_list);
                
                List<Thing> closest_undesired_jewels_list = Collections.synchronizedList(new ArrayList<Thing>());
                closestUndesiredJewelMO=createMemoryObject("CLOSEST_UNDESIRED_JEWELS", closest_undesired_jewels_list);

                Boolean lowFuel = false;
                lowFuelMO=createMemoryObject("LOW_FUEL", lowFuel); // Low Fuel Memory Object
                
                legsMO=createMemoryObject("LEGS", "");
		handsMO=createMemoryObject("HANDS", "");
                List<Thing> vision_list = Collections.synchronizedList(new ArrayList<Thing>());
		visionMO=createMemoryObject("VISION",vision_list);
                CreatureInnerSense cis = new CreatureInnerSense();
		innerSenseMO=createMemoryObject("INNER", cis);
                Thing closestApple = null;
                closestAppleMO=createMemoryObject("CLOSEST_APPLE", closestApple);
                List<Thing> knownApples = Collections.synchronizedList(new ArrayList<Thing>());
                knownApplesMO=createMemoryObject("KNOWN_APPLES", knownApples);
                
                // Create and Populate MindViewer
                MindView mv = new MindView("MindView");
                mv.addMO(knownApplesMO);
                mv.addMO(visionMO);
                mv.addMO(closestAppleMO);
                mv.addMO(innerSenseMO);
                mv.addMO(handsMO);
                mv.addMO(legsMO);
                mv.addMO(lowFuelMO);
                mv.addMO(desiredJewelMO);
                mv.addMO(jewelControlMO);
                mv.addMO(closestDesiredJewelMO);
                mv.addMO(goalAchievedMO);
                mv.StartTimer();
                mv.setVisible(true);
		
		/******************************************/
                /********* Create Sensor Codelets *********/
                /******************************************/	
		Codelet vision=new Vision(env.c);
		vision.addOutput(visionMO);
                insertCodelet(vision); //Creates a vision sensor
		
		Codelet innerSense=new InnerSense(env.c);
		innerSense.addOutput(innerSenseMO);
                insertCodelet(innerSense); //A sensor for the inner state of the creature
		
		/******************************************/
                /******** Create Actuator Codelets ********/
                /******************************************/
		Codelet legs=new LegsActionCodelet(env.c);
		legs.addInput(legsMO);
                legs.addInput(handsMO);
                insertCodelet(legs);

		Codelet hands=new HandsActionCodelet(env.c);
		hands.addInput(handsMO);
                insertCodelet(hands);
		
		/******************************************/
                /******* Create Perception Codelets *******/
                /******************************************/
                // --- Jewel Goal Achieved --- //
                Codelet jewelGoalAchievedDetector = new JewelGoalAchievedDetector();
                jewelGoalAchievedDetector.addInput(jewelControlMO);
                jewelGoalAchievedDetector.addInput(handsMO);
                jewelGoalAchievedDetector.addOutput(goalAchievedMO);
                insertCodelet(jewelGoalAchievedDetector);
                
                // --- Desired Jewel Detector --- //
                Codelet desiredJewelDetector = new DesiredJewelDetector();
                desiredJewelDetector.addInput(visionMO);
                desiredJewelDetector.addInput(jewelControlMO);
                desiredJewelDetector.addOutput(desiredJewelMO);
                insertCodelet(desiredJewelDetector);

                // --- Closest Desired Jewel Detector --- //
                Codelet closestDesiredJewelDetector = new ClosestDesiredJewelDetector(reachDistance);
                closestDesiredJewelDetector.addInput(innerSenseMO);
                closestDesiredJewelDetector.addInput(desiredJewelMO);
                closestDesiredJewelDetector.addOutput(closestDesiredJewelMO);
                insertCodelet(closestDesiredJewelDetector);

                // --- Closest Undesired Jewel Detector --- //
                Codelet closestUndesiredJewelDetector = new ClosestUndesiredJewelDetector(reachDistance);
                closestUndesiredJewelDetector.addInput(innerSenseMO);
                closestUndesiredJewelDetector.addInput(visionMO);
                closestUndesiredJewelDetector.addInput(jewelControlMO);
                closestUndesiredJewelDetector.addOutput(closestUndesiredJewelMO);
                insertCodelet(closestUndesiredJewelDetector);
                
                // --- Low Fuel Detector --- //
                Codelet lf = new LowFuelDetector();
                lf.addInput(innerSenseMO);
                lf.addOutput(lowFuelMO);
                insertCodelet(lf);
                
                // --- Apple Detector --- //
                Codelet ad = new AppleDetector();
                ad.addInput(visionMO);
                ad.addOutput(knownApplesMO);
                insertCodelet(ad);
                
                // --- Closest Apple Detector --- //
		Codelet closestAppleDetector = new ClosestAppleDetector();
		closestAppleDetector.addInput(knownApplesMO);
		closestAppleDetector.addInput(innerSenseMO);
		closestAppleDetector.addOutput(closestAppleMO);
                insertCodelet(closestAppleDetector);
		
		/******************************************/
                /******* Create Behavior Codelets *********/
                /******************************************/
		// --- Go to Target Codelet --- //
                Codelet goToTarget = new GoToTarget(creatureBasicSpeed, reachDistance);
		goToTarget.addInput(goalAchievedMO);
                goToTarget.addInput(innerSenseMO);
		goToTarget.addOutput(legsMO);
                insertCodelet(goToTarget);
                
		// --- Hide Undesired Jewel Codelet --- //
                Codelet hideUndesiredJewel = new HideUndesiredJewel();
		hideUndesiredJewel.addInput(closestUndesiredJewelMO);
		hideUndesiredJewel.addOutput(handsMO);
                insertCodelet(hideUndesiredJewel);
                
                // --- Get Desired Jewel Codelet --- //
                Codelet getDesiredJewel = new GetDesiredJewel();
		getDesiredJewel.addInput(closestDesiredJewelMO);
		getDesiredJewel.addInput(desiredJewelMO);
                getDesiredJewel.addInput(jewelControlMO);
		getDesiredJewel.addOutput(handsMO);
                insertCodelet(getDesiredJewel);
                
		// --- Go to Desired Jewel Codelet --- //
                Codelet goToDesiredJewel = new GoToDesiredJewel(creatureBasicSpeed,reachDistance);
		goToDesiredJewel.addInput(desiredJewelMO);
		goToDesiredJewel.addInput(innerSenseMO);
		goToDesiredJewel.addOutput(legsMO);
                insertCodelet(goToDesiredJewel);
                
                
                Codelet goToClosestApple = new GoToClosestApple(creatureBasicSpeed,reachDistance);
		goToClosestApple.addInput(closestAppleMO);
		goToClosestApple.addInput(innerSenseMO);
                goToClosestApple.addInput(lowFuelMO);
		goToClosestApple.addOutput(legsMO);
                insertCodelet(goToClosestApple);
		
		Codelet eatApple=new EatClosestApple(reachDistance);
		eatApple.addInput(closestAppleMO);
		eatApple.addInput(innerSenseMO);
		eatApple.addOutput(handsMO);
                eatApple.addOutput(knownApplesMO);
                insertCodelet(eatApple);
                
                Codelet forage=new Forage();
		forage.addInput(desiredJewelMO);
                forage.addInput(goalAchievedMO);
                forage.addOutput(legsMO);
                insertCodelet(forage);
                
                // sets a time step for running the codelets to avoid heating too much your machine
                for (Codelet c : this.getCodeRack().getAllCodelets())
                    c.setTimeStep(200);
		
		// Start Cognitive Cycle
		start(); 
    }             
    
}
