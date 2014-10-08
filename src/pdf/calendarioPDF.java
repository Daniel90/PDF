/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;

import java.util.Calendar;
/**
 *
 * @author AMaroevich
 */
public class calendarioPDF {
    //para año actual
    public int anoActual(){
        
        Calendar c1 = Calendar.getInstance();
        int ano = (c1.get(Calendar.YEAR));
        
        return ano;
    }
    //borde minimo
    public int fechaMinimaPower(String mes, String ano){
        int fechaInicial = 0;
        String fechaCompleta = "";
        fechaCompleta = ano + mes + "01";
        
        fechaInicial = Integer.parseInt(fechaCompleta);
        
        return fechaInicial;
    }
    //borde maximo
    public int fechaMaximaPower(String mes, String ano){
        int fechaFinal = 0;
        String fechaCompleta = "";
        fechaCompleta = ano + mes + "31";
        
        fechaFinal = Integer.parseInt(fechaCompleta);
        
        return fechaFinal;
    }
}
