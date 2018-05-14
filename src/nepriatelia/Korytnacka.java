package nepriatelia;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import johnario.Audio;
import johnario.Obrazovka;
import player.Mario;

/**
 * Trieda nepriatela Korytnacka.
 * @author pego1
 */
public class Korytnacka implements INepriatel {
    private final Image nepriatelObr1;
    private Image nepriatelObr;
    private int x;
    private int y;
    private int dx;
    private int dy;
    private final int maxDy;
    private final double gravity;
    private boolean mozePadat;
    private int mapx;
    private final Obrazovka panel;
    private final Image nepriatelObr2;
    private int zmena;
    private final Mario mario;
    private boolean zivy;
        
    /**
     * Konstruktor pre Korytnacku
     * @param panel Obrazovka
     * @param x pozicia x
     * @param y pozicia y
     */
    public Korytnacka(Obrazovka panel, int x, int y) {
        this.panel = panel; // obrazovka
        this.x = x; // pozicia x
        this.y = y; // pozicia y 
        this.nepriatelObr1 = new ImageIcon(this.getClass().getResource("/res/goomba1.png")).getImage(); // obrazok
        this.nepriatelObr2 = new ImageIcon(this.getClass().getResource("/res/goomba2.png")).getImage(); // obrazok
        this.nepriatelObr = this.nepriatelObr1; // nastavi obrazok na vykreslenie
        this.dx = 2; // default rychlost x
        this.dy = 0; // default rychlost y
        this.maxDy = 8; // max rychlost padania
        this.gravity = 1; // gravitacne zrychlenie
        this.mozePadat = true; // ci moze padat
        this.mapx = 0; // mapx pozicia
        this.mario = panel.getMario(); // mario
        this.zivy = true; // ci je zivy
    }
    
    /**
     * Vykresli korytnacku
     * @param g grafický kontext
     */
    @Override
    public void vykresliSa(Graphics g) {
        g.drawImage(this.nepriatelObr, this.x, this.y, null);
    }
    
    /**
     * Pohne s nepriatelom
     */
    @Override
    public void urobPohyb() {
        this.x += this.dx;
        this.y += this.dy;
        if (this.x < this.mapx) {
            this.dx = 2;
        }
        if (this.dx == 2 && this.panel.isZrazka(new Rectangle(this.x + 5, this.y, this.nepriatelObr.getWidth(null) + 3, this.nepriatelObr.getHeight(null) - 5))) {
            this.dx = -2;
            this.nepriatelObr = this.nepriatelObr1; 
        } else if (this.dx == -2 && this.panel.isZrazka(new Rectangle(this.x - 3, this.y, this.nepriatelObr.getWidth(null) + 1, this.nepriatelObr.getHeight(null) - 5))) {
            this.dx = 2;
            this.nepriatelObr = this.nepriatelObr2;
        }
        this.mozePadat = !this.panel.isZrazka(new Rectangle(this.x + 2, this.y, this.nepriatelObr.getWidth(null) - 2, this.nepriatelObr.getHeight(null)));
        if (this.mozePadat) {
            this.padanie(); 
        } else {
            this.y -= 1;
            this.dy = 0;            
        }
        this.kontrolaKolizieSMariom();
    }

    /**
     * Pada a akceleruje padanie po maxDy potom pada konstantne
     */
    public void padanie () {
        this.dy += this.gravity;
        if (this.dy >= this.maxDy) {
            this.dy = this.maxDy;
        }
    }

    /**
     * Getter na Y
     * @return this.y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Kontroluje ci nastala kolizia s Mariom a ako nastala
     */
    public void kontrolaKolizieSMariom() {
        if (this.mario.getOkraje().intersects(new Rectangle(this.x + 5, this.y, 28, this.nepriatelObr.getHeight(null) - 10))) {
            Audio kick = new Audio("/res/smb_kick.wav");
            kick.play();
            this.zivy = false;
            this.panel.pridajScore(100);
            this.mario.setDy(-5);
        }
        if (this.mario.getOkraje().intersects(new Rectangle(this.x - 5, this.y + 10, 40, this.nepriatelObr.getHeight(null)))) {
            this.mario.stretSNepriatelom();
            this.zivy = false;
        }
    }

    /**
     * Odpocita z this.y
     * @param y kolko ma odpocitat
     */
    public void minusY(int y) {
        this.y -= y;
    }

    /**
     * Setter X
     * @param i prida i k this.x a k this.mapx
     */
    public void setX(int i) {
        this.x = this.x + i;
        this.mapx = this.mapx + i;
    }

    /**
     * Ci je zivy
     * @return this.zivy
     */
    public boolean isZivy() {
        return this.zivy;
    }
}
