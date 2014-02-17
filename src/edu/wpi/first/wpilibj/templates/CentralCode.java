///*----------------------------------------------------------------------------*/
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
 * directory.-
 */
public class CentralCode extends IterativeRobot {

    Jaguar jag1, jag2, jag3, jag4;
    Joystick xBox;
    Victor victor;
    Solenoid sol1, sol2, sol4, sol5, sol7, sol8;
    Relay relay;
    DigitalInput digi2, digi3;
    AnalogChannel ultrasonic, encoder;
    Gyro gyro;
    double conf;
    boolean ready, goShoot;
    int i, noWait;
    NetworkTable server = NetworkTable.getTable("smartDashboard");
    Drive drive;
    loadAndShoot loadAndShoot;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        jag1 = new Jaguar(1);
        jag2 = new Jaguar(2);
        jag3 = new Jaguar(3);
        jag4 = new Jaguar(4);
        victor = new Victor(5);

        sol1 = new Solenoid(1);
        sol2 = new Solenoid(2);

        sol4 = new Solenoid(4);
        sol5 = new Solenoid(5);

        sol7 = new Solenoid(7);
        sol8 = new Solenoid(8);

        relay = new Relay(1);

        digi2 = new DigitalInput(14);
        digi3 = new DigitalInput(3);

        encoder = new AnalogChannel(2);
        ultrasonic = new AnalogChannel(3);

        gyro = new Gyro(1);
        gyro.setSensitivity(0.07);
        gyro.reset();

        xBox = new Joystick(1);

        conf = 0;
        noWait = 0;
        i = 0;
        noWait = 0;
        ready = false;
        goShoot = false;

        drive = new Drive(jag1, jag2, jag3, jag4, sol1, sol2, xBox);
        loadAndShoot = new loadAndShoot(encoder, victor, sol4, sol5, sol7, sol8, xBox, digi2, digi3);
        drive.start();
        loadAndShoot.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousInit() {
        conf = 0;
        relay.set(Relay.Value.kOn);
        noWait = 0;
<<<<<<< HEAD
=======
        sol1.set(true); //change it to fast setting
        sol2.set(false);
        sol4.set(true);
        sol5.set(false);
        sol7.set(true);
        sol8.set(false);
>>>>>>> a927a343140ea489bb9f2b2b59942b391938cda3
    }

    public void autonomousPeriodic() {
        if (!ready){
        conf = conf + SmartDashboard.getNumber("Confidence") - 70;
<<<<<<< HEAD
        }
=======
>>>>>>> a927a343140ea489bb9f2b2b59942b391938cda3
        if (ultrasonic.getVoltage() <= .96) {
            jag1.set(0);
            jag3.set(0);
            ready = true;
        } else {
            jag1.set(1);
            jag3.set(-1);
        }
        if (i >= 100) {
            goShoot = false;
<<<<<<< HEAD
            sol7.set(false);
            sol8.set(true);
            i = 0;
        }
        if (ready && conf >= 40) {
            goShoot = true;
        }
        if (ready && conf < 40) {
            noWait++;
            if (noWait >= 40 && noWait < 70) {
                jag1.set(-1);
                jag3.set(1);
            }
            if (noWait >= 70 && noWait < 110) {
                jag1.set(0);
                jag3.set(0);
            }
            if (noWait >= 110 && noWait < 150) {
                jag1.set(1);
                jag3.set(-1);
            }
            if (noWait == 150) {
                goShoot = true;
            }
        }
        if (goShoot) {
            sol7.set(true);
            sol8.set(false);
=======
            sol7.set(true);
            sol8.set(false);
            i = 0;
        }
        if (ready && conf >= 40) {
            goShoot = true;
        }
        if (ready && conf < 40) {
            noWait++;
        }
        if (ready && conf < 40 && noWait == 150) {
            goShoot = true;
        }
        if (goShoot && i < 3) {
            i++;
            sol4.set(false);
            sol5.set(true);
        }
        if (goShoot && i >= 3) {
            sol7.set(false);
            sol8.set(true);
>>>>>>> a927a343140ea489bb9f2b2b59942b391938cda3
            i++;
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit() {
        relay.set(Relay.Value.kOff);
        drive.setRun(true);
        loadAndShoot.setRun(true);
    }

    public void teleopPeriodic() {
<<<<<<< HEAD
        SmartDashboard.putNumber("Distance in.", 102.4*ultrasonic.getAverageVoltage());
                //^need to do this as a boolean eventually
=======
        if (digi3.get()) {
            SmartDashboard.putBoolean("ArmBack", true);
        }
        if (!digi3.get()) {
            SmartDashboard.putBoolean("ArmBack", false);
        }
        SmartDashboard.putNumber("Distance in.", 102.4 * ultrasonic.getVoltage());
        //^need to do this as a boolean eventually
>>>>>>> a927a343140ea489bb9f2b2b59942b391938cda3
    }

    public void disabledInit() {
        drive.setRun(false);
        
        loadAndShoot.setRun(false);
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
