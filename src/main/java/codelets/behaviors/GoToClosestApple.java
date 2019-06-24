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

package codelets.behaviors;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.json.JSONException;
import org.json.JSONObject;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.Collections;
import java.util.List;
import memory.CreatureInnerSense;
import support.ActionControl;
import ws3dproxy.model.Thing;

public class GoToClosestApple extends Codelet {

	private MemoryObject lowFuelMO;
        private MemoryObject actionControlMO;
        private MemoryObject closestAppleMO;
        private MemoryObject knownApplesMO;
	private MemoryObject selfInfoMO;
        private MemoryContainer actionMO;
	private int creatureBasicSpeed;
	private double reachDistance;

        int objectId = -1;
        ActionControl actionControl;
        
	public GoToClosestApple(int creatureBasicSpeed, int reachDistance) {
		this.creatureBasicSpeed=creatureBasicSpeed;
		this.reachDistance=reachDistance;
	}

	@Override
	public void accessMemoryObjects() {
		closestAppleMO=(MemoryObject)this.getInput("CLOSEST_APPLE");
                actionControlMO=(MemoryObject)this.getInput("ACTION_CONTROL");
		selfInfoMO=(MemoryObject)this.getInput("INNER");
                lowFuelMO=(MemoryObject)this.getInput("LOW_FUEL"); // Low Fuel Object Memory
                this.knownApplesMO=(MemoryObject)this.getInput("KNOWN_APPLES");
                actionMO=(MemoryContainer)this.getOutput("CREATURE_ACTION");
	}

	@Override
	public void proc() {
            // Find distance between creature and closest apple
            //If far, go towards it
            //If close, stops
            // --- If low fuel go to the Closest Apple --- //
            CreatureInnerSense cis = (CreatureInnerSense) selfInfoMO.getI();
            List<Thing> known = Collections.synchronizedList((List<Thing>) knownApplesMO.getI());
            Thing closestApple = null;
            double maxDistance = 999;
            if(known!=null){
                if(known.size()>0){
                    for (Thing t : known) {
                        double distance = calculateDistance(t.getX1(), t.getY1(), cis.position.getX(), cis.position.getY());
                        if(distance<maxDistance){
                            closestApple = t;
                            maxDistance = distance;
                        }
                    }
                }
            }
            Boolean lowFuel = (Boolean) lowFuelMO.getI();
            if(lowFuel){
                //Thing closestApple = (Thing) closestAppleMO.getI();
                actionControl = (ActionControl) actionControlMO.getI();
                
                if(closestApple != null && lowFuel)
                {
                    double appleX=0;
                    double appleY=0;
                    try {
                        appleX = closestApple.getX1();
                        appleY = closestApple.getY1();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    double selfX=cis.position.getX();
                    double selfY=cis.position.getY();

                    Point2D pApple = new Point();
                    pApple.setLocation(appleX, appleY);

                    Point2D pSelf = new Point();
                    pSelf.setLocation(selfX, selfY);

                    double distance = pSelf.distance(pApple);
                    JSONObject message=new JSONObject();
                    try {
                        if(distance>reachDistance){ //Go to it
                            message.put("ACTION", "GOTO");
                            message.put("X", (int)appleX);
                            message.put("Y", (int)appleY);
                            message.put("SPEED", creatureBasicSpeed);
                            message.put("TYPE", "GO_APPLE");
                            System.out.println("Behaviours > Go to Apple");

                        }else{//Stop
                            message.put("ACTION", "GOTO");
                            message.put("X", (int)appleX);
                            message.put("Y", (int)appleY);
                            message.put("SPEED", 0.0);
                            message.put("TYPE", "GO_APPLE");
                            System.out.println("Behaviours > Stop in front of Apple");
                        }
                        if(objectId==-1){
                            objectId = actionMO.setI(message.toString(),0.49);
                            actionControl.setGotToApple(objectId);
                        }else{
                            actionMO.setI(message.toString(),0.49, objectId);
                        }

                    } catch (JSONException e) {
                            e.printStackTrace();
                    }	
                }
            }
	}//end proc
        
        @Override
        public void calculateActivation() {
        
        }
        private double calculateDistance(double x1, double y1, double x2, double y2) {
            return(Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)));
        }

}
