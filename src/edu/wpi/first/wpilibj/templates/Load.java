/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author pronto1
 */
public class Load extends Thread {

    Victor victor;

    public Load(Victor v) {
        victor = v;
    }

    public void loadWithBall() {
    }

    public void loadWithoutBall() {
        victor.set(0.2);
    }
}
