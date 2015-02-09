/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Components;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Administrador
 */
public class TextField extends JTextField{

    public TextField(String titulo) {
        super(titulo);
        setOpaque(true);
        this.setForeground(Color.WHITE);
        this.setBackground(Color.DARK_GRAY);
        
    }
    
    
    
}
