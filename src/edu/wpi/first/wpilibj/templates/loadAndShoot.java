/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

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
    Victor vic;
    Joystick xBox;
    
    Shoot Shoot;
    boolean shooting = false;
    int shootTimer = 0;
    
    Load Load;
    boolean loading = false;
    
    Unload Unload;
    boolean unloading = false;

    public loadAndShoot(Solenoid s4, Solenoid s5, Solenoid s7, Solenoid s8, Victor v, Joystick x) {
        sol4 = s4;
        sol5 = s5;
        sol7 = s7;
        sol8 = s8;
        vic = v;
        xBox = x;

        shootTimer = 0;

        Unload = new Unload(vic, sol4, sol5);
        Shoot = new Shoot(vic, sol4, sol5, sol7, sol8, Unload);
        Load = new Load(vic, sol4, sol5);
    }

    public void setRun(boolean run) {
        running = run;
    }

    public void run() {
        while (true) {
            shootTimer = 0;
            shooting = false;
            loading = false;
            unloading = false;
            while (running) {
                if(xBox.getRawButton(5)){
                    loading = true;
                }
                
                //shooter
                if (xBox.getRawAxis(3) < -.8 && !loading && ! unloading) {
                    shooting = true;
                }
                if (shooting && shootTimer <= 0/*insert right number*/) {
                    shootTimer++;
                    Shoot.shoot();
                }
                if(shootTimer>0/*insert same number*/){
                    shootTimer = 0;
                    shooting = false;
                }
            }
        }
    }
}
