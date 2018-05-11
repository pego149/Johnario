package nepriatelia;

import player.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import johnario.Audio;
import johnario.Obrazovka;

/**
 * Třída reprezentující auto.
 * @author vita
 */
public class Goomba {
    private Obrazovka panel;
    private ESmer smer;
    private Image gObr;
    private int x;      // x-ová souřadnice auta
    private int y;      // y-ová souřadnice auta
    private double dx;     // směr auta po ose x (+ doprava, - doleva)
    private double dy;     // směr auta po ose y (+ dole, - hore)
    private int  maxDy;
    private double gravity;
    private boolean mozePadat;
    private boolean mozeSkocit;
    private boolean[] tlacidla;
    private boolean vAnimacii;
    private int animacia;
    private Image g1;
    private Image g2;
    

    
    /**
     * Kontruktor třídy Auto
     * @param sirkaPanelu - šířka herní plochy
     */
    public Goomba(Obrazovka panel) {
        this.smer = ESmer.VPRAVO;
        this.g1 = new ImageIcon(Goomba.class.getResource("goomba1.png")).getImage();
        this.g2 = new ImageIcon(Goomba.class.getResource("goomba2.png")).getImage();
        this.panel = panel;
        this.x = 100;
        this.y = 300;
        this.dx = 0;
        this.dy = 0;
        this.maxDy = 8;
        this.gravity = 0.64;
        this.mozePadat = true;
        this.animacia = 0;
    }
    
    /**
     * Vykreslí obrázek na aktuální souřadnice
     * @param g grafický kontext
     */
    public void vykresliSe(Graphics g) {
        
        g.drawImage(gObr, x, y, null);
    }
    
    /**
     * Změna x-ové souřadnice v daném směru.
     */
    public void provedPohyb() {
        x += dx;
        y += dy;
        if (x < 0) {
            x = 0;
        } else if (x > (panel.getWidth() - gObr.getWidth(null) - 1) && (panel.getWidth() >0)) {
            x = panel.getWidth() - gObr.getWidth(null) - 1;
        }
        if (dx == 5 && panel.isSrazka(new Rectangle(x + 5, y, gObr.getWidth(null) + 3, gObr.getHeight(null) - 5))) {
            dx = 0;
            x -= 1;
        } else if (dx == -5 && panel.isSrazka(new Rectangle(x - 3, y, gObr.getWidth(null) + 1, gObr.getHeight(null) - 5))) {
            dx = 0;
            x += 1;
        }
        mozePadat = !panel.isSrazka(new Rectangle(x + 2, y, gObr.getWidth(null) - 2, gObr.getHeight(null) + 1));
        if (mozePadat) {
            padanie(); 
        } else {
            dy = 0;            
        }
    }
    
    public void padanie () {
        dy += gravity;
        if (dy >= maxDy) {
            dy = maxDy;
        }
    }
    
    public Rectangle getOkraje() {
        Rectangle r = new Rectangle(x, y, gObr.getWidth(null), gObr.getHeight(null));
        return r;
    }
}
