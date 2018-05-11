package johnario;

import player.Mario;
import svet.Skala;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;
import player.ETypHraca;
import svet.IPrekazka;
import svet.Pozadie;
import svet.Tehla;

/**
 * Třída představující herní panel. Dědí z JPanelu a implementuje rozhraní
 * pro naslouchání událostem ActionEvent
 * @author vita
 */
public class Obrazovka extends JPanel implements ActionListener {
    private final int sirkaPanelu = 720;  // preferovaná šířka panelu
    private final int vyskaPanelu = 400;  // preferovaná výška panelu
    private Timer casovac;    
    private Mario mario;
    private boolean hraSa;    // true když se hraje, false když hra skončila
    private int citac;          // počítá body, čím déle se hraje, tím více
    private ArrayList<IPrekazka> prekazky;    // seznam překážek
    private Audio bgMusic;
    private IPrekazka zrazka;
    private Pozadie bg;

    public IPrekazka getZrazka() {
        return zrazka;
    }
    
    /**
     * Konstruktor HernihoPanelu.
     */
    public Obrazovka() {
        init();
        start();    
    }
    
    /**
     * Slouží k inicializaci HernihoPanelu
     */
    private void init() {
        this.setPreferredSize(new Dimension(sirkaPanelu, vyskaPanelu));
        this.setBackground(Color.black);
        this.setForeground(Color.white);
        this.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        this.setFocusable(true);
    }
    
    /**
     * Provede inicializaci proměnných nutných ke hře a spustí timer.
     */
    private void start() {
        hraSa = true;
        mario = new Mario(this);
        citac = 0;
        prekazky = new ArrayList<>();
        this.addKeyListener(mario);
        pridajSpodok();
        bg = new Pozadie(this);
        
        casovac = new Timer(10, this);
        casovac.start();
        bgMusic = new Audio("/johnario/mario.wav");
        bgMusic.play();
    }

    public Mario getMario() {
        return mario;
    }
    
    /**
     * Zde definujeme, co se má dít při vykreslování HernihoPanelu.
     * @param g grafický kontext
     */
    @Override
    public void paintComponent(Graphics g) {
        // pokud se hraje
        if (mario.getOkraje().getY() < 480) {
            super.paintComponent(g);
            bg.vykresliSe(g);
            
            vypisCitac(g); 
            mario.vykresliSe(g);
            
            
            for (int i = 0; i < prekazky.size(); i++) {
                IPrekazka prek = prekazky.get(i);
                if(prek.isViditelny()) {
                    prek.vykresliSe(g);
                }
                
            }
        }
        // pokud hra skončila
        else {
            vypisKonec(g);
        }
    }
    
    /**
     * Vypíše na panel stav čítače
     * @param g grafický kontext
     */
    private void vypisCitac(Graphics g) {
        g.drawString(String.valueOf(citac), 10, 30);
    }
    
    /**
     * V případě ukončení hry vypíše závěrečný text.
     * @param g grafický kontext
     */
    private void vypisKonec(Graphics g) {
        String textKonec = "G A M E   O V E R"; // text, který se vypíše při skončení hry
        Font pismo = new Font(Font.MONOSPACED, Font.BOLD, 28);
        g.setFont(pismo);
        FontMetrics fm = g.getFontMetrics(pismo);
        int sirkaTextu = fm.stringWidth(textKonec);

        g.setColor(Color.red);  // tento text bude červeně
        g.drawString(textKonec, (this.getWidth() - sirkaTextu) / 2, this.getHeight() / 2);
    }
    
    /**
     * Definuje, co se má stát při vzniku události ActionEvent, kterou generuje Timer.
     * @param ae událost ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        
        citac++;        // zvýší čítač o jedničku
        
        mario.provedPohyb();
        
        if (hraSa && mario.getOkraje().getY() > 350) {
            mario.endSkok();
            mario.setTypMaria(ETypHraca.MALY_KONIEC);
            bgMusic.stop();
            Audio end = new Audio("/res/smb_gameover.wav");
            end.play();
            hraSa = false;
        }
        if (mario.getOkraje().getY() > 480) {
            casovac.stop();
            
        }
        this.repaint(); // metoda, která překreslí panel 
    }

    public boolean isHraSa() {
        return hraSa;
    }
    
    /**
     * Zjistí, zda došlo ke srážce.
     * @return true pokud ano, fale pokud ne
     */
    public boolean isSrazka(Rectangle okraje) {
        for (int i = 0; i < prekazky.size(); i++) {
            IPrekazka prek = prekazky.get(i);
            if (okraje.intersects(prek.getOkraje())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Zkontroluj prodlevu a v případě, že je čas, tak přidá další překážku.
     * S pokročilostí hry se prodleva zmenšuje a překážek přibývá.
     */
    private void pridajSpodok() {
       for (int i = 0; i < 15; i++) {
           this.prekazky.add(new Skala(this, i * 32, 325));
           this.prekazky.add(new Skala(this, i * 32, 357));
       }
       this.prekazky.add(new Skala(this, 14 * 32, 293));
       
       for (int i = 19; i < 40; i++) {
           this.prekazky.add(new Skala(this, i * 32, 325));
           this.prekazky.add(new Skala(this, i * 32, 357));
       }
       this.prekazky.add(new Skala(this, 10 * 32, 261));
       this.prekazky.add(new Skala(this, 10 * 32, 229));
       this.prekazky.add(new Skala(this, 5 * 32, 5 * 32));
       this.prekazky.add(new Tehla(this, 6 * 32, 6 * 32));
    }
    
    public void posunObrazu(int x) {
        for (IPrekazka iPr : this.prekazky) {
            iPr.setX(-x);
        }
        this.bg.setX(-x/2);
    }
}
