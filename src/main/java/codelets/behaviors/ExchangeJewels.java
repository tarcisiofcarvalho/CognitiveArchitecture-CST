package codelets.behaviors;

import org.json.JSONObject;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import memory.CreatureInnerSense;
import support.GoalAchieved;
import support.JewelControl;
import ws3dproxy.CommandExecException;
import ws3dproxy.CommandUtility;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Leaflet;
import ws3dproxy.model.World;

public class ExchangeJewels extends Codelet {

	private MemoryObject goalAchievedMO;
        private MemoryObject jewelControlMO;
        private MemoryObject innerMO;
	private MemoryObject legsMO;
        private MemoryObject handsMO;

        GoalAchieved goalAchieved;
        JewelControl jewelControl;
        Creature c;
        
	public ExchangeJewels(Creature c) {
            this.c = c;
	}

	@Override
	public void accessMemoryObjects() {
            innerMO=(MemoryObject)this.getInput("INNER");
            goalAchievedMO=(MemoryObject)this.getInput("GOAL_ACHIEVED");
            jewelControlMO=(MemoryObject)this.getInput("JEWEL_CONTROL");
            legsMO=(MemoryObject)this.getOutput("LEGS");
            handsMO=(MemoryObject)this.getOutput("HANDS");
	}

	@Override
	public void proc() {
            
            // --- Memory Objects Load --- //
            goalAchieved = (GoalAchieved) goalAchievedMO.getI();
            CreatureInnerSense cis = (CreatureInnerSense) innerMO.getI();
            jewelControl = (JewelControl) jewelControlMO.getI();
            
            // --- Check the condition to stop the creature and exchange the leaflets --- //
//            System.out.println("Final: GameGoalStatus: " + goalAchieved.getGameGoalStatus());
//            System.out.println("Final: FinishStatus: " + goalAchieved.getFinishStatus());
            
            if(goalAchieved.getGameGoalStatus() && !goalAchieved.getFinishStatus()){
                synchronized(goalAchieved){
                    // --- Stop the creature --- //
                    JSONObject message=new JSONObject();
                    message.put("ACTION", "STOP");
                    message.put("X", cis.position.getX());
                    message.put("Y", cis.position.getY());
                    message.put("SPEED", 0.0);                
                    legsMO.updateI(message.toString());

                    //JSONObject msg=new JSONObject();
                    //msg.put("ACTION", "EXCHANGE"); 
                    //msg.put("OBJECT", "JEWELS");
                    //handsMO.updateI(msg.toString()); 

                    try {
                        System.out.println("Leaflets delivering...");
                        c.deliverLeaflet(""+c.getLeaflets().get(0).getID());
                        c.deliverLeaflet(""+c.getLeaflets().get(1).getID());
                        c.deliverLeaflet(""+c.getLeaflets().get(2).getID());
                        System.out.println("Leaflets delivered");
  
                    } catch (CommandExecException ex) {
                        ex.printStackTrace();;
                        //Logger.getLogger(ExchangeJewels.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // --- Exchange Jewels --- //
                    //WS3DProxy proxy = new WS3DProxy();
                    //World w = proxy.getWorld();
//                    ?Creature c = this.jewelControl.getCreature();
//                    try {
//                        //c = proxy.getCreature("0");
//                        List<Leaflet> leafletList = c.getLeaflets();
//                        System.out.println("Final exchange jewell > size: " + this.jewelControl.getLeafletsIds().size());
//                        c.deliverLeaflet(""+this.jewelControl.getLeafletsIds().get(0));
//                        c.deliverLeaflet(""+this.jewelControl.getLeafletsIds().get(1));
//                        c.deliverLeaflet(""+this.jewelControl.getLeafletsIds().get(2));
////                        for(Long l : this.jewelControl.getLeafletsIds()){
////                            c.deliverLeaflet(""+l);
////                            System.out.println("Delivery of leaflet: " + l);
////                        }
//                    } catch (CommandExecException ex) {
//                        ex.getStackTrace();
//                        Logger.getLogger(ExchangeJewels.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    
                    System.out.println("Game completed");
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
