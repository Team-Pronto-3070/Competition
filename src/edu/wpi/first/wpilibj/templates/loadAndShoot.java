/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author pronto1
 */
public class loadAndShoot extends Thread {

    boolean running = false;
    Solenoid sol4, sol5, sol7, sol8;
    Victor victor;
    Encoder encoder;
    DigitalInput digi2, digi3;
    Joystick xBox;
    Shoot Shoot;
    boolean shooting = false; //starts the catapult
    int justShootCount = 0;
    Load Load;
    boolean loadingWithBall = false; //loads with more force, to compensate for the ball
    boolean loadingWithoutBall = false; //loads with less force
    boolean loaded = false; //checks if the suction arm is in the catapult or not
    Unload Unload;
    boolean unloading = false; //extends the suction cup
    SuckUpBall SuckUpBall;
    boolean okToSuck = false;
    boolean sucking = false; //begins the autosucker
    int suckingCount = 0;
    boolean doNotSuck = false; //toggles on/off the auto suction
    int doNotSuckCount = 0;
    boolean turnOffSuck = false; //disables the auto suction for 5 seconds (or longer)
    int turnOffSuckCount = 0;

    public loadAndShoot(Encoder e, Victor v, Solenoid s4, Solenoid s5, Solenoid s7, Solenoid s8, Joystick x, DigitalInput d2, DigitalInput d3) {
        victor = v;
        encoder = e;

        sol4 = s4;
        sol5 = s5;

        sol7 = s7;
        sol8 = s8;

        xBox = x;

        digi2 = d2;
        digi3 = d3;

        Shoot = new Shoot(victor, sol4, sol5, sol7, sol8, Unload);
        Load = new Load(victor);
        Unload = new Unload(victor, sol4, sol5);
        SuckUpBall = new SuckUpBall(victor, sol4, sol5);
    }

    public void setRun(boolean run) {
        running = run;
    }

    public void run() {
        while (true) {
            shooting = false;
            loadingWithBall = false;
            loadingWithoutBall = false;
            unloading = false;
            loaded = false;
            justShootCount = 0;
            okToSuck = false;
            sucking = false;
            suckingCount = 0;
            doNotSuck = false;
            doNotSuckCount = 0;
            turnOffSuck = false;
            turnOffSuckCount = 0;
            while (running) {
                //begin unsuction or suction on command
                if (!loadingWithBall && !loadingWithoutBall && !shooting && !sucking && !loaded) {
                    if (xBox.getRawAxis(3) > .95) { //suction- look at if(sucking){}
                        sucking = true;
                    }
                    if (xBox.getRawButton(3)) { //stop suction
                        sol4.set(false);
                        sol5.set(true);
                        okToSuck = true; //to reset the delay on the sucker
                        turnOffSuck = true; // stops the autosucker from picking up again immediately
                    }
                }
                if (turnOffSuck) {
                    turnOffSuckCount++;
                }
                if (turnOffSuckCount > 250/*<-may need to adjust this number*/) {
                    turnOffSuck = false;
                    turnOffSuckCount = 0;
                }
                //end unsuction or suction on command

                //begin autosucker
                if (xBox.getRawButton(4) && !doNotSuck) { //toggle on
                    doNotSuck = true;
                    doNotSuckCount++;
                }
                if (xBox.getRawButton(4) && doNotSuck && doNotSuckCount > 150) { //toggle off
                    doNotSuck = false;
                    doNotSuckCount = 0;
                }
                if (digi2.get() && !loadingWithBall && !loadingWithoutBall && !unloading && !shooting && !sucking && okToSuck && !doNotSuck && !turnOffSuck) {
                    sucking = true;
                }
                if (sucking) {
                    suckingCount++;
                    SuckUpBall.suckItUp();
                }
                if (suckingCount > 20/*<-may need to adjust this number, time for suction*/) {
                    sucking = false;
                    okToSuck = false;
                }
                //end autosucker

                //begin loading
                if (xBox.getRawButton(6) && !loadingWithBall && !loadingWithoutBall && !unloading && !shooting && digi2.get()) {
                    loadingWithBall = true;
                }
                if (xBox.getRawButton(6) && !loadingWithBall && !loadingWithoutBall && !unloading && !shooting && !digi2.get()) {
                    loadingWithoutBall = true;
                }
                if (loadingWithBall) {
                    Load.loadWithBall();
                }
                if (loadingWithoutBall) {
                    Load.loadWithoutBall();
                }
                if (encoder.get() == 0/*<-insert right number*/ || digi3.get()) {
                    victor.set(0);
                    loadingWithBall = false;
                    loadingWithoutBall = false;
                    loaded = true;
                }
                //end loading

                //begin shooter
                if (xBox.getRawAxis(3) < -.95 && !loadingWithBall && !loadingWithoutBall && !unloading && !shooting) {
                    shooting = true;
                }
                if (shooting && loaded) {
                    Shoot.shootPlusUnload();
                    if (encoder.get() == 0/*<-insert right number*/) {
                        shooting = false;
                        loaded = false;
                    }
                }
                if (shooting && !loaded) {
                    justShootCount++;
                    Shoot.justShoot();
                    if (justShootCount > 60/*<-may need to adjust this number a little*/) {
                        shooting = false;
                    }
                }
                //end shooter

                //begin Unload
                if (xBox.getRawButton(5) && !loadingWithBall && !loadingWithoutBall && !unloading && !shooting) {
                    unloading = true;
                }
                if (unloading) {
                    Unload.unload();
                }
                if (encoder.get() == 0/*<-insert right number*/) {
                    unloading = false;
                    loaded = false;
                    okToSuck = true;
                }
                //end Unload
            }
        }
    }
}
