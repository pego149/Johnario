/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nepriatelia;

import java.awt.Graphics;

/**
 * Interface pre nepriatelov
 * @author pego1
 */
public interface INepriatel {
    void urobPohyb();
    void vykresliSa(Graphics g);
    boolean isZivy();
    void setX(int i);
}
