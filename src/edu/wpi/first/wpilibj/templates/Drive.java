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

    Jaguar jagleft1, jagright2, jagleft3, jagright4;
    Solenoid sol1, sol2;
    Joystick xBox;
    
    boolean running = false;
    boolean fast = false;
    double speed, turn;

    public Drive(Jaguar j1, Jaguar j2, Jaguar j3, Jaguar j4, Solenoid s1, Solenoid s2, Joystick x) {
        jagleft1 = j1;
        jagright2 = j2;
        jagleft3 = j3;
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
                    turn = 0.5 * xBox.getRawAxis(4);
                    if (turn < 0.1 && turn > -0.1) { //maybe need to adjust this
                        turn = 0;
                    }
                if (fast) {
<<<<<<< HEAD
                   jagleft1.set(speed - turn); //may need to adjust drive based on weight
                    jagright2.set(speed + turn);
=======
                    jagleft1.set(speed - turn); //may need to adjust drive based on weight
                    jagright2.set(-(speed + turn));
>>>>>>> 1d1280d47974992dd4cc6868baa12c001d62cab0
                }
                if (!fast) {
                    jagleft3.set(speed - turn);
                    jagright4.set(-(speed + turn));
                }
                if (xBox.getRawButton(1)) {
                    sol1.set(false);
                    sol2.set(true);
                    fast = false; // check which one is fast, which one is slow
                }
                if (xBox.getRawButton(2)) {
                    sol1.set(true);
                    sol2.set(false);
                    fast = true;
                }
            }
        }
    }
}