/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package svet;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import johnario.Audio;
import johnario.Obrazovka;
import player.EVelkost;
import player.Mario;

/**
 *
 * @author pego1
 */
public class BonusBlok implements IPrekazka {

    private final Obrazovka panel;   // reference na panel
    private final Image bonusObr1;
    private final Image bonusObr2;
    private final Image bonusObr3;
    private final Image bonusObr;
    private Image obr;
    private int x;
    private int y;
    private final Mario mario;
    private boolean vAnimacii;
    private int animacia;
    private int zmena;
    private boolean active;
    private boolean viditelny;
    private final Audio coin;
    private final Image coinObr;
    private boolean coinActive;
    private boolean hribActive;
    private int cy;
    private final boolean isForCoin;
    private Hrib hrib;
    private boolean vysunutyHrib;
    private final Audio powerup;
    private boolean overovanie;
    private final Audio powerup2;

    /**
     * Konstruktor.
     *
     * @param x x-ová souřadnice
     */
    public BonusBlok(Obrazovka panel, int x, int y, boolean coin) {
        this.bonusObr1 = new ImageIcon(this.getClass().getResource("/res/bonus1.png")).getImage();
        this.bonusObr2 = new ImageIcon(this.getClass().getResource("/res/bonus2.png")).getImage();
        this.bonusObr3 = new ImageIcon(this.getClass().getResource("/res/bonus3.png")).getImage();
        this.bonusObr = new ImageIcon(this.getClass().getResource("/res/bonus.png")).getImage();
        this.coinObr = new ImageIcon(this.getClass().getResource("/res/coin.png")).getImage();
        this.coinActive = false;
        this.hribActive = false;
        this.coin = new Audio("/res/smb_coin.wav");
        this.powerup = new Audio("/res/smb_powerup_appears.wav");
        this.powerup2 = new Audio("/res/smb_powerup.wav");
        this.obr = this.bonusObr1;
        this.panel = panel;
        this.x = x;
        this.y = y;
        this.mario = panel.getMario();
        this.vAnimacii = false;
        this.animacia = 0;
        this.zmena = 0;
        this.active = true;
        this.viditelny = true;
        this.cy = y;
        this.isForCoin = coin;
        this.vysunutyHrib = false;
        this.overovanie = false;
    }

    /**
     * Vykreslí obrázek na aktuální souřadnice
     *
     * @param g grafický kontext
     */
    public void vykresliSe(Graphics g) {
        if (this.zmena == 0 && this.active == true) {
            obr = bonusObr1;
        } else if (this.zmena == 10 && this.active == true) {
            obr = bonusObr2;
        } else if (this.zmena == 20 && this.active == true) {
            obr = bonusObr3;
        } else if (this.zmena == 30 && this.active == true) {
            obr = bonusObr2;
        } else if (this.active == false) {
            obr = bonusObr;
        }
        if (zmena == 40) {
            this.zmena = -1;
        }
        g.drawImage(obr, x, y, null);
        if (zrazka()) {
            this.vAnimacii = true;
            if (this.active) {
                this.active = false;
                if (this.isForCoin) {
                    this.vysunMincu();
                } else {
                    this.vysunHrib();
                }
            }
            //this.viditelny = false;
            //this.x = -50;
            //this.y = 50;
        }
        if (this.coinActive) {
            g.drawImage(coinObr, x + 2, cy, null);
            cy = cy - 2;
            if (cy == (y - 50)) {
                this.coinActive = false;
            }
        }
        if (this.hribActive) {
            this.hrib.minusY(2);
            if (this.hrib.getY() < (y - 30)) {
                this.hribActive = false;
                this.overovanie = true;
            }
        }
        if (this.hrib != null) {
            this.hrib.provedPohyb();
            this.hrib.vykresliSe(g);
            if(this.hrib.overKoliziuSMariom() && this.overovanie) {
                powerup2.play();
                this.mario.setMarioToBig();
                this.hrib = null;
            }
        }
        if (this.vAnimacii) {
            this.animacia++;
            if (animacia == 20) {
                this.animacia = 0;
                this.vAnimacii = false;
            }
            animacia();
        }
        this.zmena++;
    }

    public boolean zrazka() {
        if (this.mario.getOkraje().intersects(new Rectangle(this.x + 5, this.y + 10, this.obr.getWidth(panel) - 5, this.obr.getHeight(panel)))) {
            return true;
        }
        return false;
    }

    /**
     * Vrací obrys obrázku ve formě obdélníka.
     *
     * @return Rectangle ve velikosti obrázku
     */
    public Rectangle getOkraje() {
        Rectangle r = new Rectangle(x, y, obr.getWidth(panel), obr.getHeight(panel));
        return r;
    }

    /**
     * Vrací zda je objekt visible či nikoli.
     *
     * @return visible
     */
    public boolean isViditelny() {
        return viditelny;
    }

    public void animacia() {
        //if(mario.getTypMaria() == ETypHraca.MALY_SKOK_VLAVO || mario.getTypMaria() == ETypHraca.MALY_SKOK_VPRAVO || mario.getTypMaria() == ETypHraca.MALY_VLAVO || mario.getTypMaria() == ETypHraca.MALY_VPRAVO) {
        if (animacia >= 0 && animacia < 4) {
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
        if(this.hrib != null) {
            this.hrib.setX(i);
        }
    }

    private void vysunMincu() {
        this.coin.play();
        this.coinActive = true;
        this.panel.pridajScore(10);
    }

    private void vysunHrib() {
        this.powerup.play();
        this.hribActive = true;
        this.hrib = new Hrib();
    }
    
    class Hrib {
        private Image hribObr;
        private int x;
        private int y;
        private int dx;
        private int dy;
        private final int maxDy;
        private final double gravity;
        private boolean mozePadat;
        private int mapx;
        
        public Hrib() {
            this.x = BonusBlok.this.x;
            this.y = BonusBlok.this.y;
            this.hribObr = new ImageIcon(this.getClass().getResource("/res/mushroom.png")).getImage();
            this.dx = 3;
            this.dy = 0;
            this.maxDy = 8;
            this.gravity = 1;
            this.mozePadat = true;
            this.mapx = 0;
        }
        
        public void vykresliSe(Graphics g) {
            g.drawImage(hribObr, x, y, null);
        }

        public void provedPohyb() {
            x += dx;
            y += dy;
            if (x < mapx) {
                dx = 3;
            }
            if (dx == 3 && panel.isSrazka(new Rectangle(x + 5, y, hribObr.getWidth(null) + 3, hribObr.getHeight(null) - 5))) {
                dx = -3;
            } else if (dx == -3 && panel.isSrazka(new Rectangle(x - 3, y, hribObr.getWidth(null) + 1, hribObr.getHeight(null) - 5))) {
                dx = 3;
            }
            mozePadat = !panel.isSrazka(new Rectangle(x + 2, y, hribObr.getWidth(null) - 2, hribObr.getHeight(null)));
            if (mozePadat) {
                this.padanie(); 
            } else {
                y -= 1;
                dy = 0;            
            }
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
        
        

        public void minusY(int y) {
            this.y -= y;
        }
        
        public void setX(int i) {
            this.x = this.x + i;
            this.mapx = this.mapx + i;
        }

        private boolean overKoliziuSMariom() {
            return mario.getOkraje().intersects(new Rectangle(x, y, hribObr.getWidth(null), hribObr.getHeight(null)));
        }
    }
}
