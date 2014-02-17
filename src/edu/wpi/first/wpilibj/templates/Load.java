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

    public void load() {
        if (encoder.getAverageVoltage() > 2.75) {
            victor.set(.6);
        } else {
            victor.set(0.1);
        }
    }
}
