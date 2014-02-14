/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class CentralCode extends IterativeRobot {

    Jaguar jag1, jag2, jag3, jag4;
    Joystick xBox;
    Victor vic;
    Solenoid sol1, sol2, sol4, sol5, sol7, sol8;
    Relay relay;
    Drive drive;
    loadAndShoot load;
    
    AnalogChannel ultrasonic;
    double conf;
    boolean ready, goShoot;
    int i;
    NetworkTable server = NetworkTable.getTable("smartDashboard");

/*stevie yougosh darnbutt we need  network tables  or something for the camera 
    or god help us, we will make you suffer*/
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        jag1 = new Jaguar(1);
        jag2 = new Jaguar(3);
        jag3 = new Jaguar(3);
        jag4 = new Jaguar(4);

        sol1 = new Solenoid(1);
        sol2 = new Solenoid(2);
        
        sol4 = new Solenoid(4);
        sol5 = new Solenoid(5);
        
        sol7 = new Solenoid(7);
        sol8 = new Solenoid(8);
        
        relay = new Relay(1);

        xBox = new Joystick(1);
        
        conf = 0;
        i = 0;
        ready = false;
        goShoot = false;

        drive = new Drive(jag1, jag2, jag3, jag4, sol1, sol2, xBox);
        load = new loadAndShoot(sol4, sol5, sol7, sol8, vic, xBox);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        relay.set(Relay.Value.kOn);
        conf = conf + SmartDashboard.getNumber("Confidence") - 70;
        if (ultrasonic.getVoltage() <= .96){
            jag1.set(0);
            jag3.set(0);
            ready = true;
        } else {
            jag1.set(.5);
            jag3.set(.5);
        }
        if (i >= 100){
            goShoot = false;
            sol1.set(true);
            sol2.set(false);
            i = 0;
        }
        if (ready && conf >= 100){
            goShoot = true;
        }
        if (goShoot){
            sol1.set(false);
            sol2.set(true);
            i++;
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit() {
        if (!drive.running) {
            drive.start();
        }
        drive.setRun(true);
        
        /*
        if (!load.running){
            load.start();
        }
        load.setRun(true);
        */
    }
    public void teleopPeriodic() {
    }

    public void disabledInit() {
        drive.setRun(false);
        //load.setRun(false);
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
