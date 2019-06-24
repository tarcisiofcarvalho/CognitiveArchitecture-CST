package codelets.behaviors;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.json.JSONException;
import org.json.JSONObject;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.List;
import memory.CreatureInnerSense;
import support.ActionControl;
import ws3dproxy.model.Thing;

public class GoToDesiredJewel extends Codelet {

	private MemoryObject desiredJewelsMO;
	private MemoryObject selfInfoMO;
        private MemoryObject actionControlMO;
        private MemoryContainer actionMO;
	private int creatureBasicSpeed;
	private double reachDistance;
        
        int objectId = -1;
        ActionControl actionControl;

	public GoToDesiredJewel(int creatureBasicSpeed, int reachDistance) {
		this.creatureBasicSpeed=creatureBasicSpeed;
		this.reachDistance=reachDistance;
	}

	@Override
	public void accessMemoryObjects() {
            desiredJewelsMO=(MemoryObject)this.getInput("DESIRED_JEWELS");
            actionControlMO=(MemoryObject)this.getInput("ACTION_CONTROL");
            selfInfoMO=(MemoryObject)this.getInput("INNER");
            actionMO=(MemoryContainer)this.getOutput("CREATURE_ACTION");
	}

	@Override
	public void proc() {
            // Find distance between creature and closest apple
            //If far, go towards it
            //If close, stops
            
            List<Thing> desiredJewels = (List<Thing>) desiredJewelsMO.getI();
            actionControl = (ActionControl) actionControlMO.getI();
            CreatureInnerSense cis = (CreatureInnerSense) selfInfoMO.getI();
            
            Thing nearestJewel = null;
            double distance = 9999;
            if(desiredJewels!=null){
                for(Thing j : desiredJewels){
                    double selfX=cis.position.getX();
                    double selfY=cis.position.getY();

                    Point2D pJewel = new Point();
                    pJewel.setLocation(j.getX1(), j.getY1());

                    Point2D pSelf = new Point();
                    pSelf.setLocation(selfX, selfY);

                    double jewelDist = pSelf.distance(pJewel);
                    if(jewelDist<distance){
                        distance = jewelDist;
                        nearestJewel = j;
                    }                
                }
                if(nearestJewel!=null){
                    JSONObject message=new JSONObject();
                    try {
                        if(distance>reachDistance){ //Go to it
                                message.put("ACTION", "GOTO");
                                message.put("X", (int)nearestJewel.getX1());
                                message.put("Y", (int)nearestJewel.getY1());
                                message.put("SPEED", creatureBasicSpeed);
                                message.put("TYPE", "GO_JEWEL");
                                if(objectId==-1){
                                    objectId = actionMO.setI(message.toString(),0.3);
                                    actionControl.setGoToJewel(objectId);
                                }else{
                                    actionMO.setI(message.toString(),0.3,objectId);
                                }
                                System.out.println("Behaviours > Go To Desired Jewel");

                        }
                        
                    } catch (JSONException e) {
                            e.printStackTrace();
                    }
                }
            }
        }//end proc
        
        @Override
        public void calculateActivation() {
        
        }

}
