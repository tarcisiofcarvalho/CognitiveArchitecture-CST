/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import ws3dproxy.CommandExecException;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Environment;
import ws3dproxy.model.Leaflet;
import ws3dproxy.model.World;

/**
 *
 * @author tfc
 */
public class GoalAchieved {
    
    private boolean jewelGoalstatus;
    private boolean gameGoalstatus;
    
    public GoalAchieved(){
        this.jewelGoalstatus = false;
    }
    
    public void setJewelGoalStatus(boolean status){
        this.jewelGoalstatus = status;
    }
    
    public boolean getJewelGoalStatus(){
        return this.jewelGoalstatus;
    }    
    
    public void setGameGoalStatus(boolean status){
        this.gameGoalstatus = status;
    }
    
    public boolean getGameGoalStatus(){
        return this.gameGoalstatus;
    }    
}
