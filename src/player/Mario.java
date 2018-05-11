package player;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import johnario.Audio;
import johnario.Obrazovka;

/**
 * Třída reprezentující auto.
 * @author vita
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
    

    
    /**
     * Kontruktor třídy Auto
     * @param sirkaPanelu - šířka herní plochy
     */
    public Mario(Obrazovka panel) {
        
        this.typMaria = ETypHraca.MALY_VPRAVO;
        this.smer = ESmer.VPRAVO;
        this.marioObr = this.typMaria.getObr();
        this.panel = panel;
        this.x = 100;
        this.y = 250;
        this.dx = 0;
        this.dy = 0;
        this.maxDy = 8;
        this.gravity = 0.64;
        this.mozePadat = true;
        this.mozeSkocit = true;
        this.tlacidla = new boolean[10];
        this.vAnimacii = false;
        this.animacia = 0;
    }
    
    /**
     * Vykreslí obrázek na aktuální souřadnice
     * @param g grafický kontext
     */
    public void vykresliSe(Graphics g) {
        
        g.drawImage(marioObr, x, y, null);
        if(vAnimacii) {
            animacia++;
            if (animacia == 18) {
                animacia = 0;
            }
            animacia();
        } else {
            animacia = 0;
            if (smer == ESmer.VLAVO) {
                this.typMaria = ETypHraca.MALY_VLAVO;
            } else {
                this.typMaria = ETypHraca.MALY_VPRAVO;
            }
        }
    }

    public ETypHraca getTypMaria() {
        return typMaria;
    }
    
    /**
     * Změna x-ové souřadnice v daném směru.
     */
    public void provedPohyb() {
        if(panel.isHraSa()) {
            ovladanie();
        }
        x += dx;
        y += dy;
        if (x < 0) {
            x = 0;
        } else if (x > (panel.getWidth() - marioObr.getWidth(null) - 1) && (panel.getWidth() >0)) {
            x = panel.getWidth() - marioObr.getWidth(null) - 1;
        }
        if (dx == 5 && panel.isSrazka(new Rectangle(x + 5, y, marioObr.getWidth(null) + 3, marioObr.getHeight(null) - 5))) {
            dx = 0;
            x -= 1;
        } else if (dx == -5 && panel.isSrazka(new Rectangle(x - 3, y, marioObr.getWidth(null) + 1, marioObr.getHeight(null) - 5))) {
            dx = 0;
            x += 1;
        }
        if (this.x > 300 && dx == 5) {
            this.posunObrazu(5);
        }
        mozePadat = !panel.isSrazka(new Rectangle(x + 2, y, marioObr.getWidth(null) - 2, marioObr.getHeight(null) + 1));
        if (mozePadat) {
            padanie(); 
            if (smer == ESmer.VLAVO) {
                this.typMaria = ETypHraca.MALY_SKOK_VLAVO;
            } else if (smer == ESmer.VPRAVO) {
                this.typMaria = ETypHraca.MALY_SKOK_VPRAVO;
            } else if (smer == ESmer.KONIEC) {
                this.typMaria = ETypHraca.MALY_KONIEC;
            }
        } else {
            dy = 0;
            if (panel.isSrazka(new Rectangle(x, y, marioObr.getWidth(null), marioObr.getHeight(null)))) {
                y -= 1;
                if (panel.isSrazka(new Rectangle(x + 3, y - 3, marioObr.getWidth(null) - 3, marioObr.getHeight(null) - 10))) {
                    y += 2;
                }
                mozeSkocit = false;
            } else {
                mozeSkocit = true;
            }
            
        }
        this.marioObr = this.typMaria.getObr();
    }
    
    public void padanie () {
        dy += gravity;
        if (dy >= maxDy) {
            dy = maxDy;
        }
    }
    
    public void skok(int vyska) {
        if (mozeSkocit) {
            Audio skok = new Audio("/res/smb_jump-small.wav");
            skok.play();
            dy -= vyska;
            this.typMaria = ETypHraca.MALY_SKOK_VLAVO;
            this.marioObr = this.typMaria.getObr();
        }
    }
    
    /**
     * Vrací obrys obrázku ve formě obdélníka.
     * @return Rectangle ve velikosti obrázku
     */
    public Rectangle getOkraje() {
        Rectangle r = new Rectangle(x, y, marioObr.getWidth(null), marioObr.getHeight(null));
        return r;
    }
    
    /**
     * Definuje činnost, která se provede po stisku klávesy na klávesnici.
     * @param e - KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            tlacidla[0] = true;
        }
        
        if (key == KeyEvent.VK_RIGHT) {
            tlacidla[1] = true;
        }
        
        if (key == KeyEvent.VK_UP) {
            tlacidla[2] = true;
        }
    }
    
    /**
     * Definuje činnost, která se provede po puštění stisknuté klávesy.
     * @param e - KeyEvent
     */
    @Override
    public void keyReleased(KeyEvent ke) {
        int key = ke.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            tlacidla[0] = false;
            this.vAnimacii = false;
        }
        
        if (key == KeyEvent.VK_RIGHT) {
            tlacidla[1] = false;
            this.vAnimacii = false;
        }
        
        if (key == KeyEvent.VK_UP) {
            tlacidla[2] = false;
        }
    }
    
    public void ovladanie() {
        if(tlacidla[0]) {
            if (!panel.isSrazka(new Rectangle(x - 2, y, marioObr.getWidth(null) - 2, marioObr.getHeight(null)))) {
                dx = -5;
                this.smer = ESmer.VLAVO;
                this.vAnimacii = true;
            } else {
                dx = 0;
            }  
        } else if (tlacidla[1]) {
            if (!panel.isSrazka(new Rectangle(x + 2, y, marioObr.getWidth(null) + 2, marioObr.getHeight(null)))) {
                dx = 5;   
                this.smer = ESmer.VPRAVO; 
                this.vAnimacii = true;
            }  else {
                dx = 0;
            }
        } else {
            dx = 0;
        }
        if (tlacidla[2]) {
            skok(12);
            mozeSkocit = false;
            mozePadat = true;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent ke) {
    } 

    public void setPada(boolean b) {
        mozePadat = b;
    }
    
    public void animacia() {
        if (smer == ESmer.VPRAVO) {
            if(animacia == 0) {
                this.typMaria = ETypHraca.MALY_BEZI1_VPRAVO;
            } else if (animacia == 6) {
                this.typMaria = ETypHraca.MALY_BEZI2_VPRAVO;
            } else if (animacia == 12) {
                this.typMaria = ETypHraca.MALY_BEZI3_VPRAVO;
            }
        } else {
            if(animacia == 0) {
                this.typMaria = ETypHraca.MALY_BEZI1_VLAVO;
            } else if (animacia == 6) {
                this.typMaria = ETypHraca.MALY_BEZI2_VLAVO;
            } else if (animacia == 12) {
                this.typMaria = ETypHraca.MALY_BEZI3_VLAVO;
            }
        }
        this.marioObr = this.typMaria.getObr();
    }

    public void setTypMaria(ETypHraca eTypHraca) {
        this.typMaria = eTypHraca;
        this.marioObr = this.typMaria.getObr();
    }
    
    public void endSkok() {
        this.dy -= 28;
        this.dx = 0;
        this.smer = ESmer.KONIEC;
    }
    
    public void posunObrazu(int x) {
        this.x = this.x - 5;
        this.panel.posunObrazu(5);
    }
}
