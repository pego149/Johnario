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
    private Image goombaObr1;
    private Image goombaObr;
    private int x;
    private int y;
    private int dx;
    private int dy;
    private final int maxDy;
    private final double gravity;
    private boolean mozePadat;
    private int mapx;
    private final Obrazovka panel;
    private final Image goombaObr2;
    private int zmena;
    private final Mario mario;
    private boolean zivy;
        
    public Goomba(Obrazovka panel, int x, int y) {
        this.panel = panel;
        this.x = x;
        this.y = y;
        this.goombaObr1 = new ImageIcon(this.getClass().getResource("/res/goomba1.png")).getImage();
        this.goombaObr2 = new ImageIcon(this.getClass().getResource("/res/goomba2.png")).getImage();
        this.goombaObr = goombaObr1;
        this.dx = 2;
        this.dy = 0;
        this.maxDy = 8;
        this.gravity = 1;
        this.mozePadat = true;
        this.mapx = 0;
        this.mario = panel.getMario();
        this.zivy = true;
    }

    public void vykresliSe(Graphics g) {
        if (this.zmena == 0) {
            goombaObr = goombaObr1;
        } else if (this.zmena == 20) {
            goombaObr = goombaObr2;
        }
        if (zmena == 40) {
            this.zmena = -1;
        }
        this.zmena++;
        g.drawImage(goombaObr, x, y, null);
    }

    public void provedPohyb() {
        x += dx;
        y += dy;
        if (x < mapx) {
            dx = 2;
        }
        if (dx == 2 && panel.isSrazka(new Rectangle(x + 5, y, goombaObr.getWidth(null) + 3, goombaObr.getHeight(null) - 5))) {
            dx = -2;
        } else if (dx == -2 && panel.isSrazka(new Rectangle(x - 3, y, goombaObr.getWidth(null) + 1, goombaObr.getHeight(null) - 5))) {
            dx = 2;
        }
        mozePadat = !panel.isSrazka(new Rectangle(x + 2, y, goombaObr.getWidth(null) - 2, goombaObr.getHeight(null)));
        if (mozePadat) {
            this.padanie(); 
        } else {
            y -= 1;
            dy = 0;            
        }
        this.kontrolaKolizieSMariom();
    }

    public void padanie () {
        this.dy += this.gravity;
        if (dy >= maxDy) {
            dy = maxDy;
        }
    }

    public int getY() {
        return y;
    }

    public void kontrolaKolizieSMariom() {
        if(mario.getOkraje().intersects(new Rectangle(x + 2, y, goombaObr.getWidth(null) - 2, goombaObr.getHeight(null) - 10))) {
            Audio kick = new Audio("/res/smb_kick.wav");
            kick.play();
            this.zivy = false;
            this.panel.pridajScore(100);
        }
    }

    public void minusY(int y) {
        this.y -= y;
    }

    public void setX(int i) {
        this.x = this.x + i;
        this.mapx = this.mapx + i;
    }

    public boolean isZivy() {
        return zivy;
    }
    
    
}
