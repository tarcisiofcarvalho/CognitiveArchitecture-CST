package codelets.behaviors;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.json.JSONObject;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import memory.CreatureInnerSense;
import support.ActionControl;
import support.GoalAchieved;

public class GoToTarget extends Codelet {

	private MemoryObject goalAchievedMO;
	private MemoryObject selfInfoMO;
        private MemoryObject actionControlMO;
        private MemoryContainer actionMO;
	private int creatureBasicSpeed;
	private double reachDistance;

        int objectId = -1;
        ActionControl actionControl;
        
        GoalAchieved goalAchieved;
        double targetX = 700.0;
        double targetY = 500.0;
        
	public GoToTarget(int creatureBasicSpeed, int reachDistance) {
		this.creatureBasicSpeed=creatureBasicSpeed;
		this.reachDistance=reachDistance;
	}

	@Override
	public void accessMemoryObjects() {
		goalAchievedMO=(MemoryObject)this.getInput("GOAL_ACHIEVED");
                actionControlMO=(MemoryObject)this.getInput("ACTION_CONTROL");
		selfInfoMO=(MemoryObject)this.getInput("INNER");
                actionMO=(MemoryContainer)this.getOutput("CREATURE_ACTION");
	}

	@Override
	public void proc() {
            goalAchieved = (GoalAchieved) goalAchievedMO.getI();
            actionControl = (ActionControl) actionControlMO.getI();
            CreatureInnerSense cis = (CreatureInnerSense) selfInfoMO.getI();
            if(goalAchieved.getJewelGoalStatus() && !goalAchieved.getGameGoalStatus()){
                // --- Checking the distance of the creature to the target spot --- //
                double selfX=cis.position.getX();
                double selfY=cis.position.getY();

                Point2D target = new Point();
                target.setLocation(this.targetX, this.targetY);

                Point2D pSelf = new Point();
                pSelf.setLocation(selfX, selfY);

                double dist = pSelf.distance(target);
                JSONObject message=new JSONObject();
                if(dist<reachDistance){
                    // --- Stop the creature in case reached the target --- //
                    message.put("ACTION", "GOTO");
                    message.put("X", selfX);
                    message.put("Y", selfY);
                    message.put("SPEED", 0.0);
                    message.put("TYPE", "GO_TARGET");
                    System.out.println("Behaviours > Stopped in front the target");
                }else{
                    message.put("ACTION", "GOTO");
                    message.put("X", targetX);
                    message.put("Y", targetY);
                    message.put("SPEED", creatureBasicSpeed);
                    message.put("TYPE", "GO_TARGET");
                    System.out.println("Behaviours > Go To the Target");
                }
                if(objectId==-1){
                    objectId = actionMO.setI(message.toString(),0.1);
                    actionControl.setGoToTarget(objectId);
                }else{
                    actionMO.setI(message.toString(),0.1,objectId);
                }
                
            }
        }//end proc
        
        @Override
        public void calculateActivation() {
        
        }

}
