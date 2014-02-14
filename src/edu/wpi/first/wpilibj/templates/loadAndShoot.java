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
    boolean shooting = false;
    int justShootCount = 0;
    Load Load;
    boolean loadingWithBall = false;
    boolean loadingWithoutBall = false;
    boolean loaded = false;
    Unload Unload;
    boolean unloading = false;
    SuckUpBall SuckUpBall;
    boolean sucking = false;
    boolean doNotSuck = false;
    int noSuckingCount = 0;
    int suckingCount = 0;

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
            sucking = false;
            suckingCount = 0;
            doNotSuck = false;
            noSuckingCount = 0;
            while (running) {
                //begin unsuction or suction on command
                if (!loadingWithBall && !loadingWithoutBall && !shooting && !sucking && !loaded) {
                    if (xBox.getRawButton(3)) {
                        sol4.set(false); //suction
                        sol5.set(true);
                        doNotSuck = true; // stops the autosucker from picking up again immediately
                    }
                    if (xBox.getRawButton(4)) {
                        sol4.set(true); //stop suction
                        sol5.set(false);
                    }
                }
                if (doNotSuck) {
                    noSuckingCount++;
                }
                if (noSuckingCount > 250/*<-may need to adjust this number*/) {
                    doNotSuck = false;
                    noSuckingCount = 0;
                }
                //end unsuction or suction on command

                //begin autosucker
                if (digi2.get() && !loadingWithBall && !loadingWithoutBall && !unloading && !shooting && !sucking && !doNotSuck) {
                    sucking = true;
                }
                if (sucking) {
                    suckingCount++;
                    SuckUpBall.suckItUp();
                }
                if (suckingCount > 20/*<-may need to adjust this number, time for suction*/) {
                    sucking = false;
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
                }
                //end Unload
            }
        }
    }
}
