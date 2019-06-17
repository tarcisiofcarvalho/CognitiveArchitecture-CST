package codelets.perception;



import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.ArrayList;
import java.util.Collections;
import memory.CreatureInnerSense;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import support.JewelControl;
import ws3dproxy.model.Thing;
import ws3dproxy.util.Constants;

/**
 * @author klaus
 *
 */
public class ClosestUndesiredJewelDetector extends Codelet {

	private MemoryObject closestUndesiredJewelMO;
        private MemoryObject jewelControlMO;
	private MemoryObject innerSenseMO;
        private MemoryObject visionMO;
        int reachDistance;

        private List<Thing> closestUndesiredJewels;
        private JewelControl jewelControl;
        
	public ClosestUndesiredJewelDetector(int reachDistance) {
            this.reachDistance = reachDistance;
            System.out.println("codelets.perception.ClosestDesiredJewelDetector.<init>()");
	}


	@Override
	public void accessMemoryObjects() {
		this.innerSenseMO=(MemoryObject)this.getInput("INNER");
                this.jewelControlMO=(MemoryObject)this.getInput("JEWEL_CONTROL");
                this.visionMO=(MemoryObject)this.getInput("VISION");
		this.closestUndesiredJewelMO=(MemoryObject)this.getOutput("CLOSEST_UNDESIRED_JEWELS");	
	}
	@Override
	public void proc() {
            closestUndesiredJewels = Collections.synchronizedList((List<Thing>) closestUndesiredJewelMO.getI());
            closestUndesiredJewels = Collections.synchronizedList((new ArrayList<Thing>()));
            jewelControl = (JewelControl) jewelControlMO.getI();
            CreatureInnerSense cis = (CreatureInnerSense) innerSenseMO.getI();
            CopyOnWriteArrayList<Thing> vision = new CopyOnWriteArrayList((List<Thing>) visionMO.getI());
            
            synchronized(vision) {
               for(Thing t : vision){
                  // --- Looking for jewels --- //
                  if(t.getAttributes().getCategory()==Constants.categoryJEWEL){
                        // --- Check if its is in front of creature --- //
                        boolean check = true;
                        double distance = calculateDistance(t.getX1(), t.getY1(), cis.position.getX(), cis.position.getY());
                        if(distance < reachDistance){
                            synchronized(closestUndesiredJewels){
                                for(Thing j : closestUndesiredJewels){
                                    if(j.getName().equals(t.getName())){
                                        check = false;
                                    }
                                }
                                // -- Checking if it is an undesired jewel --- //
                                if(check && !jewelControl.isDesiredJewel(t.getAttributes().getColor())){
                                    closestUndesiredJewels.add(t);
                                    System.out.println("Undesired jewel ahead: " + t.getName());
                                }
                            }  
                        }                        
                    }
                }
            }
            // --- Setting the closest Desired Jewel in Memory Object --- //
            closestUndesiredJewelMO.setI(closestUndesiredJewels);
	}//end proc

@Override
        public void calculateActivation() {
        
        }
        
        private double calculateDistance(double x1, double y1, double x2, double y2) {
            return(Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)));
        }

}
