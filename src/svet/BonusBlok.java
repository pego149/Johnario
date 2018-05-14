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
import player.Mario;

/**
 * Trieda pre BonusBlok
 * @author pego1
 */
public class BonusBlok implements IPrekazka {

    private final Obrazovka panel; // panel
    private final Image bonusObr1; // obr1
    private final Image bonusObr2; // obr2
    private final Image bonusObr3; // obr3
    private final Image bonusObr; // obr zhasnuty
    private Image obr; // vykreslovany obr
    private int x; // x
    private int y; // y
    private final Mario mario; // mario
    private boolean vAnimacii; // ci je v animacii
    private int animacia; // pocitadlo pre animaciu
    private int zmena; // pocitadlo pre zmeny
    private boolean active; // ci je aktivny
    private final boolean viditelny; // ci je viditelny
    private final Audio coin; // zvuk pre mincu
    private final Image coinObr; // obr pre mincu
    private boolean coinActive; // ci je minca aktivna
    private boolean hribActive; // ci je hrib aktivny
    private int cy; // pozicia mince y
    private final boolean isForCoin; // ci je bonus blok pre mincu
    private Hrib hrib; // hrib
    private boolean vysunutyHrib; // ci je hrib vysunuty
    private final Audio powerup; // zvuk pre powerup
    private boolean overovanie; // boolean pre overovanie
    private final Audio powerup2; // zvuk pre powerup2

    /**
     * Konstruktor.
     * @param panel obrazovka
     * @param x x-ová suradnica
     * @param y y-ová suradnica
     * @param coin ci je pre mincu
     */
    public BonusBlok(Obrazovka panel, int x, int y, boolean coin) {
        this.bonusObr1 = new ImageIcon(this.getClass().getResource("/res/bonus1.png")).getImage(); // nacita obr1
        this.bonusObr2 = new ImageIcon(this.getClass().getResource("/res/bonus2.png")).getImage(); // nacita obr2
        this.bonusObr3 = new ImageIcon(this.getClass().getResource("/res/bonus3.png")).getImage(); // nacita obr3
        this.bonusObr = new ImageIcon(this.getClass().getResource("/res/bonus.png")).getImage(); // nacita obr zhasnuty
        this.coinObr = new ImageIcon(this.getClass().getResource("/res/coin.png")).getImage(); // nacita mincu
        this.coinActive = false; // nastavi mincu na neaktivnu
        this.hribActive = false; // nastavi hrib na neaktivny
        this.coin = new Audio("/res/smb_coin.wav"); // nacita zvuk pre mincu
        this.powerup = new Audio("/res/smb_powerup_appears.wav"); // nacita zvuk pre hrib
        this.powerup2 = new Audio("/res/smb_powerup.wav"); // nacita zvuk pre zvacsenie
        this.obr = this.bonusObr1; // nastavi obr
        this.panel = panel; // priradi panel
        this.x = x; // x
        this.y = y; // y
        this.mario = panel.getMario(); // nastavi maria
        this.vAnimacii = false; // nastavi ze nie je animacia
        this.animacia = 0; // vynuluje pocitadlo
        this.zmena = 0; // vynuluje pocitadlo
        this.active = true; // nastavi ze je aktivny
        this.viditelny = true; // nastavi ze je viditelny
        this.cy = y; // y mince = y
        this.isForCoin = coin; // nastavi podla coin ci je pre mincu
        this.vysunutyHrib = false; // nie je vysunuty hrib
        this.overovanie = false; // nie je overovanie
    }

