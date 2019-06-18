package codelets.perception;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import memory.CreatureInnerSense;
import ws3dproxy.model.Thing;

/**
 * Detect Low Fuel in the Inner Sensory.
 * 	This class detects if the creature fuel level is under than 400.
 * @author Tarcisio
 *
 */
public class LowFuelDetector extends Codelet {

        private MemoryObject innerMO;
        private MemoryObject lowFuelMO;
        private final double lowFuelLevel = 400.0;

	public LowFuelDetector(){
		
	}

	@Override
	public void accessMemoryObjects() {
                synchronized(this) {
		    this.innerMO=(MemoryObject)this.getInput("INNER");
                }
		this.lowFuelMO=(MemoryObject)this.getOutput("LOW_FUEL");
	}

	@Override
	public void proc() {
            synchronized (innerMO) {
               CreatureInnerSense innerSense = (CreatureInnerSense) innerMO.getI();
               Boolean lowFuel = (Boolean) lowFuelMO.getI();
               //System.out.println("Fuel... " + innerSense.fuel);
               synchronized(lowFuel){
                    if(innerSense.fuel<lowFuelLevel){
                        System.out.println("Perception > LowFuel Detector");                     
                        lowFuel = true;
                    }else{
                        //System.out.println("Low fuel > false");
                        lowFuel = false;
                    }
                    lowFuelMO.setI(lowFuel);
               }
            }
	}// end proc
        
        @Override
        public void calculateActivation() {
        
        }


}//end class


