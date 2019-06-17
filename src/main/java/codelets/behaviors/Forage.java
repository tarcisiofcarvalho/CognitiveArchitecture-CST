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

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import support.GoalAchieved;
import ws3dproxy.model.Thing;

/** 
 * 
 * @author klaus
 * 
 * 
 */

public class Forage extends Codelet {
    
        //private MemoryObject knownMO;
        private MemoryObject goalAchievedMO;
        private MemoryObject desiredJewelsMO;
        private List<Thing> known;
        private MemoryObject legsMO;

        List<Thing> desiredJewel;
        GoalAchieved goalAchieved;
        
	/**
	 * Default constructor
	 */
	public Forage(){       
	}

	@Override
	public void proc() {
            desiredJewel = (List<Thing>) desiredJewelsMO.getI();
            goalAchieved = (GoalAchieved) goalAchievedMO.getI();
            System.out.println("goalAchieved.getJewelGoalStatus() >>> "+ goalAchieved.getJewelGoalStatus());
            if (desiredJewel.size() == 0 && !goalAchieved.getJewelGoalStatus()) {
		JSONObject message=new JSONObject();
                try {
                        message.put("ACTION", "FORAGE");
                        legsMO.updateI(message.toString());
                        System.out.println("FORAGE");

                } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
            }
//            else{
//                System.out.println("FORAGE NONO ");
//                legsMO.updateI("");
//            }            
		
	}

	@Override
	public void accessMemoryObjects() {
            //knownMO = (MemoryObject)this.getInput("KNOWN_APPLES");
            desiredJewelsMO = (MemoryObject) this.getInput("DESIRED_JEWELS");
            goalAchievedMO = (MemoryObject) this.getInput("GOAL_ACHIEVED");
            legsMO=(MemoryObject)this.getOutput("LEGS");	
	}
        
        @Override
        public void calculateActivation() {
            
        }


}
