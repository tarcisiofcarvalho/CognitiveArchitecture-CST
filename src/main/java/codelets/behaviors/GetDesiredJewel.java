package codelets.behaviors;

import org.json.JSONObject;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import support.ActionControl;
import support.JewelControl;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Thing;

public class GetDesiredJewel extends Codelet {

	private MemoryObject closestDesiredJewelsMO;
        private MemoryObject desiredJewelsMO;
        private MemoryObject jewelControlMO;
        private MemoryObject actionControlMO;
        private MemoryContainer actionMO;
        
        
        int objectId = -1;

        List<Thing> closestDesiredJewels;
        List<Thing> desiredJewels;
        JewelControl jewelControl;
        ActionControl actionControl;
        Creature c;
        
	public GetDesiredJewel(Creature c) {
                //setTimeStep(50);
            this.c = c;
	}

	@Override
	public void accessMemoryObjects() {
		closestDesiredJewelsMO=(MemoryObject)this.getInput("CLOSEST_DESIRED_JEWELS");
                desiredJewelsMO=(MemoryObject)this.getInput("DESIRED_JEWELS");
                jewelControlMO=(MemoryObject)this.getInput("JEWEL_CONTROL");
                actionControlMO=(MemoryObject)this.getInput("ACTION_CONTROL");
                actionMO=(MemoryContainer)this.getOutput("CREATURE_ACTION");
	}

	@Override
	public void proc() {
            closestDesiredJewels = Collections.synchronizedList((List<Thing>) closestDesiredJewelsMO.getI());
            desiredJewels = Collections.synchronizedList((List<Thing>) desiredJewelsMO.getI());
            jewelControl = (JewelControl) jewelControlMO.getI();
            actionControl = (ActionControl) actionControlMO.getI();
            
            if(closestDesiredJewels!=null){
                synchronized(closestDesiredJewels){
                    JSONObject message=new JSONObject();
                    for(Thing t : closestDesiredJewels){
                        message.put("OBJECT", t.getName());
                        message.put("ACTION", "PICKUP");
                        if(objectId==-1){
                            objectId = actionMO.setI(message.toString(),0.6);
                            actionControl.setGetJewel(objectId);
                        }else{
                            actionMO.setI(message.toString(),0.6,objectId);
                        }
                        System.out.println("Behaviours > Get Desired Jewel");
                        jewelControl.processLeafletControl(t.getAttributes().getColor(),t.getName());
                    }
                    closestDesiredJewels = Collections.synchronizedList((new ArrayList<Thing>()));
                    closestDesiredJewelsMO.setI(closestDesiredJewels);
                }
                updateJewelControl(); 
            }else{
                if(objectId==-1){
                    objectId = actionMO.setI("",0.0);
                    actionControl.setGetJewel(objectId);
                }else{
                    actionMO.setI("",0.0, objectId);
                }
            }
	}
        
        @Override
        public void calculateActivation() {
        
        }
        
        public void updateJewelControl() {
           
            // --- Removing the Desired Jewels gotten already from Memory Object --- //
            //synchronized(closestDesiredJewels) {  
                CopyOnWriteArrayList<Thing> myknown = new CopyOnWriteArrayList<>(desiredJewels);
                for(int i=0;i<closestDesiredJewels.size();i++){
                    int pos = -1;
                    for(int j=0;j<myknown.size();j++){
                        if(closestDesiredJewels.get(i).getName().equals(myknown.get(j).getName())){
                            pos = j; // Postition to be removed
                            break;
                        }
                    }
                    if(pos!=-1)
                        myknown.remove(pos);     
                }
                // --- Cleaing the closestDesired Jewel Memory Object, because all jewels related to him were gotten --- //
                closestDesiredJewels = Collections.synchronizedList(new ArrayList<Thing>());
                
                // Updating the Memory Object
                desiredJewelsMO.setI(myknown);
                closestDesiredJewelsMO.setI(closestDesiredJewels);
            //}
        }

}
