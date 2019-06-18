package codelets.perception;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import memory.CreatureInnerSense;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Thing;

/**
 * Detect Low Fuel in the Inner Sensory.
 * 	This class detects if the creature fuel level is under than 400.
 * @author Tarcisio
 *
 */
public class LeafletGenerationStatus extends Codelet {

        private MemoryObject jewelControllMO;
        private MemoryObject goalAchievedlMO;
        
        private Creature c;

	public LeafletGenerationStatus(Creature c){
            this.c = c;
	}

	@Override
	public void accessMemoryObjects() {
                synchronized(this) {
		    this.jewelControllMO=(MemoryObject)this.getInput("JEWEL_CONTROL");
                }
		this.goalAchievedlMO=(MemoryObject)this.getOutput("GOAL_ACHIEVED");
	}

	@Override
	public void proc() {
            // --- Check if the leaflets were already generated
            if(this.c.getLeaflets().size()>0){
                
            }

	}// end proc
        
        @Override
        public void calculateActivation() {
        
        }


}//end class


