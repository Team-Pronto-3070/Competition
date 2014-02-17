/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author pronto1
 */
public class Drive extends Thread {

    Jaguar jagleft1, jagright3, jagleft2, jagright4;
    Solenoid sol1, sol2;
    Joystick xBox;
    boolean running = false;
    double speed, turn, leftspeed, rightspeed;

    public Drive(Jaguar j1, Jaguar j2, Jaguar j3, Jaguar j4, Solenoid s1, Solenoid s2, Joystick x) {
        jagleft1 = j1;
        jagleft2 = j2;
        jagright3 = j3;
        jagright4 = j4;
        sol1 = s1;
        sol2 = s2;
        xBox = x;
    }

    public void setRun(boolean run) {
        running = run;
    }

    public void run() {
        while (true) {
            while (running) {
                speed = xBox.getRawAxis(2);
                turn = 0.5 * xBox.getRawAxis(4); //may need to adjust this
                if (turn < 0.1 && turn > -0.1) { //maybe need to adjust this
                    turn = 0;
                }
                if (xBox.getRawButton(1)) {
                    sol1.set(false);
                    sol2.set(true);
                }
                if (xBox.getRawButton(2)) {
                    sol1.set(true);
                    sol2.set(false);
                }
                if(leftspeed < speed - turn){
                    leftspeed = leftspeed + .2;
                }
                if(leftspeed > speed - turn){
                    leftspeed = leftspeed - .2;
                }
                if(Math.abs(speed - turn - leftspeed) < .1){
                    leftspeed = speed - turn;
                }
                if(rightspeed < speed + turn){
                    rightspeed = rightspeed + .2;
                }
                if(rightspeed > speed + turn){
                    rightspeed = rightspeed - .2;
                }
                if(Math.abs(speed + turn - leftspeed) < .1){
                    leftspeed = speed + turn;
                }
                jagleft1.set(leftspeed);
                jagleft2.set(leftspeed);
                jagright3.set(-(rightspeed));
                jagright4.set(-(rightspeed));
            }
        }
    }
}