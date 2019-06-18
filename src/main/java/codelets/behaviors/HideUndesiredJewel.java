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

public class HideUndesiredJewel extends Codelet {

	private MemoryObject closestUndesiredJewelsMO;
	private MemoryObject handsMO;

        List<Thing> closestUndesiredJewels;
        
	public HideUndesiredJewel() {
                //setTimeStep(50);
	}

	@Override
	public void accessMemoryObjects() {
		closestUndesiredJewelsMO=(MemoryObject)this.getInput("CLOSEST_UNDESIRED_JEWELS");
              	handsMO=(MemoryObject)this.getOutput("HANDS");
	}

	@Override
	public void proc() {
            closestUndesiredJewels = Collections.synchronizedList((List<Thing>) closestUndesiredJewelsMO.getI());
//            System.out.println("closestUndesiredJewels.size(): " + closestUndesiredJewels.size());
            if(closestUndesiredJewels!=null){
                synchronized(closestUndesiredJewels){
                    JSONObject message=new JSONObject();
                    for(Thing t : closestUndesiredJewels){
                        message.put("OBJECT", t.getName());
                        message.put("ACTION", "BURY");
                        handsMO.updateI(message.toString());
                        System.out.println("Behaviours > Hide Undesired Jewel");
                    }
                    closestUndesiredJewels = Collections.synchronizedList((new ArrayList<Thing>()));
                    closestUndesiredJewelsMO.setI(closestUndesiredJewels);
                }
                updateJewelControl(); 
            }else{
                handsMO.updateI("");	//nothing
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
