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
 * Trieda pre pozadie
 * @author pego1
 */
public class Pozadie {
    private Obrazovka panel;   // obrazovka
    private Image pozadieObr; // obrazok pre pozadie
    private int x; // x
    private int y; // y
    
    /**
     * Konstruktor
     * @param panel Obrazovka
     */
    public Pozadie(Obrazovka panel) {
        ImageIcon ii = new ImageIcon(this.getClass().getResource("/res/bg.png")); // nacita obrazok
        this.pozadieObr = ii.getImage(); // priradi obrazok
        this.panel = panel;             // panel
        this.x = x;  // umiestni prekazku podla x
        this.y = y;  // umiestni prekazku podla y
    }
    
    /**
     * Vykresli prekazku
     * @param g grafický kontext
     */
    public void vykresliSa(Graphics g) {
        g.drawImage(this.pozadieObr, this.x, this.y, null);
        g.drawImage(this.pozadieObr, this.x + this.pozadieObr.getWidth(null), this.y, null);
        g.drawImage(this.pozadieObr, this.x + this.pozadieObr.getWidth(null) * 2, this.y, null);
    }

    /**
     * Prida k this.x i
     * @param x o kolko sa ma pridat
     */
    public void setX(int x) {
        this.x = this.x + x;
    }    
}
