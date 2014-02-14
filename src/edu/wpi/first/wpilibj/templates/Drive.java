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

    boolean running = false;
    boolean gearchange = false;
    Jaguar jagleft1, jagright2, jagleft3, jagright4;
    Solenoid sol1, sol2;
    Joystick xBox;

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
                if (gearchange) {
                    jagleft1.set(xBox.getRawAxis(2));
                    jagright2.set(-xBox.getRawAxis(5));
                }
                if (!gearchange) {
                    jagleft3.set(xBox.getRawAxis(2));
                    jagright4.set(-xBox.getRawAxis(5));
                }
                if (xBox.getRawButton(1)) {
                    sol1.set(false);
                    sol2.set(true);
                    gearchange = true;
                }
                if (xBox.getRawButton(2)) {
                    sol1.set(true);
                    sol2.set(false);
                    gearchange = false;
                }
            }
        }
    }
}