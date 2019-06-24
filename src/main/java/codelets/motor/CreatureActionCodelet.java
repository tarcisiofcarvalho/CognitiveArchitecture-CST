package codelets.motor;


import org.json.JSONObject;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.Random;
import java.util.logging.Logger;
import support.ActionControl;
import ws3dproxy.model.Creature;

public class CreatureActionCodelet extends Codelet{
    
        private MemoryContainer actionMO;
        private MemoryObject actionControlMO;
	private double previousTargetx=0;
	private double previousTargety=0;
	private String previousAction="";
        
        ActionControl actionControl;
        
        private Creature c;
        double old_angle = 0;
        
        int k=0;
        static Logger log = Logger.getLogger(CreatureActionCodelet.class.getCanonicalName());

	public CreatureActionCodelet(Creature nc) {
		c = nc;
                System.out.println("In constructor:" + c.getLeaflets().size());
	}
	
	@Override
	public void accessMemoryObjects() {
            actionMO=(MemoryContainer)this.getInput("CREATURE_ACTION");
            actionControlMO=(MemoryObject)this.getInput("ACTION_CONTROL");
	}
	
	@Override
	public void proc() {
                
            actionControl = (ActionControl) actionControlMO.getI();
            String comm = (String) actionMO.getI();

            if (comm == null) comm = "";
            Random r = new Random();
            
            if(!comm.equals("") ){
                //System.out.println("Cmd: " + comm);
                if (!comm.equals(previousAction)){
                    //System.out.println("Different from previous action");
                    try {
                        JSONObject command=new JSONObject(comm);
                        if(command.has("ACTION") && command.has("OBJECT")){
                                String action=command.getString("ACTION");
                                String objectName=command.getString("OBJECT");
                                if(action.equals("PICKUP")){
                                    //System.out.println("Action Index: " + actionControl.getGetJewel());
                                    c.putInSack(objectName);
                                    System.out.println("Motor > Get Jewel: " + objectName);
                                    log.info("Sending Put In Sack command to agent:****** "+objectName+"**********");							
                                    actionMO.setI("", 0.0, actionControl.getGetJewel());
                                    comm = "";
                                }else if(action.equals("EATIT")){
                                    c.eatIt(objectName);
                                    System.out.println("Motor > Eat Apple: " + objectName);
                                    log.info("Sending Eat command to agent:****** "+objectName+"**********");
                                    actionMO.setI("",0.0,actionControl.getEatApple());
                                    comm = "";
                                }else if(action.equals("BURY")){
                                    c.hideIt(objectName);
                                    System.out.println("Motor > Hide Jewel: " + objectName);
                                   log.info("Sending Bury command to agent:****** "+objectName+"**********");
                                   actionMO.setI("",0.0,actionControl.getHideJewel());
                                   comm = "";
                                }else if(action.equals("EXCHANGE")){
                                    System.out.println("Motor > Exchange Leaflet");
                                    c.deliverLeaflet(""+c.getLeaflets().get(0).getID());
                                    c.deliverLeaflet(""+c.getLeaflets().get(1).getID());
                                    c.deliverLeaflet(""+c.getLeaflets().get(2).getID());
                                    comm = "";
                                }
                            }else{
                                if (command.has("ACTION")) {
                                    int x=0,y=0;
                                    String action=command.getString("ACTION");
                                    if(action.equals("FORAGE")){
                                        if (!comm.equals(previousAction)){
                                            log.info("Sending Forage command to agent");
                                            c.rotate(2);  
                                            System.out.println("Motor > FORAGE");
                                            actionMO.setI("",0.0,actionControl.getForage());
                                        }
                                        comm = "";
                                    }else if(action.equals("GOTO")){
                                        if (!comm.equals(previousAction)) {
                                            double speed=command.getDouble("SPEED");
                                            double targetx=command.getDouble("X");
                                            double targety=command.getDouble("Y");
                                            if (!comm.equals(previousAction)){
                                                log.info("Sending move command to agent: ["+targetx+","+targety+"]");
                                                c.moveto(speed, targetx, targety);
                                                System.out.println("Motor > GOTO ");
                                                previousTargetx=targetx;
                                                previousTargety=targety;
                                                if(command.getString("TYPE").equals("GO_APPLE")){
                                                    actionMO.setI("",0.0,actionControl.getGotToApple());
                                                }else if(command.getString("TYPE").equals("GO_JEWEL")){
                                                    actionMO.setI("",0.0,actionControl.getGoToJewel());
                                                }else if(command.getString("TYPE").equals("GO_TARGET")){
                                                    actionMO.setI("",0.0,actionControl.getGoToTarget());
                                                }
//                                                
                                            }
                                        }
                                        comm = "";
                                    } else {
                                        log.info("Sending stop command to agent");
                                        c.moveto(0,0,0);
                                        System.out.println("Motor > STOP");
                                        comm = "";
                                    }
                                }
                            k++;	
                        }
                        previousAction=comm;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }   
            }
        }//end proc


    @Override
    public void calculateActivation() {
        
    }


}
