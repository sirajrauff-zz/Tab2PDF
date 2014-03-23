/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cse2311;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author waleed
 */
public class FontSelector {
  final static public String Fonts[] = {

"Monospace",
"Champagne & Limousines",

"Duty Cycle",
"Magnolia Light",
"Moon Flower",
"PWPerspective",

"Shut'Em Down",

"Helvetica",
"Times New Roman",
"Courier"};
   
   
   public static BaseFont getFont(int index){
       System.out.println(index +' '+Fonts[index]);
       
      try {
          if (Fonts[index] =="Helvetica"){
               return  BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1257, 
                  BaseFont.EMBEDDED);
           
       }else if (Fonts[index] =="Times New Roman"){
           return  BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1257, 
                  BaseFont.EMBEDDED);
       }
       else if (Fonts[index] =="Courier"){
          return  BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1257, 
                  BaseFont.EMBEDDED);
           
       }
          return BaseFont.createFont("fonts/"+ Fonts[index]+".ttf",
                  BaseFont.IDENTITY_H,
                  BaseFont.EMBEDDED);
      } catch (DocumentException ex) {
          Logger.getLogger(FontSelector.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
          Logger.getLogger(FontSelector.class.getName()).log(Level.SEVERE, null, ex);
      }
      return null;
}
    
}
