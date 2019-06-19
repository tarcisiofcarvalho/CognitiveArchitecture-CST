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

import ws3dproxy.CommandExecException;
import ws3dproxy.CommandUtility;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.Creature;
import ws3dproxy.model.World;

/**
 *
 * @author rgudwin
 */
public class Environment {
    
    public String host="localhost";
    public int port = 4011;
    public String robotID="r0";
    public Creature c = null;
    
    public Environment(){
          WS3DProxy proxy = new WS3DProxy();
          try {   
            
            // --- Word and creature setup --- //
            World w = World.getInstance();
            w.reset();
            c = proxy.createCreature(100,450,0);
            World.grow();
            // -- Loop of 20 random Jewels //
            for(int x=0;x<=2;x++){     
               for(int i=0;i<=5;i++){
                   World.createJewel(i, Math.ceil(Math.random()*600.0), Math.ceil(Math.random()*600.0));
               }                 
            }
            
//            // --- Loop to create 10 foods --- //
//            for(int i=1;i<=5;i++){
//                World.createFood(0, Math.ceil(Math.random()*600.0), Math.ceil(Math.random()*600.0));
//            }                 
            
            // -- Jewel for testing -- //
            //World.createJewel(0, 350.0, 250.0);
            //World.createJewel(0, 450.0, 500.0);
            //World.createJewel(1, 400.0, 500.0);
            //World.createFood(0, 100.0, 100.0);
            
            // --- Creating a new way point as target symbol --- //
            CommandUtility.sendNewWaypoint(700.0, 500.0);
            
            // --- Staring the creature --- //
            c.start();
             
            // --- Updating the state to reflect the creature leaflets --- //
            c.updateState();
             
             
          } catch (CommandExecException e) {
              e.printStackTrace();
          }
          
          System.out.println("Robot "+c.getName()+" is ready to go.");
		


	}
}
