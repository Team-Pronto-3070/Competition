/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author pronto1
 */
public class loadAndShoot extends Thread {

    boolean running = false;
    Solenoid sol4, sol5, sol7, sol8;
    Victor victor;
    AnalogChannel encoder;
    DigitalInput digi14, digi3;
    Joystick xBox;
    Shoot shooter;
    boolean shooting = false; //starts the catapult
    int justShootCount = 0;
    Load Load;
    boolean loaded = false;
    boolean loadingWithBall = false; //loads with more force, to compensate for the ball
    boolean loadingWithoutBall = false; //loads with less force
    Unload Unload;
    boolean unloading = false; //extends the suction cup
    SuckUpBall SuckUpBall;
    boolean okToSuck = false; //toggles on/off the auto suction
    boolean sucking = false; //begins the autosuction
    int suckingCount = 0;
    boolean doNotSuck = false; //stops the autosuction from screwing up

    public loadAndShoot(AnalogChannel e, Victor v, Solenoid s4, Solenoid s5, Solenoid s7, Solenoid s8, Joystick x, DigitalInput d14, DigitalInput d3) {
        victor = v;
        encoder = e;

        sol4 = s4;
        sol5 = s5;

        sol7 = s7;
        sol8 = s8;

        xBox = x;

        digi14 = d14;
        digi3 = d3;

        shooter = new Shoot(victor, sol4, sol5, sol7, sol8);
        Load = new Load(victor, encoder);
        Unload = new Unload(victor, sol4, sol5);
        SuckUpBall = new SuckUpBall(victor, sol4, sol5);
    }

    public void setRun(boolean run) {
        running = run;
    }

    public void run() {
        while (true) {
            victor.set(0);
            shooting = false;
            loadingWithBall = false;
            loadingWithoutBall = false;
            loaded = false;
            unloading = false;
            justShootCount = 0;
            okToSuck = true;
            sucking = false;
            suckingCount = 0;
            doNotSuck = false;
            while (running) {
                if (digi3.get()) {
                    loaded = true;
                }
                //begin suction on command
                if (!loadingWithBall && !loadingWithoutBall && !shooting && !sucking && !digi3.get()) {
                    if (xBox.getRawAxis(3) > .8) { //suction- look at if(sucking){}
                        sucking = true;
                    }
                }
                //end suctions on command

                //begin autosuction and manual suction
                if (xBox.getRawButton(4)) { //toggle on
                    sol4.set(false);
                    sol5.set(true);
                    okToSuck = true;
                    doNotSuck = false;
                }
                if (xBox.getRawButton(3)) { //toggle off
                    okToSuck = false;
                }
                System.out.println("doNotSuck: " + doNotSuck);
                System.out.println("okToSuck" + okToSuck);
                if (digi14.get() && !loadingWithBall && !loadingWithoutBall && !unloading && !shooting && !sucking && okToSuck && !doNotSuck) {
                    sucking = true;
                }
                if (sucking) {
                    suckingCount++;
                    SuckUpBall.suckItUp();
                }
                if (suckingCount > 20) {
                    sucking = false;
                    doNotSuck = true;
                }
                //end autosuction and manual suction

                //begin loading
                if (xBox.getRawButton(6) && !loadingWithBall && !loadingWithoutBall && !unloading && !shooting) {
                    System.out.println("3");
                    if (digi14.get()) {
                        loadingWithBall = true;
                    }
                    if (!digi14.get()) {
                        loadingWithoutBall = true;
                    }
                }
                if (loadingWithBall) {
                    Load.load();
                }
                if (loadingWithoutBall) {
                    victor.set(0.3);
                }
                if (encoder.getVoltage() <= 1.5|| digi3.get()) {
                    victor.set(0);
                    loadingWithBall = false;
                    loadingWithoutBall = false;
                    sol4.set(false);
                    sol5.set(true);
                }
                //end loading

                //begin shooter
                if (xBox.getRawAxis(3) < -.95 && !loadingWithBall && !loadingWithoutBall && !unloading && !shooting) {
                    shooting = true;
                    shooter.setCountToZero();
                }
                if (shooting && (loaded || digi3.get())) {
                    shooter.shootPlusUnload();
                    if (encoder.getVoltage() >= 3.75) {
                        shooting = false;
                        loaded = false;
                        victor.set(0);
                        doNotSuck = false;
                    }
                }
                if (shooting && !loaded && !digi3.get()) {
                    justShootCount++;
                    shooter.justShoot();
                    if (justShootCount > 105/*
                             * <-may need to adjust this number a little
                             */) {
                        shooting = false;
                        justShootCount = 0;
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
                if (encoder.getVoltage() >= 3.75) {
                    unloading = false;
                    okToSuck = true;
                    loaded = false;
                    victor.set(0);
                    doNotSuck = false;
                }
                //end Unload
            }
        }
    }
}