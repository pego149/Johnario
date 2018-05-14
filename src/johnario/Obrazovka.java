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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;
import nepriatelia.Goomba;
import nepriatelia.INepriatel;
import nepriatelia.Korytnacka;
import svet.Blok;
import svet.BonusBlok;
import svet.Castle;
import svet.IPrekazka;
import svet.Pozadie;
import svet.Rura;
import svet.Tehla;

/**
 * Trieda Obrazovka. Dedí z JPanelu a implementuje rozhranie
 * pre události ActionEvent
 * @author pego1
 */
public class Obrazovka extends JPanel implements ActionListener {
    private final int sirkaPanelu = 720;  // preferovaná šírka panelu
    private final int vyskaPanelu = 416;  // preferovaná výška panelu
    private Timer casovac; // casovac
    private Mario mario; // mario - player
    private boolean hraSa;    // true když se hraje, false když hra skončila
    private int citac;          // čítač frameov
    private ArrayList<IPrekazka> prekazky;    // seznam překážek
    private Audio bgMusic; // hudba na pozadí
    private IPrekazka zrazka; // prekazka s ktorou sa mario zrazil
    private Pozadie bg; // pozadie
    private int cas; // cas
    private int score; // score
    private ArrayList<INepriatel> nepriatelia; // pole nepriatelov
    private boolean koniec; // ak je koniec
    private boolean vMenu; // ak je v menu
    private boolean chybaSuboru; // ak nastala chyba nacitania zo suboru
    private boolean vyhral; // ak vyhral
    private Castle hrad; // hrad

    
    /**
     * Konstruktor Obrazovky.
     */
    public Obrazovka() {
        this.hraSa = false;
        this.koniec = false;
        this.vMenu = true;
        this.init();
    }
    
    /**
     * Sluzi k inicializacii
     */
    private void init() {
        this.setPreferredSize(new Dimension(this.sirkaPanelu, this.vyskaPanelu));
        this.setBackground(Color.black);
        this.setForeground(Color.white);
        this.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        this.setFocusable(true);
        this.nepriatelia = new ArrayList<>();
        this.prekazky = new ArrayList<>();
        this.bg = new Pozadie(this);
        this.cas = 300;
        this.mario = new Mario(this);
        this.chybaSuboru = false;
        this.vyhral = false;
        try {
            this.pridajObjektyDoMapy();
            this.addKeyListener(this.mario);
        } catch (IOException e) {
            this.chybaSuboru = true;
        }
    }
    
    /**
     * Spisti hru
     */
    public void start() {
        this.vMenu = false;
        this.hraSa = true;
        this.citac = 0;
        this.score = 0;
        this.casovac = new Timer(15, this);
        this.casovac.start();
        this.bgMusic = new Audio("/res/mario.wav");
        this.bgMusic.play();
    }

    /**
     * Getter Maria
     * @return this.mario
     */
    public Mario getMario() {
        return this.mario;
    }
    
    /**
     * Vykreslovanie
     * @param g grafický kontext
     */
    @Override
    public void paintComponent(Graphics g) {
        if (this.mario.getOkraje().getY() < 480) {
            super.paintComponent(g);
            this.bg.vykresliSa(g);
            this.vypisVrch(g); 
            this.mario.vykresliSe(g);
            for (int i = 0; i < this.nepriatelia.size(); i++) {
                if (this.nepriatelia.get(i).isZivy()) {
                    this.nepriatelia.get(i).vykresliSa(g);
                    this.nepriatelia.get(i).urobPohyb();
                }
                
            }
            
            for (int i = 0; i < this.prekazky.size(); i++) {
                IPrekazka prek = this.prekazky.get(i);
                if (prek.isViditelny()) {
                    prek.vykresliSa(g);
                }
                
            }
        } else {
            this.vypisKoniec(g);
        }
        if (this.vyhral) {
            this.vypisVyhru(g);
        }
        if (this.vMenu) {
            g.drawString("Stlačte medzerník pre štart...", 100, 200);
        }
        if (this.chybaSuboru) {
            g.drawString("Chyba suboru s levelom...", 100, 220);
        }
    }
    
    /**
     * Vypíše na panel stav
     * @param g grafický kontext
     */
    private void vypisVrch(Graphics g) {
        g.drawString("Čas: " + String.valueOf(this.cas) + "    Score: " + String.valueOf(this.score), 10, 30);
    }
    
    /**
     * Vypíše záverečný text.
     * @param g grafický kontext
     */
    private void vypisKoniec(Graphics g) {
        String textKonec = "G A M E   O V E R"; // text, který se vypíše při skončení hry
        Font pismo = new Font(Font.MONOSPACED, Font.BOLD, 28);
        g.setFont(pismo);
        FontMetrics fm = g.getFontMetrics(pismo);
        int sirkaTextu = fm.stringWidth(textKonec);

        g.setColor(Color.red);  // tento text bude červeně
        g.drawString(textKonec, (this.getWidth() - sirkaTextu) / 2, this.getHeight() / 2);
    }
    
    /**
     * Vypíše záverečný text.
     * @param g grafický kontext
     */
    private void vypisVyhru(Graphics g) {
        Font pismo = new Font(Font.MONOSPACED, Font.BOLD, 28);
        g.setFont(pismo);
        g.setColor(Color.red);  // tento text bude červeně
        g.drawString("V Y H R A L I   S T E", 200, 140);
        g.drawString("Skore: " + (this.score + this.cas), 300, 170);
    }
    
