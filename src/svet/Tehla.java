package svet;

import johnario.Obrazovka;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import johnario.Audio;
import player.EVelkost;
import player.Mario;

/**
 * Trieda tehla
 * @author pego1
 */
public class Tehla implements IPrekazka {
    private final Obrazovka panel;   // panel
    private final Image prekazkaObr; // obrazok prekazyk
    private int x; // x
    private int y; // y
    private boolean viditelny;  // ci je prekazka viditelna
    private final Mario mario; // mario
    private boolean vAnimacii; // ci je v animacii
    private int animacia; // pocitadlo pre animaciu
    
    /**
     * Konstruktor.
     * @param panel obrazovka
     * @param x x-ová suradnica
     * @param y y-ová suradnica
     */
    public Tehla(Obrazovka panel, int x, int y) {
        ImageIcon ii = new ImageIcon(this.getClass().getResource("/res/tehla.png"));// nacita obrazok
        this.prekazkaObr = ii.getImage(); // priradi obrazok
        this.panel = panel;             // panel
        this.x = x;  // umiestni prekazku podla x
        this.y = y;  // umiestni prekazku podla y
        this.viditelny = true; // nastavi ze je viditelny
        this.vAnimacii = false; // nastavi ze najskor nie je v animacii
        this.animacia = 0; // vynuluje pocitadlo
        this.mario = panel.getMario(); // nastavi maria
    }
    
    /**
     * Vykresli prekazku a spravi animaciu
     * @param g grafický kontext
     */
    public void vykresliSa(Graphics g) {
        if (this.viditelny) {
            g.drawImage(this.prekazkaObr, this.x, this.y, null);
        }
        if (this.zrazka()) {
            this.vAnimacii = true;
            if (this.mario.getVelkost() == EVelkost.VELKY) {
                this.viditelny = false;
                this.x = -50;
                this.y = 50;
                Audio block = new Audio("/res/smb_breakblock.wav");
                block.play();
            }
            
        }
        if (this.vAnimacii) {
            this.animacia++;
            if (this.animacia == 20) {
                this.animacia = 0;
                this.vAnimacii = false;
            }
            this.animacia();
        }
    }
    
    /**
     * Ci nastala zrazka
     * @return ak ano - true, ak nie - false
     */
    public boolean zrazka() {
        if (this.mario.getOkraje().intersects(new Rectangle(this.x + 5, this.y + 10, this.prekazkaObr.getWidth(this.panel) - 5, this.prekazkaObr.getHeight(this.panel)))) {
            return true;
        }
        return false;
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
     * Vracia ci je viditelny
     * @return this.viditelny
     */
    public boolean isViditelny() {
        return this.viditelny;
    }
    
    /**
     * Urobi animaciu ak je povolena animacia
     */
    public void animacia() {
       //if(mario.getTypMaria() == ETypHraca.MALY_SKOK_VLAVO || mario.getTypMaria() == ETypHraca.MALY_SKOK_VPRAVO || mario.getTypMaria() == ETypHraca.MALY_VLAVO || mario.getTypMaria() == ETypHraca.MALY_VPRAVO) {
        if (this.animacia >= 0 && this.animacia < 4) {
            this.y = this.y - 3;
        } else if (this.animacia >= 4 && this.animacia < 8) {
            this.y = this.y - 2;
        } else if (this.animacia >= 8 && this.animacia < 12) {
            this.y = this.y + 2;
        } else if (this.animacia >= 12 && this.animacia < 16) {
            this.y = this.y + 3;
        }
        //}
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
