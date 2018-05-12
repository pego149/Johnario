package svet;

import johnario.Obrazovka;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import johnario.Audio;
import player.ETypHraca;
import player.EVelkost;
import player.Mario;

/**
 * Třída reprezentující překážku.
 * @author vita
 */
public class Tehla implements IPrekazka{
    private Obrazovka panel;   // reference na panel
    private Image prekazkaObr;
    private int x;
    private int y;
    private boolean viditelny;
    private Mario mario;
    private boolean vAnimacii;
    private int animacia;
    
    /**
     * Konstruktor.
     * @param x x-ová souřadnice
     */
    public Tehla(Obrazovka panel, int x, int y) {
        ImageIcon ii = new ImageIcon(this.getClass().getResource("/res/tehla.png"));
        prekazkaObr = ii.getImage();
        this.panel = panel;
        this.x = x;
        this.y = y;
        this.mario = panel.getMario();
        this.viditelny = true;
        this.vAnimacii = false;
        this.animacia = 0;
    }
    
    /**
     * Vykreslí obrázek na aktuální souřadnice
     * @param g grafický kontext
     */
    public void vykresliSe(Graphics g) {
        if(this.viditelny) {
            g.drawImage(prekazkaObr, x, y, null);
        }
        if(zrazka()) {
            this.vAnimacii = true;
            if(this.mario.getVelkost() == EVelkost.VELKY) {
                this.viditelny = false;
                this.x = -50;
                this.y = 50;
                Audio block = new Audio("/res/smb_breakblock.wav");
                block.play();
            }
            
        }
        if(vAnimacii) {
            animacia++;
            if (animacia == 20) {
                animacia = 0;
                this.vAnimacii = false;
            }
            animacia();
        }
    }
    
    public boolean zrazka() {
        if (mario.getOkraje().intersects(new Rectangle(x + 5,y + 10,prekazkaObr.getWidth(panel) - 5,prekazkaObr.getHeight(panel)))) {
            return true;
        }
        return false;
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
    
    public void animacia() {
       //if(mario.getTypMaria() == ETypHraca.MALY_SKOK_VLAVO || mario.getTypMaria() == ETypHraca.MALY_SKOK_VPRAVO || mario.getTypMaria() == ETypHraca.MALY_VLAVO || mario.getTypMaria() == ETypHraca.MALY_VPRAVO) {
            if(animacia >= 0 && animacia < 4) {
                this.y = y - 3;
            } else if (animacia >= 4 && animacia < 8) {
                this.y = y - 2;
            } else if (animacia >= 8 && animacia < 12) {
                this.y = y + 2;
            } else if (animacia >= 12 && animacia < 16) {
                this.y = y + 3;
            }
        //}
    }

    @Override
    public void setX(int i) {
        this.x = this.x + i;
    }
}
