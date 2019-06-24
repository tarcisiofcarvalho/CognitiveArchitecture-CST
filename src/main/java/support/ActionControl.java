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
public class ActionControl {

    public int getEatApple() {
        return eatApple;
    }

    public void setEatApple(int eatApple) {
        this.eatApple = eatApple;
    }

    public int getGetJewel() {
        return getJewel;
    }

    public void setGetJewel(int getJewel) {
        this.getJewel = getJewel;
    }

    public int getHideJewel() {
        return hideJewel;
    }

    public void setHideJewel(int hideJewel) {
        this.hideJewel = hideJewel;
    }

    public int getForage() {
        return forage;
    }

    public void setForage(int forage) {
        this.forage = forage;
    }

    public int getGoToJewel() {
        return goToJewel;
    }

    public void setGoToJewel(int goToJewel) {
        this.goToJewel = goToJewel;
    }

    public int getGotToApple() {
        return gotToApple;
    }

    public void setGotToApple(int gotToApple) {
        this.gotToApple = gotToApple;
    }

    public int getGoToTarget() {
        return goToTarget;
    }

    public void setGoToTarget(int goToTarget) {
        this.goToTarget = goToTarget;
    }
    
    private int eatApple = -1;
    private int getJewel = -1;
    private int hideJewel = -1;
    private int forage = -1;
    private int goToJewel = -1;
    private int gotToApple = -1;
    private int goToTarget = -1;
    
    
    public ActionControl(){

    }
 
}
