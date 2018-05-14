package svet;

import johnario.Obrazovka;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 * Trieda hrad
 * @author pego1
 */
public class Castle implements IPrekazka {
    private final Obrazovka panel;   // panel
    private final Image prekazkaObr; // obrazok prekazyk
    private int x; // x
    private final int y; // y
    private final boolean viditelny;  // ci je prekazka viditelnae
    
    /**
     * Konstruktor.
     * @param panel obrazovka
     * @param x x-ová suradnica
     * @param y y-ová suradnica
     */
    public Castle(Obrazovka panel, int x, int y) {
        ImageIcon ii = new ImageIcon(this.getClass().getResource("/res/end.png")); // nacita obrazok
        this.prekazkaObr = ii.getImage(); // priradi obrazok
        this.panel = panel;             // panel
        this.x = x;  // umiestni prekazku podla x
        this.y = y;  // umiestni prekazku podla y
        this.viditelny = true; // nastavi ze je viditelny
    }
    
    /**
     * Vykresli prekazku
     * @param g grafický kontext
     */
    public void vykresliSa(Graphics g) {
        g.drawImage(this.prekazkaObr, this.x, this.y, null);
    }
    
    /**
     * Vracia obrys obrázku
     * @return Rectangle podla velkosti this.prekazkaObr
     */
    public Rectangle getOkraje() {
        Rectangle r = new Rectangle(this.x, this.y, this.prekazkaObr.getWidth(this.panel), this.prekazkaObr.getHeight(this.panel));
        return r;
    }
    
    /**
     * vracia getX posunutu o -50
     * @return this.x - 50
     */
    public int getX() {
        return this.x - 50;
    }
   
    /**
     * Vracia ci je viditelny
     * @return this.viditelny
     */
    public boolean isViditelny() {
        return this.viditelny;
    }  
    
    /**
     * Prida k this.x i
     * @param i o kolko sa ma pridat
     */
    @Override
    public void setX(int i) {
        this.x = this.x + i;
    }
}
