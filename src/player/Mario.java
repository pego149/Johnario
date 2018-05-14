package player;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import johnario.Audio;
import johnario.Obrazovka;

/**
 * Třída pre maria - playera.
 * @author pego1
 */
public class Mario implements KeyListener {
    private Obrazovka panel;   // reference na panel
    private ETypHraca typMaria;
    private ESmer smer;
    private Image marioObr;
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
    private EVelkost velkost;
    
    /**
     * Konstruktor pre maria
     * @param panel obrazovka
     */
    public Mario(Obrazovka panel) {
        this.velkost = EVelkost.MALY; // pociatocna velkost maly
        this.typMaria = ETypHraca.MALY_VPRAVO; // pociatocny typ
        this.smer = ESmer.VPRAVO; // pociatocny smer
        this.marioObr = this.typMaria.getObr(); // priradi obrazok z enumu
        this.panel = panel; // priradi panel
        this.x = 100; // pozicia x
        this.y = 250; // pozicia y
        this.dx = 0; // pociatocna horizontalna rychlost
        this.dy = 0; // pociatocna vertikalna rychlost
        this.maxDy = 8; // maximalna vertikalna rychlost (padania)
        this.gravity = 0.64; // gravitacne zrychlenie
        this.mozePadat = true; // ze moze padat
        this.mozeSkocit = true; // ze moze skocit
        this.tlacidla = new boolean[10]; // array pre tlacidla
        this.vAnimacii = false; // ze nie je v animacii - nehybe sa na zaciatku
        this.animacia = 0; // pocitadlo animacii
    }
    
    /**
     * Vykreslí maria a pripade pohybu spravi animaciu
     * @param g grafický kontext
     */
    public void vykresliSe(Graphics g) {
        
        g.drawImage(this.marioObr, this.x, this.y, null);
        if (this.vAnimacii) {
            this.animacia++;
            if (this.animacia == 18) {
                this.animacia = 0;
            }
            this.animacia();
        } else {
            this.animacia = 0;
            if (this.velkost == EVelkost.MALY) {
                if (this.smer == ESmer.VLAVO) {
                    this.typMaria = ETypHraca.MALY_VLAVO;
                } else {
                    this.typMaria = ETypHraca.MALY_VPRAVO;
                }
            } else {
                if (this.smer == ESmer.VLAVO) {
                    this.typMaria = ETypHraca.VELKY_VLAVO;
                } else {
                    this.typMaria = ETypHraca.VELKY_VPRAVO;
                }
            }
        }
    }

    /**
     * Vrati typ maria
     * @return this.typMaria
     */
    public ETypHraca getTypMaria() {
        return this.typMaria;
    }
    
