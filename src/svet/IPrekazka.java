/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package svet;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author pego1
 */
public interface IPrekazka {
    void vykresliSe(Graphics g);
    Rectangle getOkraje();
    boolean isViditelny();
    void setX(int i);
}
