package codelets.behaviors;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.json.JSONException;
import org.json.JSONObject;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import memory.CreatureInnerSense;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import support.JewelControl;
import ws3dproxy.model.Thing;

public class GetDesiredJewel extends Codelet {

	private MemoryObject closestDesiredJewelsMO;
        private MemoryObject desiredJewelsMO;
        private MemoryObject jewelControlMO;
	private MemoryObject handsMO;

        List<Thing> closestDesiredJewels;
        List<Thing> desiredJewels;
        JewelControl jewelControl;
        
	public GetDesiredJewel() {
                setTimeStep(50);
	}

	@Override
	public void accessMemoryObjects() {
		closestDesiredJewelsMO=(MemoryObject)this.getInput("CLOSEST_DESIRED_JEWELS");
                desiredJewelsMO=(MemoryObject)this.getInput("DESIRED_JEWELS");
                jewelControlMO=(MemoryObject)this.getInput("JEWEL_CONTROL");
		handsMO=(MemoryObject)this.getOutput("HANDS");
	}

	@Override
	public void proc() {
            closestDesiredJewels = Collections.synchronizedList((List<Thing>) closestDesiredJewelsMO.getI());
            desiredJewels = Collections.synchronizedList((List<Thing>) desiredJewelsMO.getI());
            jewelControl = (JewelControl) jewelControlMO.getI();
            
            if(closestDesiredJewels!=null){
                //synchronized(closestDesiredJewels){
                    JSONObject message=new JSONObject();
                    for(Thing t : closestDesiredJewels){
                        message.put("OBJECT", t.getName());
                        message.put("ACTION", "PICKUP");
                        handsMO.updateI(message.toString());
                        System.out.println("Behaviours > Get Desired Jewel");
                     //synchronized(jewelControl){
                        jewelControl.processLeafletControl(t.getAttributes().getColor(),t.getName());
                    //}
                    }
                //}
                updateJewelControl(); 
            }else{
                handsMO.updateI("");
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
