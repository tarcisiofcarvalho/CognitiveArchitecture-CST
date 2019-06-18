package codelets.perception;



import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.ArrayList;
import java.util.Collections;
import memory.CreatureInnerSense;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import ws3dproxy.model.Thing;

/**
 * @author klaus
 *
 */
public class ClosestDesiredJewelDetector extends Codelet {

	private MemoryObject desiredJewelMO;
	private MemoryObject closestDesiredJewelMO;
	private MemoryObject innerSenseMO;
        int reachDistance;
	
        private List<Thing> desiredJewels;
        private List<Thing> closestDesiredJewels;

	public ClosestDesiredJewelDetector(int reachDistance) {
            this.reachDistance = reachDistance;
	}


	@Override
	public void accessMemoryObjects() {
		this.desiredJewelMO=(MemoryObject)this.getInput("DESIRED_JEWELS");
		this.innerSenseMO=(MemoryObject)this.getInput("INNER");
		this.closestDesiredJewelMO=(MemoryObject)this.getOutput("CLOSEST_DESIRED_JEWELS");	
	}
	@Override
	public void proc() {
            desiredJewels = Collections.synchronizedList((List<Thing>) desiredJewelMO.getI());
            closestDesiredJewels = Collections.synchronizedList((List<Thing>) closestDesiredJewelMO.getI());
            closestDesiredJewels = Collections.synchronizedList((new ArrayList<Thing>()));
            CreatureInnerSense cis = (CreatureInnerSense) innerSenseMO.getI();
            synchronized(desiredJewels) {
               if(desiredJewels.size() != 0){
                    // --- Iterate over desired jewels to check if some one is close then creature --- //
                    CopyOnWriteArrayList<Thing> myDesiredJewels = new CopyOnWriteArrayList<>(desiredJewels);
                    for (Thing t : myDesiredJewels) {
                        boolean check = true;
                        double distance = calculateDistance(t.getX1(), t.getY1(), cis.position.getX(), cis.position.getY());
                        if(distance < reachDistance){
                            synchronized(closestDesiredJewels){
                                for(Thing j : closestDesiredJewels){
                                    if(j.getName().equals(t.getName())){
                                        check = false;
                                    }
                                }
                                if(check){
                                    closestDesiredJewels.add(t);
                                    System.out.println("Perception > Desired Jewel Ahead Detector");
                                }
                            }  
                        }
                    }
                    // --- Setting the closest Desired Jewel in Memory Object --- //
                    closestDesiredJewelMO.setI(closestDesiredJewels);
               }
            }
	}//end proc

@Override
        public void calculateActivation() {
        
        }
        
        private double calculateDistance(double x1, double y1, double x2, double y2) {
            return(Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)));
        }

}
