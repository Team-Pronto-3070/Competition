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
    int ShootCount = 0;
    Load Load;
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
            sol7.set(true);
            sol8.set(false);
            victor.set(0);
            shooting = false;
            loadingWithBall = false;
            loadingWithoutBall = false;
            unloading = false;
            ShootCount = 0;
            okToSuck = true;
            sucking = false;
            suckingCount = 0;
            doNotSuck = false;
            while (running) {
                //begin suction on command
                if (!loadingWithBall && !loadingWithoutBall && !shooting && !sucking && !digi3.get()) {
                    if (xBox.getRawAxis(3) > .8) { //suction- look at if(sucking){}
                        System.out.println("manual suction");
                        sucking = true;
                    }
                }
                //end suctions on command

                //begin autosuction and manual suction
                if (xBox.getRawButton(3)) { //toggle off
                    System.out.println("off suction");
                    sol4.set(false);
                    sol5.set(true);
                    okToSuck = false;
                }
                if (xBox.getRawButton(4)) { //toggle on
                    System.out.println("on suction");
                    okToSuck = true;
                    doNotSuck = false;
                }
                if (digi14.get() && !loadingWithBall && !loadingWithoutBall && !unloading && !shooting && !sucking && okToSuck && !doNotSuck) {
                    sucking = true;
                    System.out.println("auto suction");
                }
                if (sucking) {
                    System.out.println("is autosuction");
                    suckingCount++;
                    SuckUpBall.suckItUp();
                }
                if (suckingCount > 100) {
                    suckingCount = 0;
                    victor.set(0);
                    System.out.println("end asuction");
                    sucking = false;
                    doNotSuck = true;
                }
                //end autosuction and manual suction

                //begin loading
                if (xBox.getRawButton(6) && !loadingWithBall && !loadingWithoutBall && !unloading && !shooting) {
                    if (sol4.get()==true && sol5.get()==false) {
                        System.out.println("lead with ball");
                        loadingWithBall = true;
                    }
                    if (sol4.get()==false && sol5.get()==true) {
                        System.out.println("lead w/out ball");
                        loadingWithoutBall = true;
                    }
                }
                if (loadingWithBall) {
                    System.out.println("LOAD w/ ball");
                    Load.load();
                }
                if (loadingWithoutBall) {
                    System.out.println("load w/out ball");
                    Load.loadWithoutBall();
                }
                if (digi3.get()) {
                    System.out.println("load stop");
                    sol4.set(false);
                    sol5.set(true);
                    victor.set(0);
                    loadingWithBall = false;
                    loadingWithoutBall = false;
                }
                //end loading

                //begin shooter
                if (xBox.getRawAxis(3) < -.95 && !loadingWithBall && !loadingWithoutBall && !unloading && !shooting) {
                    shooting = true;
                    shooter.setCountToZero();
                }
                if (shooting) {
                    ShootCount++;
                    shooter.Shoot();
                    if (ShootCount > 105) {
                        System.out.println("yend shoot");
                        shooting = false;
                        ShootCount = 0;
                    }
                }
                //end shooter

                //begin Unload
                if (xBox.getRawButton(5) && !loadingWithBall && !loadingWithoutBall && !unloading && !shooting) {
                    unloading = true;
                    System.out.println("unload");
                }
                if (unloading) {
                    System.out.println("Yunload");
                    Unload.unload();
                }
                if (encoder.getVoltage() >= 2.5 && encoder.getVoltage() <= 3.8 && unloading) {
                    System.out.println("end yunload");
                    unloading = false;
                    okToSuck = true;
                    victor.set(0);
                    doNotSuck = false;
                }
                //end Unload
            }
        }
    }
}