    /**
     * Definuje, co sa stane pri udalosti ActionEvent, ktoru generuje Timer.
     * @param ae udalost ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        this.citac++;       // zvýší čítač o jedničku
        if ((this.citac % 60) == 0) {
            this.cas--;
        }
        this.mario.urobPohyb();
        
        if (this.hraSa && (this.koniec || this.mario.getOkraje().getY() > 350)) {
            this.mario.endSkok();
            this.bgMusic.stop();
            Audio end = new Audio("/res/smb_gameover.wav");
            end.play();
            this.hraSa = false;
            this.koniec = false;
        }
        if (this.mario.getOkraje().getY() > 480) {
            this.casovac.stop();
            
        }
        if (this.mario.getOkraje().getX() > this.hrad.getX()) {
            this.bgMusic.stop();
            Audio end = new Audio("/res/smb_level.wav");
            end.play();
            this.hraSa = false;
            this.koniec = false;
            this.vyhral = true;
            this.casovac.stop();
        }
        this.repaint(); // metoda, která překreslí panel 
    }

    /**
     * Getter ci sa hra
     * @return this.hraSa
     */
    public boolean isHraSa() {
        return this.hraSa;
    }
    
    /**
     * Ukonci hru
     */
    public void koniecHry() {
        this.koniec = true;
    }
    
    /**
     * Zistí, ci doslo k zrazke.
     * @param okraje Okraje vstupneho obdlznika
     * @return true ak ano, fale ak nie
     */
    public boolean isZrazka(Rectangle okraje) {
        for (int i = 0; i < this.prekazky.size(); i++) {
            IPrekazka prek = this.prekazky.get(i);
            if (okraje.intersects(prek.getOkraje())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Prida objekty do mapy (prekazky, nepriatelov, hrad)
     */
    private void pridajObjektyDoMapy() throws IOException {
        List<String> riadky = Files.readAllLines(Paths.get("src/res/level.txt"));
        for (int i = 0; i < riadky.size(); i++) {
            for (int j = 0; j < riadky.get(i).length(); j++) {
                switch (riadky.get(i).charAt(j)) {
                    case '1':
                        this.prekazky.add(new Skala(this, j * 32, i * 32));
                        break;
                    case '2':
                        this.prekazky.add(new Tehla(this, j * 32, i * 32));
                        break;
                    case '3':
                        this.prekazky.add(new BonusBlok(this, j * 32, i * 32, true));
                        break;
                    case '4':
                        this.prekazky.add(new BonusBlok(this, j * 32, i * 32, false));
                        break;
                    case '5':
                        this.prekazky.add(new Rura(this, j * 32, i * 32));
                        break;
                    case '6':
                        this.prekazky.add(new Blok(this, j * 32, i * 32));
                        break;
                    case '7':
                        Castle h = new Castle(this, j * 32, i * 32);
                        this.prekazky.add(h);
                        this.hrad = h;
                        break;
                    case '8':
                        this.nepriatelia.add(new Goomba(this, j * 32, i * 32));
                        break;
                    case '9':
                        this.nepriatelia.add(new Korytnacka(this, j * 32, i * 32));
                        break;
                }
            }
        }
        /*
        this.prekazky.add(new Rura(this, 25 * 32, 8 * 32));
        for (int i = 0; i < 15; i++) {
            this.prekazky.add(new Skala(this, i * 32, 357));
            this.prekazky.add(new Skala(this, i * 32, 389));
        }
        this.prekazky.add(new Skala(this, 14 * 32, 325));

        for (int i = 19; i < 40; i++) {
            this.prekazky.add(new Skala(this, i * 32, 389));
            this.prekazky.add(new Skala(this, i * 32, 357));
        }
        this.prekazky.add(new Skala(this, 10 * 32, 261));
        this.prekazky.add(new Skala(this, 10 * 32, 229));
        this.prekazky.add(new Skala(this, 5 * 32, 5 * 32));
        this.prekazky.add(new Tehla(this, 6 * 32, 6 * 32));
        this.prekazky.add(new BonusBlok(this, 3 * 32, 6 * 32, true));
        this.prekazky.add(new BonusBlok(this, 7 * 32, 6 * 32, false));
        this.go.add(new Goomba(this, 7 * 32, 8 * 32));
        */
    }
    
    /**
     * Posunie obraz a vytvori paralax efekt
     * @param x o kolko sa ma posunut obraz
     */
    public void posunObrazu(int x) {
        for (IPrekazka iPr : this.prekazky) {
            iPr.setX(-x);
        }
        for (INepriatel g : this.nepriatelia) {
            g.setX(-x);
        }
        this.bg.setX(-x / 2);
    }
    
    /**
     * Prida score
     * @param score o kolko prida score
     */
    public void pridajScore(int score) {
        this.score += score;
    }

    /**
     * Getter ci je v menu
     * @return this.vMenu
     */
    public boolean getVMenu() {
        return this.vMenu;
    }
    
    /**
     * Getter na prekazku s ktorou nastala zrazka
     * @return this.zrazka
     */
    public IPrekazka getZrazka() {
        return this.zrazka;
    }
}
