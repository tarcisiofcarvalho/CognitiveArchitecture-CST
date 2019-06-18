/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

/**
 *
 * @author tfc
 */
public class GoalAchieved {
    
    private boolean jewelGoalstatus;
    private boolean gameGoalstatus;
    private boolean finished;
    private boolean leafletGenerationStatus;
    
    public GoalAchieved(){
        this.jewelGoalstatus = false;
        this.gameGoalstatus = false;
        this.finished = false;
    }
    
    public boolean getLeafletGenerationStatus(){
        return this.leafletGenerationStatus;
    }
    
    public void setLeafletGenerationStatus(boolean leafletGenerationStatus){
        this.leafletGenerationStatus = leafletGenerationStatus;
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

    public void setFinishedStatus(boolean finished){
        this.finished = finished;
    }
    
    public boolean getFinishStatus(){
        return this.finished;
    }
}
