/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Leaflet;

/**
 *
 * @author tfc
 */
public class JewelControl {
    
    private int leafletRemainingJewelWhite = 0;
    private int leafletRemainingJewelRed = 0;
    private int leafletRemainingJewelBlue = 0;
    private int leafletRemainingJewelYellow = 0;
    private int leafletRemainingJewelMagenta = 0;
    private int leafletRemainingJewelGreen = 0;
    private int leafletRemainingJewelOrange = 0;
    private int leafletRemainingJewelDarkGray_Spoiled = 0;
    private List<String> processedJewels;
    
    public JewelControl(Creature c){

        processedJewels = new ArrayList<String>();
        for(Leaflet lf: c.getLeaflets()){
            for (Map.Entry<String, Integer[]> entry : lf.getItems().entrySet()) {
                 if(entry.getKey().equals("White")){
                     leafletRemainingJewelWhite++;
                 }else if(entry.getKey().equals("Red")){
                     leafletRemainingJewelRed++;
                 }else if(entry.getKey().equals("Blue")){
                     leafletRemainingJewelBlue++;
                 }else if(entry.getKey().equals("Yellow")){
                     leafletRemainingJewelYellow++;
                 }else if(entry.getKey().equals("Magenta")){
                     leafletRemainingJewelMagenta++;
                 }else if(entry.getKey().equals("Green")){
                     leafletRemainingJewelGreen++;
                 }else if(entry.getKey().equals("Orange")){
                     leafletRemainingJewelOrange++;
                 }else if(entry.getKey().equals("DarkGray_Spoiled")){
                     leafletRemainingJewelDarkGray_Spoiled++;
                 }
            }
        } 
    }

    public void processLeafletControl(String color){

        if(color.equals("White")){
            if(leafletRemainingJewelWhite > 0) leafletRemainingJewelWhite--;
        }else if(color.equals("Red")){
            if(leafletRemainingJewelRed > 0) leafletRemainingJewelRed--;
        }else if(color.equals("Blue")){
            if(leafletRemainingJewelBlue > 0) leafletRemainingJewelBlue--;
        }else if(color.equals("Yellow")){
            if(leafletRemainingJewelYellow > 0) leafletRemainingJewelYellow--;
        }else if(color.equals("Magenta")){
            if(leafletRemainingJewelMagenta > 0) leafletRemainingJewelMagenta--;
        }else if(color.equals("Green")){
            if(leafletRemainingJewelGreen > 0) leafletRemainingJewelGreen--;
        }else if(color.equals("Orange")){
            if(leafletRemainingJewelOrange > 0) leafletRemainingJewelOrange--;
        }else if(color.equals("DarkGray_Spoiled")){
            if(leafletRemainingJewelDarkGray_Spoiled > 0) leafletRemainingJewelDarkGray_Spoiled--;
        }
    }

    public void processLeafletControl(String color, String JewelName){
        
        boolean check = true;
        for(String s : this.processedJewels){
            if(s.equals(JewelName)){
                check = false;
            }
        }
        if(check){   
            if(color.equals("White")){
                if(leafletRemainingJewelWhite > 0) leafletRemainingJewelWhite--;
            }else if(color.equals("Red")){
                if(leafletRemainingJewelRed > 0) leafletRemainingJewelRed--;
            }else if(color.equals("Blue")){
                if(leafletRemainingJewelBlue > 0) leafletRemainingJewelBlue--;
            }else if(color.equals("Yellow")){
                if(leafletRemainingJewelYellow > 0) leafletRemainingJewelYellow--;
            }else if(color.equals("Magenta")){
                if(leafletRemainingJewelMagenta > 0) leafletRemainingJewelMagenta--;
            }else if(color.equals("Green")){
                if(leafletRemainingJewelGreen > 0) leafletRemainingJewelGreen--;
            }else if(color.equals("Orange")){
                if(leafletRemainingJewelOrange > 0) leafletRemainingJewelOrange--;
            }else if(color.equals("DarkGray_Spoiled")){
                if(leafletRemainingJewelDarkGray_Spoiled > 0) leafletRemainingJewelDarkGray_Spoiled--;
            }
            this.processedJewels.add(JewelName);
        }
    }

    public boolean isDesiredJewel (String color){
        if(color.equals("White")){
            if(leafletRemainingJewelWhite > 0) return true;
        }else if(color.equals("Red")){
            if(leafletRemainingJewelRed > 0) return true;
        }else if(color.equals("Blue")){
            if(leafletRemainingJewelBlue > 0) return true;
        }else if(color.equals("Yellow")){
            if(leafletRemainingJewelYellow > 0) return true;
        }else if(color.equals("Magenta")){
            if(leafletRemainingJewelMagenta > 0) return true;
        }else if(color.equals("Green")){
            if(leafletRemainingJewelGreen > 0) return true;
        }else if(color.equals("Orange")){
            if(leafletRemainingJewelOrange > 0) return true;
        }else if(color.equals("DarkGray_Spoiled")){
            if(leafletRemainingJewelDarkGray_Spoiled > 0) return true;
        }
        
        return false;
    }
               
    public int getJewelRemainingTotal(){
        int result = leafletRemainingJewelWhite+
                    leafletRemainingJewelRed+
                    leafletRemainingJewelBlue+
                    leafletRemainingJewelYellow+
                    leafletRemainingJewelMagenta+
                    leafletRemainingJewelGreen+
                    leafletRemainingJewelOrange+
                    leafletRemainingJewelDarkGray_Spoiled;
        return result;
    }    
    
}
