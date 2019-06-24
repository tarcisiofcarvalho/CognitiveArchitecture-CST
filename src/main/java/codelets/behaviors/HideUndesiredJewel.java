package codelets.behaviors;

import org.json.JSONObject;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import support.ActionControl;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Thing;

public class HideUndesiredJewel extends Codelet {

	private MemoryObject closestUndesiredJewelsMO;
	private MemoryContainer handsMO;
        private MemoryContainer actionMO;
        private MemoryObject actionControlMO;
        
        ActionControl actionControl;

        int objectId = -1;
        
        List<Thing> closestUndesiredJewels;
        Creature c;
        
	public HideUndesiredJewel(Creature c) {
                //setTimeStep(50);
            this.c = c;
	}

	@Override
	public void accessMemoryObjects() {
		closestUndesiredJewelsMO=(MemoryObject)this.getInput("CLOSEST_UNDESIRED_JEWELS");
                actionControlMO=(MemoryObject)this.getInput("ACTION_CONTROL");
              	handsMO=(MemoryContainer)this.getOutput("HANDS");
                actionMO=(MemoryContainer)this.getOutput("CREATURE_ACTION");
	}

	@Override
	public void proc() {
            actionControl = (ActionControl)this.actionControlMO.getI();
            closestUndesiredJewels = Collections.synchronizedList((List<Thing>) closestUndesiredJewelsMO.getI());
//            System.out.println("closestUndesiredJewels.size(): " + closestUndesiredJewels.size());
            if(closestUndesiredJewels!=null){
                synchronized(closestUndesiredJewels){
                    JSONObject message=new JSONObject();
                    for(Thing t : closestUndesiredJewels){
                        message.put("OBJECT", t.getName());
                        message.put("ACTION", "BURY");
                        if(objectId==-1){
                            objectId = actionMO.setI(message.toString(),0.4);
                            actionControl.setHideJewel(objectId);
                        }else{
                            actionMO.setI(message.toString(),0.4,objectId);
                        }
                        
                        System.out.println("Behaviours > Hide Undesired Jewel");
                    }
                    closestUndesiredJewels = Collections.synchronizedList((new ArrayList<Thing>()));
                    closestUndesiredJewelsMO.setI(closestUndesiredJewels);
                }
                updateJewelControl(); 
            }else{
                if(objectId==-1){
                    objectId = actionMO.setI("",0.0);
                    actionControl.setHideJewel(objectId);
                }else{
                    actionMO.setI("",0.0,objectId);
                }
            }
	}
        
        @Override
        public void calculateActivation() {
        
        }
        
        public void updateJewelControl() {
           
            // --- Removing the Undesired Jewels hidden already from Memory Object --- //
            //synchronized(closestUndesiredJewels) {  

                // --- Cleaing the closestUndesiredJewels Jewel Memory Object, because all jewels related to him were gotten --- //
                closestUndesiredJewels = Collections.synchronizedList(new ArrayList<Thing>());
                
                // --- Updating the Memory Object --- //
                closestUndesiredJewelsMO.setI(closestUndesiredJewels);
            //}
        }

}
