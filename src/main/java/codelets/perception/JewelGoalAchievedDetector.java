package codelets.perception;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import support.GoalAchieved;
import support.JewelControl;

/**
 * Detect Low Fuel in the Inner Sensory.
 * 	This class detects if the creature fuel level is under than 400.
 * @author Tarcisio
 *
 */
public class JewelGoalAchievedDetector extends Codelet {

        private MemoryObject jewelControlMO;
        private MemoryObject goalAchievedlMO;
        private MemoryObject handsMO;

        GoalAchieved goalAchieved;
        
	public JewelGoalAchievedDetector(){
		
	}

	@Override
	public void accessMemoryObjects() {
                synchronized(this) {
                    this.jewelControlMO=(MemoryObject)this.getInput("JEWEL_CONTROL");
                    this.handsMO=(MemoryObject)this.getInput("HANDS");
                }
		this.goalAchievedlMO=(MemoryObject)this.getOutput("GOAL_ACHIEVED");
	}

	@Override
	public void proc() {
            JewelControl jewelControl = (JewelControl) jewelControlMO.getI();
            goalAchieved = (GoalAchieved) this.goalAchievedlMO.getI();
            
                String commHands = (String) handsMO.getI();
                
                if (commHands == null) commHands = "";
                
                if(commHands.equals("")){
                    if(jewelControl.getJewelRemainingTotal()==0 && !goalAchieved.getJewelGoalStatus()){
                        goalAchieved.setJewelGoalStatus(true);
                        System.out.println("Perception > Jewel Goal Detector");
                        this.goalAchievedlMO.setI(goalAchieved);
                    }
                }

            
	}// end proc
        
        @Override
        public void calculateActivation() {
        
        }


}//end class


