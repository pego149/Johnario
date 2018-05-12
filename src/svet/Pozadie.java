/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package svet;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import johnario.Obrazovka;

/**
 *
 * @author pego1
 */
public class Pozadie {
    private Obrazovka panel;   // reference na panel
    private Image prekazkaObr;
    private int x;
    private int y;
    
    public Pozadie(Obrazovka panel) {
        ImageIcon ii = new ImageIcon(this.getClass().getResource("/res/bg.png"));
        prekazkaObr = ii.getImage();
        this.panel = panel;             // reference na panel
        this.x = 0;
        this.y = 0;
    }
    
    public void vykresliSe(Graphics g) {
        g.drawImage(prekazkaObr, x, y, null);
    }

    public void setX(int x) {
        this.x = this.x + x;
    }    
}
