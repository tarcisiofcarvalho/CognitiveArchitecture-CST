package codelets.behaviors;

import org.json.JSONObject;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import memory.CreatureInnerSense;
import support.GoalAchieved;
import support.JewelControl;
import ws3dproxy.CommandExecException;
import ws3dproxy.model.Creature;

public class ExchangeJewels extends Codelet {

	private MemoryObject goalAchievedMO;
        private MemoryObject jewelControlMO;
        private MemoryObject innerMO;
        private MemoryContainer actionMO;

        GoalAchieved goalAchieved;
        JewelControl jewelControl;
        Creature c;
        
        int objectId =-1;
        
	public ExchangeJewels(Creature c) {
            this.c = c;
	}

	@Override
	public void accessMemoryObjects() {
            innerMO=(MemoryObject)this.getInput("INNER");
            goalAchievedMO=(MemoryObject)this.getInput("GOAL_ACHIEVED");
            jewelControlMO=(MemoryObject)this.getInput("JEWEL_CONTROL");
            actionMO=(MemoryContainer)this.getOutput("CREATURE_ACTION");
	}

	@Override
	public void proc() {
            
            // --- Memory Objects Load --- //
            goalAchieved = (GoalAchieved) goalAchievedMO.getI();
            CreatureInnerSense cis = (CreatureInnerSense) innerMO.getI();
            jewelControl = (JewelControl) jewelControlMO.getI();
            
            if(goalAchieved.getGameGoalStatus() && !goalAchieved.getFinishStatus()){
                synchronized(goalAchieved){
                    // --- Stop the creature --- //
                    JSONObject message=new JSONObject();
                    message.put("ACTION", "STOP");
                    message.put("X", cis.position.getX());
                    message.put("Y", cis.position.getY());
                    message.put("SPEED", 0.0);  
                    if(objectId==-1){
                        objectId = actionMO.setI(message.toString(),0.9);
                    }else{
                        actionMO.setI(message.toString(),0.9,objectId);
                    }

                    try {
                        System.out.println("Leaflets delivering...");
                        c.deliverLeaflet(""+c.getLeaflets().get(0).getID());
                        c.deliverLeaflet(""+c.getLeaflets().get(1).getID());
                        c.deliverLeaflet(""+c.getLeaflets().get(2).getID());
                        System.out.println("Leaflets delivered");
  
                    } catch (CommandExecException ex) {
                        ex.printStackTrace();
                    }

                    System.out.println("Game completed");
                    try 
	                {
	                     Thread.sleep(1000);
	                } catch (Exception e){
	                     e.printStackTrace();
	                }
                    System.exit(0);
                    this.goalAchieved.setFinishedStatus(true);
                    // --- Updating Memory Object --- //
                    this.goalAchievedMO.setI(goalAchieved);
                    // --- Update Memory Object --- //

            }
            }

        }//end proc
        
        @Override
        public void calculateActivation() {
        
        }

}
