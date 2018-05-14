/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Enum pre typ hraca a urcuje s ktorym obrazkom bude pracovat
 * @author pego1
 */
public enum ETypHraca {
    MALY_VPRAVO  (new ImageIcon(ETypHraca.class.getResource("/res/little-mario-stand-right.png")), ESmer.VPRAVO),
    MALY_VLAVO  (new ImageIcon(ETypHraca.class.getResource("/res/little-mario-stand-left.png")), ESmer.VLAVO),
    MALY_BEZI1_VLAVO (new ImageIcon(ETypHraca.class.getResource("/res/little-mario-walk-1-left.png")), ESmer.VLAVO),
    MALY_BEZI2_VLAVO (new ImageIcon(ETypHraca.class.getResource("/res/little-mario-walk-2-left.png")), ESmer.VLAVO),
    MALY_BEZI3_VLAVO (new ImageIcon(ETypHraca.class.getResource("/res/little-mario-walk-3-left.png")), ESmer.VLAVO),
    MALY_BEZI1_VPRAVO (new ImageIcon(ETypHraca.class.getResource("/res/little-mario-walk-1-right.png")), ESmer.VPRAVO),
    MALY_BEZI2_VPRAVO (new ImageIcon(ETypHraca.class.getResource("/res/little-mario-walk-2-right.png")), ESmer.VPRAVO),
    MALY_BEZI3_VPRAVO (new ImageIcon(ETypHraca.class.getResource("/res/little-mario-walk-3-right.png")), ESmer.VPRAVO),
    MALY_SKOK_VPRAVO (new ImageIcon(ETypHraca.class.getResource("/res/little-mario-jump-right.png")), ESmer.VPRAVO),
    MALY_SKOK_VLAVO (new ImageIcon(ETypHraca.class.getResource("/res/little-mario-jump-left.png")), ESmer.VLAVO),
    VELKY_VPRAVO  (new ImageIcon(ETypHraca.class.getResource("/res/big-mario-stand-right.png")), ESmer.VPRAVO),
    VELKY_VLAVO  (new ImageIcon(ETypHraca.class.getResource("/res/big-mario-stand-left.png")), ESmer.VLAVO),
    VELKY_BEZI1_VLAVO (new ImageIcon(ETypHraca.class.getResource("/res/big-mario-walk-1-left.png")), ESmer.VLAVO),
    VELKY_BEZI2_VLAVO (new ImageIcon(ETypHraca.class.getResource("/res/big-mario-walk-2-left.png")), ESmer.VLAVO),
    VELKY_BEZI3_VLAVO (new ImageIcon(ETypHraca.class.getResource("/res/big-mario-walk-3-left.png")), ESmer.VLAVO),
    VELKY_BEZI1_VPRAVO (new ImageIcon(ETypHraca.class.getResource("/res/big-mario-walk-1-right.png")), ESmer.VPRAVO),
    VELKY_BEZI2_VPRAVO (new ImageIcon(ETypHraca.class.getResource("/res/big-mario-walk-2-right.png")), ESmer.VPRAVO),
    VELKY_BEZI3_VPRAVO (new ImageIcon(ETypHraca.class.getResource("/res/big-mario-walk-3-right.png")), ESmer.VPRAVO),
    VELKY_SKOK_VPRAVO (new ImageIcon(ETypHraca.class.getResource("/res/big-mario-jump-right.png")), ESmer.VPRAVO),
    VELKY_SKOK_VLAVO (new ImageIcon(ETypHraca.class.getResource("/res/big-mario-jump-left.png")), ESmer.VLAVO),
    MALY_KONIEC (new ImageIcon(ETypHraca.class.getResource("/res/little-mario-over.png")), ESmer.VLAVO);
    private final ImageIcon marioObr;
    private final ESmer smer;
    
    ETypHraca(ImageIcon zdroj, ESmer smer) {
        this.marioObr = zdroj;
        this.smer = smer;
    }
    
    /**
     * Getter pre obrazok
     * @return obrazok
     */
    public Image getObr() {
        return this.marioObr.getImage();
    }
    
    /**
     * Getter pre smer
     * @return smer
     */
    public ESmer getSmer() {
        return this.smer;
    }
}
