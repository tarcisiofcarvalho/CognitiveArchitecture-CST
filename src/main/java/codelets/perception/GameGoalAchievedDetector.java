package codelets.perception;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import memory.CreatureInnerSense;
import support.GoalAchieved;
import support.JewelControl;

/**
 * Detect Low Fuel in the Inner Sensory.
 * 	This class detects if the creature fuel level is under than 400.
 * @author Tarcisio
 *
 */
public class GameGoalAchievedDetector extends Codelet {

        private MemoryObject innerMO;
        private MemoryObject goalAchievedlMO;
        private MemoryObject goalAchievedlMOOut;
        int reachDistance;
        GoalAchieved goalAchieved;
        
	public GameGoalAchievedDetector(int reachDistance){
            this.reachDistance = reachDistance;
	}

	@Override
	public void accessMemoryObjects() {
            synchronized(this) {
                this.innerMO=(MemoryObject)this.getInput("INNER");
                this.goalAchievedlMO=(MemoryObject)this.getInput("GOAL_ACHIEVED");
            }
            this.goalAchievedlMOOut=(MemoryObject)this.getOutput("GOAL_ACHIEVED");
	}

	@Override
	public void proc() {
            
            // --- Memory Objects --- //
            goalAchieved = (GoalAchieved) this.goalAchievedlMO.getI();
            CreatureInnerSense cis = (CreatureInnerSense) innerMO.getI();

            if(cis.position!=null){
                // --- Calculate the distance to the target --- //
                double distance = calculateDistance(700.0, 500.0, cis.position.getX(), cis.position.getY());
                
//                System.out.println("distance to the way: " + distance);
//                System.out.println("JewelGoalStatus: " + goalAchieved.getJewelGoalStatus());
//                System.out.println("GameGoalStatus: " + goalAchieved.getGameGoalStatus());
                // --- Enablement condition --- //
                if(goalAchieved.getJewelGoalStatus() 
                        && !goalAchieved.getGameGoalStatus()
                            &&(distance<70.0)){
                    synchronized(goalAchieved){
                        System.out.println("Perception > Game Goal Achieved");
                        goalAchieved.setGameGoalStatus(true);

                        // --- Updating the Memory Object --- //
                        this.goalAchievedlMOOut.setI(goalAchieved);
                    }
                }
            }
            
	}// end proc
        
        @Override
        public void calculateActivation() {
        
        }
        
        private double calculateDistance(double x1, double y1, double x2, double y2) {
            return(Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)));
        }

}//end class


