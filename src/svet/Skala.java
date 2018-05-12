package svet;

import johnario.Obrazovka;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 * Třída reprezentující překážku.
 * @author vita
 */
public class Skala implements IPrekazka{
    private Obrazovka panel;   // reference na panel
    private Image prekazkaObr;
    private int x;
    private int y;
    private boolean viditelny;  // překážka je na panelu true, mimo panel false
    
    /**
     * Konstruktor.
     * @param x x-ová souřadnice
     */
    public Skala(Obrazovka panel, int x, int y) {
        ImageIcon ii = new ImageIcon(this.getClass().getResource("/res/skala.png"));
        prekazkaObr = ii.getImage();
        this.panel = panel;             // reference na panel
        this.x = x;  // umístění překážky na ose x při vytvoření
        this.y = y;  // umístění překážky na ose y při vytvoření
        
        viditelny = true;
    }
    
    /**
     * Vykreslí obrázek na aktuální souřadnice
     * @param g grafický kontext
     */
    public void vykresliSe(Graphics g) {
        g.drawImage(prekazkaObr, x, y, null);
    }
    
    /**
     * Vrací obrys obrázku ve formě obdélníka.
     * @return Rectangle ve velikosti obrázku
     */
    public Rectangle getOkraje() {
        Rectangle r = new Rectangle(x, y, prekazkaObr.getWidth(panel), prekazkaObr.getHeight(panel));
        return r;
    }
   
    /**
     * Vrací zda je objekt visible či nikoli.
     * @return visible
     */
    public boolean isViditelny() {
        return viditelny;
    }  
    
    @Override
    public void setX(int i) {
        this.x = this.x + i;
    }
}