    /**
     * pohne s mariom a postara sa o kolizie
     */
    public void urobPohyb() {
        if (this.panel.isHraSa()) {
            this.ovladanie();
        }
        this.x += this.dx;
        this.y += this.dy;
        if (this.x < 0) {
            this.x = 0;
        } else if (this.x > (this.panel.getWidth() - this.marioObr.getWidth(null) - 1) && (this.panel.getWidth() > 0)) {
            this.x = this.panel.getWidth() - this.marioObr.getWidth(null) - 1;
        }
        if (this.dx == 5 && this.panel.isZrazka(new Rectangle(this.x + 5, this.y, this.marioObr.getWidth(null) + 3, this.marioObr.getHeight(null) - 5))) {
            this.dx = 0;
            this.x -= 1;
        } else if (this.dx == -5 && this.panel.isZrazka(new Rectangle(this.x - 3, this.y, this.marioObr.getWidth(null) + 1, this.marioObr.getHeight(null) - 5))) {
            this.dx = 0;
            this.x += 1;
        }
        if (this.x > 300 && this.dx == 5) {
            this.posunObrazu(5);
        }
        this.mozePadat = !this.panel.isZrazka(new Rectangle(this.x + 2, this.y, this.marioObr.getWidth(null) - 2, this.marioObr.getHeight(null) + 1));
        if (this.mozePadat || this.smer == ESmer.KONIEC) {
            this.padanie(); 
            if (this.velkost == EVelkost.MALY) {
                if (this.smer == ESmer.VLAVO) {
                    this.typMaria = ETypHraca.MALY_SKOK_VLAVO;
                } else if (this.smer == ESmer.VPRAVO) {
                    this.typMaria = ETypHraca.MALY_SKOK_VPRAVO;
                } else if (this.smer == ESmer.KONIEC) {
                    this.typMaria = ETypHraca.MALY_KONIEC;
                }
            } else {
                if (this.smer == ESmer.VLAVO) {
                    this.typMaria = ETypHraca.VELKY_SKOK_VLAVO;
                } else if (this.smer == ESmer.VPRAVO) {
                    this.typMaria = ETypHraca.VELKY_SKOK_VPRAVO;
                } else if (this.smer == ESmer.KONIEC) {
                    this.typMaria = ETypHraca.MALY_KONIEC;
                }
            }
        } else {
            this.dy = 0;
            if (this.panel.isZrazka(new Rectangle(this.x, this.y, this.marioObr.getWidth(null), this.marioObr.getHeight(null)))) {
                this.y -= 1;
                if (this.panel.isZrazka(new Rectangle(this.x + 3, this.y - 3, this.marioObr.getWidth(null) - 3, this.marioObr.getHeight(null) - 10))) {
                    this.y += 2;
                }
                this.mozeSkocit = false;
            } else {
                this.mozeSkocit = true;
            }
            
        }
        this.marioObr = this.typMaria.getObr();
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
     * Skok pokial moze skocit
     * @param vyska vyska skoku
     */
    public void skok(int vyska) {
        if (this.velkost == EVelkost.MALY) {
            if (this.mozeSkocit) {
                Audio skok = new Audio("/res/smb_jump-small.wav");
                skok.play();
                this.dy -= vyska;
            } 
        } else {
            if (this.mozeSkocit) {
                Audio skok = new Audio("/res/smb_jump-super.wav");
                skok.play();
                this.dy -= vyska + 2;
            }
        }
    }

    /**
     * Getter na velkost maria
     * @return this. velkost
     */
    public EVelkost getVelkost() {
        return this.velkost;
    }
    
    /**
     * Vracia obrys obrázku
     * @return Rectangle podla velkosti this.marioObr
     */
    public Rectangle getOkraje() {
        Rectangle r = new Rectangle(this.x, this.y, this.marioObr.getWidth(null), this.marioObr.getHeight(null));
        return r;
    }
    
    /**
     * Co sa ma stat pri stlaceni klavesy
     * @param ke keyEvent
     */
    @Override
    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            this.tlacidla[0] = true;
        }
        
        if (key == KeyEvent.VK_RIGHT) {
            this.tlacidla[1] = true;
        }
        
