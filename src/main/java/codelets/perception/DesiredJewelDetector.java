package codelets.perception;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import support.GoalAchieved;
import support.JewelControl;
import ws3dproxy.model.Thing;
import ws3dproxy.util.Constants;

/**
 * Detect Low Fuel in the Inner Sensory.
 * 	This class detects if the creature fuel level is under than 400.
 * @author Tarcisio
 *
 */
public class DesiredJewelDetector extends Codelet {

        private MemoryObject visionMO;
        private MemoryObject jewelControlMO;
        private MemoryObject desiredJewelslMO;
        private MemoryObject goalAchievedMO;

	public DesiredJewelDetector(){
		
	}

	@Override
	public void accessMemoryObjects() {
                synchronized(this) {
		    this.visionMO=(MemoryObject)this.getInput("VISION");
                    this.jewelControlMO=(MemoryObject)this.getInput("JEWEL_CONTROL");
                    this.goalAchievedMO=(MemoryObject)this.getInput("GOAL_ACHIEVED");
                }
		this.desiredJewelslMO=(MemoryObject)this.getOutput("DESIRED_JEWELS");
	}

	@Override
	public void proc() {
            CopyOnWriteArrayList<Thing> vision;
            List<Thing> desiredJewels;
            JewelControl jewelControl = (JewelControl) jewelControlMO.getI();
            // --- Temp action, remove it later --- //
            //System.out.println("Leaflets:" + jewelControl.getCreature().getLeaflets().size() );
            
            
            GoalAchieved goalAchieved = (GoalAchieved) this.goalAchievedMO.getI();
            boolean check;
            if(!goalAchieved.getJewelGoalStatus()){
                synchronized (visionMO) {
                   vision = new CopyOnWriteArrayList((List<Thing>) visionMO.getI());    
                   //desiredJewels = Collections.synchronizedList((List<Thing>) desiredJewelslMO.getI());
                   ///if(desiredJewels==null){
                       desiredJewels = Collections.synchronizedList((new ArrayList<Thing>()));
                   //}
    //               desiredJewels = Collections.synchronizedList((new ArrayList<Thing>()));
                   
          
                   
                   synchronized(vision) {
                        for (Thing t : vision) {
                           check = true;
                           // --- Select just the Jewels from Things List --- //
                           if(t.getAttributes().getCategory()==Constants.categoryJEWEL){                      
                               synchronized(jewelControl){
                                   // --- Select just the desired jewels --- //
                                   if(jewelControl.isDesiredJewel(t.getAttributes().getColor())){
                                       synchronized(desiredJewels){
                                           // --- Check if the desired jewel is in the list already --- //
                                           for(Thing d : desiredJewels){
                                               if(d.getName().equals(t.getName())){
                                                   check = false;
                                                   System.out.println("check false");
                                               }
                                           }
                                           // --- If not in the list, add it on desired jewel list --- //
                                           if(check){
                                                desiredJewels.add(t);
                                                System.out.println("Perception > Desired Jewel Detector");
                                                //System.out.println("Desired jewel "+t.getAttributes().getColor()+" in vision");
                                           }
                                       }
                                   }
                               }
                           }
                        }
                    }
                   desiredJewelslMO.setI(desiredJewels);
                }
//               System.out.println("Desired jewel in vision size: " + desiredJewels.size());
//               System.out.println("Remaining Jewels: " + jewelControl.getJewelRemainingTotal());
            }else{
                desiredJewels = Collections.synchronizedList((new ArrayList<Thing>()));
                desiredJewelslMO.setI(desiredJewels);
            }
	}// end proc
        
        @Override
        public void calculateActivation() {
        
        }


}//end class


