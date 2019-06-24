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
import memory.CreatureInnerSense;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import support.ActionControl;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Thing;

public class EatClosestApple extends Codelet {

	private MemoryObject closestAppleMO;
	private MemoryObject innerSenseMO;
        private MemoryObject knownMO;
        private MemoryObject actionControlMO;
	private MemoryContainer actionMO;
        
        int reachDistance;
        Thing closestApple;
        CreatureInnerSense cis;
        List<Thing> known;
        Creature c;
        
        int objectId = -1;
        ActionControl actionControl;

	public EatClosestApple(int reachDistance, Creature c) {
                setTimeStep(50);
		this.reachDistance=40;
                this.c =c;
	}

	@Override
	public void accessMemoryObjects() {
		closestAppleMO=(MemoryObject)this.getInput("CLOSEST_APPLE");
		innerSenseMO=(MemoryObject)this.getInput("INNER");
                actionControlMO=(MemoryObject)this.getInput("ACTION_CONTROL");
                actionMO=(MemoryContainer)this.getOutput("CREATURE_ACTION");
                knownMO = (MemoryObject)this.getOutput("KNOWN_APPLES");
	}

	@Override
	public void proc() {
            String appleName="";
            actionControl = (ActionControl) actionControlMO.getI();
            closestApple = (Thing) closestAppleMO.getI();
            cis = (CreatureInnerSense) innerSenseMO.getI();
            known = (List<Thing>) knownMO.getI();
            //Find distance between closest apple and self
            //If closer than reachDistance, eat the apple

            if(closestApple != null)
            {
                    double appleX=0;
                    double appleY=0;
                    try {
                            appleX=closestApple.getX1();
                            appleY=closestApple.getY1();
                            appleName = closestApple.getName();


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
                        if(distance<reachDistance){ //eat it
                                message.put("OBJECT", appleName);
                                message.put("ACTION", "EATIT");
                                System.out.println("Behaviours > Eat Apple");
                                if(objectId==-1){
                                    objectId = actionMO.setI(message.toString(),0.7); 
                                    actionControl.setEatApple(objectId);
                                }else{
                                    actionMO.setI(message.toString(),0.7,objectId);
                                }
                                DestroyClosestApple();
                        }else{
                            if(objectId==-1){
                                objectId = actionMO.setI("",0.0); 
                            }else{
                                actionMO.setI("",0.0,objectId);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }else{
                if(objectId==-1){
                    objectId = actionMO.setI("",0.0); 
                    actionControl.setEatApple(objectId);
                }else{
                    actionMO.setI("",0.0,objectId);
                }
            }	

	}
        
        @Override
        public void calculateActivation() {
        
        }
        
        public void DestroyClosestApple() {
           int r = -1;
           int i = 0;
           synchronized(known) {
             CopyOnWriteArrayList<Thing> myknown = new CopyOnWriteArrayList<>(known);  
             for (Thing t : known) {
              if (closestApple != null) 
                 if (t.getName().equals(closestApple.getName())) r = i;
              i++;
             }   
             if (r != -1) known.remove(r);
             closestApple = null;
           }
        }

}