        if (key == KeyEvent.VK_UP) {
            this.tlacidla[2] = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            if (this.panel.getVMenu()) {
                this.panel.start();
            }
        }
    }
    
    /**
     * Co sa ma stat pri pusteni klavesy
     * @param ke keyEvent
     */
    @Override
    public void keyReleased(KeyEvent ke) {
        int key = ke.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            this.tlacidla[0] = false;
            this.vAnimacii = false;
        }
        
        if (key == KeyEvent.VK_RIGHT) {
            this.tlacidla[1] = false;
            this.vAnimacii = false;
        }
        
        if (key == KeyEvent.VK_UP) {
            this.tlacidla[2] = false;
        }
    }
    
    /**
     * Metoda ktora ovlada pohyb maria podla uzivatelskeho vstupu
     */
    public void ovladanie() {
        if (this.tlacidla[0]) {
            if (!this.panel.isZrazka(new Rectangle(this.x - 2, this.y, this.marioObr.getWidth(null) - 2, this.marioObr.getHeight(null)))) {
                this.dx = -5;
                this.smer = ESmer.VLAVO;
                this.vAnimacii = true;
            } else {
                this.dx = 0;
            }  
        } else if (this.tlacidla[1]) {
            if (!this.panel.isZrazka(new Rectangle(this.x + 2, this.y, this.marioObr.getWidth(null) + 2, this.marioObr.getHeight(null)))) {
                this.dx = 5;
                this.smer = ESmer.VPRAVO; 
                this.vAnimacii = true;
            }  else {
                this.dx = 0;
            }
        } else {
            this.dx = 0;
        }
        if (this.tlacidla[2]) {
            this.skok(12);
            this.mozeSkocit = false;
            this.mozePadat = true;
        }
    }
    
    /**
     * Nepouzite
     * @param ke keyEvent
     */
    @Override
    public void keyTyped(KeyEvent ke) {
    } 

    /**
     * Definuje ci mario moze padat
     * @param b ci moze padat
     */
    public void setPada(boolean b) {
        this.mozePadat = b;
    }
    
    /**
     * Animuje beh maria
     */
    public void animacia() {
        if (this.velkost == EVelkost.MALY) {
            if (this.smer == ESmer.VPRAVO) {
                if (this.animacia == 0) {
                    this.typMaria = ETypHraca.MALY_BEZI1_VPRAVO;
                } else if (this.animacia == 6) {
                    this.typMaria = ETypHraca.MALY_BEZI2_VPRAVO;
                } else if (this.animacia == 12) {
                    this.typMaria = ETypHraca.MALY_BEZI3_VPRAVO;
                }
            } else {
                if (this.animacia == 0) {
                    this.typMaria = ETypHraca.MALY_BEZI1_VLAVO;
                } else if (this.animacia == 6) {
                    this.typMaria = ETypHraca.MALY_BEZI2_VLAVO;
                } else if (this.animacia == 12) {
                    this.typMaria = ETypHraca.MALY_BEZI3_VLAVO;
                }
            }
        } else {
            if (this.smer == ESmer.VPRAVO) {
                if (this.animacia == 0) {
                    this.typMaria = ETypHraca.VELKY_BEZI1_VPRAVO;
                } else if (this.animacia == 6) {
                    this.typMaria = ETypHraca.VELKY_BEZI2_VPRAVO;
                } else if (this.animacia == 12) {
                    this.typMaria = ETypHraca.VELKY_BEZI3_VPRAVO;
                }
            } else {
                if (this.animacia == 0) {
                    this.typMaria = ETypHraca.VELKY_BEZI1_VLAVO;
                } else if (this.animacia == 6) {
                    this.typMaria = ETypHraca.VELKY_BEZI2_VLAVO;
                } else if (this.animacia == 12) {
                    this.typMaria = ETypHraca.VELKY_BEZI3_VLAVO;
                }
            }
        }
        this.marioObr = this.typMaria.getObr();
    }
    
    /**
     * posledny skok
     */
    public void endSkok() {
        this.dy = -18;
        this.dx = 0;
        this.smer = ESmer.KONIEC;
    }
    
    /**
     * Posuva obraz
     * @param x o kolko sa ma posunit obraz
     */
    public void posunObrazu(int x) {
        this.x = this.x - 5;
        this.panel.posunObrazu(5);
    }
    
    /**
     * Zvacsi maria na VELKY
     */
    public void setMarioToBig() {
        this.velkost = EVelkost.VELKY;
        this.y = this.y - 50;
    }

    /**
     * Co sa ma stat ked sa mario zrazi s nepriatelom
     */
    public void stretSNepriatelom() {
        if (this.velkost == EVelkost.VELKY) {
            this.velkost = EVelkost.MALY;
            Audio maly = new Audio("/res/smb_pipe.wav");
            maly.play();
        } else {
            this.panel.koniecHry();
        }
    }
    
    /**
     * Nastav vertikalnu rychlost
     * @param i rychlost
     */
    public void setDy(int i) {
        this.mozeSkocit = true;
    }
}
