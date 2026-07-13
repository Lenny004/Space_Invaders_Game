/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tipografia;

import java.awt.Font;
import java.io.InputStream;

/**
 *
 * @author lenny
 */
public class Fuente {
    private Font fuenteNueva = null;
    public String SpaceInvaders = "space_invaders.ttf";
    
    
    /*FONT.PLAIN = 0, Font.BOLD = 1, Font.ITALIC= 2*/
    /*tamanio = float*/

    
    public Font fuenteSpace (String fontName, int estilo, float tamanio){
        try{
            // Carga desde resources: /Tipografia/space_invaders.ttf
            String resourcePath = fontName.startsWith("/") ? fontName : "/Tipografia/" + fontName;
            InputStream inputs = getClass().getResourceAsStream(resourcePath);
            if (inputs == null) {
                // Fallback: recurso relativo al paquete (compatibilidad)
                inputs = getClass().getResourceAsStream(fontName);
            }
            fuenteNueva = Font.createFont(Font.TRUETYPE_FONT, inputs);
            inputs.close();
        }catch (Exception ex){
            //Si existe un error se carga fuente por defecto Arial
            fuenteNueva = new Font("Arial", Font.PLAIN, 14);
        }
        Font tfont = fuenteNueva.deriveFont(estilo, tamanio);
        return tfont;
    }
}
