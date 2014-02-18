/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author pronto1
 */
public class Load extends Thread {

    Victor victor;
    AnalogChannel encoder;

    public Load(Victor v, AnalogChannel e) {
        victor = v;
        encoder = e;
    }

    public void loadWithBall() {
        if (encoder.getVoltage() > 3 || encoder.getVoltage() < 0.5) {
            victor.set(0.6);
        } else {
            victor.set(0.1);
        }
    }
    public void loadWithoutBall(){
        if (encoder.getVoltage() > 3 || encoder.getVoltage() < 0.5) {
            victor.set(.4);
        } else {
            victor.set(0.2);
        }
    }
}
