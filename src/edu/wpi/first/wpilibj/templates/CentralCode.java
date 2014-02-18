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
    DigitalInput digi14, digi3;
    AnalogChannel ultrasonic, encoder;
    Gyro gyro;
    double conf;
    boolean atShoot, afterShoot;
    int endTimer, noWait;
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

        digi14 = new DigitalInput(14);
        digi3 = new DigitalInput(3);

        encoder = new AnalogChannel(2);
        ultrasonic = new AnalogChannel(3);

        gyro = new Gyro(1);
        gyro.setSensitivity(0.07);
        gyro.reset();

        xBox = new Joystick(1);

        conf = 0;
        noWait = 0;
        endTimer = 0;
        noWait = 0;
        atShoot = false;
        afterShoot = false;

        drive = new Drive(jag1, jag2, jag3, jag4, sol1, sol2, xBox);
        loadAndShoot = new loadAndShoot(encoder, victor, sol4, sol5, sol7, sol8, xBox, digi14, digi3);
        drive.start();
        loadAndShoot.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousInit() {
        endTimer = 0;
        conf = 0;
        relay.set(Relay.Value.kOn);
        noWait = 0;
        sol1.set(true); //change it to fast setting
        sol2.set(false);
        sol4.set(false);
        sol5.set(true);
        sol7.set(true);
        sol8.set(false);
        atShoot = false;
        afterShoot = false;
    }

    public void autonomousPeriodic() {
        System.out.println("Confidence: " + conf);
        if (!atShoot) {
            if (ultrasonic.getVoltage() > 0.86) {
                conf = conf + SmartDashboard.getNumber("Confidence") - 70;
                jag1.set(-0.648);
                jag2.set(-0.648);
                jag3.set(0.6);
                jag4.set(0.6);
                System.out.println("Driving forward.");
            } else {
                jag1.set(0);
                jag2.set(0);
                jag3.set(0);
                jag4.set(0);
                atShoot = true;
                System.out.println("Done 420 blazin'.");
            }
        }
        if (atShoot && !afterShoot) {
            if (conf >= 40) {
                System.out.println("Saw Target.");
                sol7.set(false);
                sol8.set(true);
                afterShoot = true;
                System.out.println("Launching.");
            } else {
                if (noWait == 0) {
                    System.out.println("Did not see target.");
                }
                noWait++;
                /*if (ultrasonic.getVoltage() > 0.85) {
                    jag1.set(-0.216);
                    jag2.set(-0.216);
                    jag3.set(0.2);
                    jag4.set(0.2);
                }
                if (ultrasonic.getVoltage() < 0.81) {
                    jag1.set(0.216);
                    jag2.set(0.216);
                    jag3.set(-0.2);
                    jag4.set(-0.2);
                }*/
                if (noWait == 200) {
                    sol7.set(false);
                    sol8.set(true);
                    afterShoot = true;
                    System.out.println("Launching.");
                }
            }
        }
        if (afterShoot) {
            if (endTimer < 100) {
                endTimer++;
                jag1.set(0);
                jag2.set(0);
                jag3.set(0);
                jag4.set(0);
                if (endTimer == 99) {
                    System.out.println("Retracting Launcher.");
                }
            } else {
                relay.set(Relay.Value.kOff);
                sol7.set(true);
                sol8.set(false);
            }
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
        SmartDashboard.putNumber("Distance in.", 102.4 * ultrasonic.getAverageVoltage());
        //^need to do this as a boolean eventually
        if (digi3.get()) {
            SmartDashboard.putBoolean("ArmBack", true);
        }
        if (!digi3.get()) {
            SmartDashboard.putBoolean("ArmBack", false);
        }
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
