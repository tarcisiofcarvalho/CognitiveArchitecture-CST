package codelets.behaviors;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.json.JSONException;
import org.json.JSONObject;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.List;
import memory.CreatureInnerSense;
import support.GoalAchieved;
import ws3dproxy.model.Thing;

public class GoToTarget extends Codelet {

	private MemoryObject goalAchievedMO;
	private MemoryObject selfInfoMO;
	private MemoryObject legsMO;
	private int creatureBasicSpeed;
	private double reachDistance;

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
		selfInfoMO=(MemoryObject)this.getInput("INNER");
                legsMO=(MemoryObject)this.getOutput("LEGS");
	}

	@Override
	public void proc() {
            goalAchieved = (GoalAchieved) goalAchievedMO.getI();
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
                    System.out.println("Behaviours > Stopped in front the target");
                }else{
                    message.put("ACTION", "GOTO");
                    message.put("X", targetX);
                    message.put("Y", targetY);
                    message.put("SPEED", creatureBasicSpeed);  
                    System.out.println("Behaviours > Go To the Target");
                }
                legsMO.updateI(message.toString());
            }
        }//end proc
        
        @Override
        public void calculateActivation() {
        
        }

}