    /**
     * Vykresli prekazku a urobi animaciu
     * @param g grafický kontext
     */
    public void vykresliSa(Graphics g) {
        if (this.zmena == 0 && this.active) {
            this.obr = this.bonusObr1;
        } else if (this.zmena == 10 && this.active) {
            this.obr = this.bonusObr2;
        } else if (this.zmena == 20 && this.active) {
            this.obr = this.bonusObr3;
        } else if (this.zmena == 30 && this.active) {
            this.obr = this.bonusObr2;
        } else if (!this.active) {
            this.obr = this.bonusObr;
        }
        if (this.zmena == 40) {
            this.zmena = -1;
        }
        g.drawImage(this.obr, this.x, this.y, null);
        if (this.zrazka()) {
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
            g.drawImage(this.coinObr, this.x + 2, this.cy, null);
            this.cy = this.cy - 2;
            if (this.cy == (this.y - 50)) {
                this.coinActive = false;
            }
        }
        if (this.hribActive) {
            this.hrib.minusY(2);
            if (this.hrib.getY() < (this.y - 30)) {
                this.hribActive = false;
                this.overovanie = true;
            }
        }
        if (this.hrib != null) {
            this.hrib.urobPohyb();
            this.hrib.vykresliSa(g);
            if (this.hrib.overKoliziuSMariom() && this.overovanie) {
                this.powerup2.play();
                this.mario.setMarioToBig();
                this.hrib = null;
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
        this.zmena++;
    }

    /**
     * Ci nastala zrazka
     * @return ak ano - true, ak nie - false
     */
    public boolean zrazka() {
        if (this.mario.getOkraje().intersects(new Rectangle(this.x + 5, this.y + 10, this.obr.getWidth(this.panel) - 5, this.obr.getHeight(this.panel)))) {
            return true;
        }
        return false;
    }

    /**
     * Vracia obrys obrázku
     * @return Rectangle podla velkosti this.prekazkaObr
     */
    @Override
    public Rectangle getOkraje() {
        Rectangle r = new Rectangle(this.x, this.y, this.obr.getWidth(this.panel), this.obr.getHeight(this.panel));
        return r;
    }

    /**
     * Vracia ci je viditelny
     * @return this.viditelny
     */
    @Override
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
        if (this.hrib != null) {
            this.hrib.setX(i);
        }
    }
    
    /**
     * vysunie mincu
     */
    private void vysunMincu() {
        this.coin.play();
        this.coinActive = true;
        this.panel.pridajScore(10);
    }

    /**
     * Vytvori a vysunie hrib
     */
    private void vysunHrib() {
        this.powerup.play();
        this.hribActive = true;
        this.hrib = new Hrib();
    }
    
    /**
     * Vnorena trieda hrib
     */
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
        
        /**
         * Konstruktor
         */
        private Hrib() {
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
        
        /**
        * Vykresli hrib
        * @param g grafický kontext
        */
        public void vykresliSa(Graphics g) {
            g.drawImage(this.hribObr, this.x, this.y, null);
        }

        /**
        * Pohne s hribom
        */
        public void urobPohyb() {
            this.x += this.dx;
            this.y += this.dy;
            if (this.x < this.mapx) {
                this.dx = 3;
            }
            if (this.dx == 3 && BonusBlok.this.panel.isZrazka(new Rectangle(this.x + 5, this.y, this.hribObr.getWidth(null) + 3, this.hribObr.getHeight(null) - 5))) {
                this.dx = -3;
            } else if (this.dx == -3 && BonusBlok.this.panel.isZrazka(new Rectangle(this.x - 3, this.y, this.hribObr.getWidth(null) + 1, this.hribObr.getHeight(null) - 5))) {
                this.dx = 3;
            }
            this.mozePadat = !BonusBlok.this.panel.isZrazka(new Rectangle(this.x + 2, this.y, this.hribObr.getWidth(null) - 2, this.hribObr.getHeight(null)));
            if (this.mozePadat) {
                this.padanie(); 
            } else {
                this.y -= 1;
                this.dy = 0;            
            }
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
        * Odpocita od this.y y
        * @param y o kolko sa ma odpocitat
        */
        public void minusY(int y) {
            this.y -= y;
        }
        
        /**
        * Prida k this.x i
        * @param i o kolko sa ma pridat
        */
        public void setX(int i) {
            this.x = this.x + i;
            this.mapx = this.mapx + i;
        }
        
        /**
         * Ci nastala zrazka
         * @return ak ano - true, ak nie - false
         */
        private boolean overKoliziuSMariom() {
            return BonusBlok.this.mario.getOkraje().intersects(new Rectangle(this.x, this.y, this.hribObr.getWidth(null), this.hribObr.getHeight(null)));
        }
    }
}
