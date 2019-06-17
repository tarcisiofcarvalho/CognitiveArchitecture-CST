package codelets.behaviors;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.json.JSONException;
import org.json.JSONObject;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.List;
import memory.CreatureInnerSense;
import ws3dproxy.model.Thing;

public class GoToDesiredJewel extends Codelet {

	private MemoryObject desiredJewelsMO;
	private MemoryObject selfInfoMO;
	private MemoryObject legsMO;
	private int creatureBasicSpeed;
	private double reachDistance;

	public GoToDesiredJewel(int creatureBasicSpeed, int reachDistance) {
		this.creatureBasicSpeed=creatureBasicSpeed;
		this.reachDistance=reachDistance;
	}

	@Override
	public void accessMemoryObjects() {
		desiredJewelsMO=(MemoryObject)this.getInput("DESIRED_JEWELS");
		selfInfoMO=(MemoryObject)this.getInput("INNER");
                legsMO=(MemoryObject)this.getOutput("LEGS");
	}

	@Override
	public void proc() {
            // Find distance between creature and closest apple
            //If far, go towards it
            //If close, stops
            
            List<Thing> desiredJewels = (List<Thing>) desiredJewelsMO.getI();
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
                                //System.out.println("Go to desired jewel: "+nearestJewel.getName());

                        }
                        else{//Stop
                                message.put("ACTION", "GOTO");
                                message.put("X", (int)nearestJewel.getX1());
                                message.put("Y", (int)nearestJewel.getY1());
                                message.put("SPEED", 0.0);	
                                //System.out.println("Closest to the desired jewel: "+nearestJewel.getName());
                        }
                        legsMO.updateI(message.toString());
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
