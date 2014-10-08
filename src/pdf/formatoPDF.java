/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author AMaroevich
 */
public class formatoPDF {
    
     public HSSFFont fuente(HSSFWorkbook hoja){
        HSSFFont fuente = hoja.createFont();
        fuente.setFontHeightInPoints((short)10);
        fuente.setFontName(fuente.FONT_ARIAL);
        fuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        return fuente;
     }
     
     public HSSFFont fuenteDatos(HSSFWorkbook hoja){
        HSSFFont fuenteDatos = hoja.createFont();
        fuenteDatos.setFontHeightInPoints((short)10);
        fuenteDatos.setFontName(fuenteDatos.FONT_ARIAL);
        //fuenteDatos.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        return fuenteDatos;
    }
     public HSSFCellStyle celdaTextoColor(HSSFWorkbook hoja){
        
        HSSFCellStyle estiloCeldaTextoColor = hoja.createCellStyle();
        formatoPDF tipoFuente = new formatoPDF();
        
        HSSFFont estiloFuente = tipoFuente.fuente(hoja);

        //estiloCelda.setWrapText(true);
        estiloCeldaTextoColor.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCeldaTextoColor.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCeldaTextoColor.setFont(estiloFuente);

        // También, podemos establecer bordes...
        estiloCeldaTextoColor.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloCeldaTextoColor.setBottomBorderColor((short)8); //EL 8 ES PARA EL COLOR DE LA LINEA
        estiloCeldaTextoColor.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCeldaTextoColor.setLeftBorderColor((short)8);
        estiloCeldaTextoColor.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloCeldaTextoColor.setRightBorderColor((short)8);
        estiloCeldaTextoColor.setBorderTop(HSSFCellStyle.BORDER_THIN);
        estiloCeldaTextoColor.setTopBorderColor((short)8);

        // Establecemos el tipo de sombreado de nuestra celda
        estiloCeldaTextoColor.setFillForegroundColor((short)43);
        estiloCeldaTextoColor.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);       
        
        return estiloCeldaTextoColor;
    }
    public HSSFCellStyle celdaTextoSinColor(HSSFWorkbook hoja){
        
        HSSFCellStyle estiloCeldaTextoSinColor = hoja.createCellStyle();
        formatoPDF tipoFuente = new formatoPDF();
        
        HSSFFont estiloFuente = tipoFuente.fuenteDatos(hoja);

        //estiloCelda.setWrapText(true);
        estiloCeldaTextoSinColor.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        estiloCeldaTextoSinColor.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCeldaTextoSinColor.setFont(estiloFuente);

        // También, podemos establecer bordes...
        estiloCeldaTextoSinColor.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloCeldaTextoSinColor.setBottomBorderColor((short)8); //EL 8 ES PARA EL COLOR DE LA LINEA
        estiloCeldaTextoSinColor.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCeldaTextoSinColor.setLeftBorderColor((short)8);
        estiloCeldaTextoSinColor.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloCeldaTextoSinColor.setRightBorderColor((short)8);
        estiloCeldaTextoSinColor.setBorderTop(HSSFCellStyle.BORDER_THIN);
        estiloCeldaTextoSinColor.setTopBorderColor((short)8);
        
        return estiloCeldaTextoSinColor;
    } 
    public HSSFCellStyle celdaNumeroSinColor(HSSFWorkbook hoja){
        
        HSSFCellStyle estiloCeldaNumeroSinColor = hoja.createCellStyle();
        formatoPDF tipoFuente = new formatoPDF();
        
        HSSFFont estiloFuente = tipoFuente.fuenteDatos(hoja);
        HSSFDataFormat format = hoja.createDataFormat();

        //estiloCelda.setWrapText(true);
        estiloCeldaNumeroSinColor.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCeldaNumeroSinColor.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCeldaNumeroSinColor.setFont(estiloFuente);

        // También, podemos establecer bordes...
        estiloCeldaNumeroSinColor.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloCeldaNumeroSinColor.setBottomBorderColor((short)8); //EL 8 ES PARA EL COLOR DE LA LINEA
        estiloCeldaNumeroSinColor.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCeldaNumeroSinColor.setLeftBorderColor((short)8);
        estiloCeldaNumeroSinColor.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloCeldaNumeroSinColor.setRightBorderColor((short)8);
        estiloCeldaNumeroSinColor.setBorderTop(HSSFCellStyle.BORDER_THIN);
        estiloCeldaNumeroSinColor.setTopBorderColor((short)8);
        //estiloCeldaNumeroSinColor.setDataFormat(format.getFormat("#,##0"));
        
        return estiloCeldaNumeroSinColor;
    }
    public HSSFCellStyle celdaNumeroSinColorFormato(HSSFWorkbook hoja){
        
        HSSFCellStyle estiloCeldaNumeroSinColor = hoja.createCellStyle();
        formatoPDF tipoFuente = new formatoPDF();
        
        HSSFFont estiloFuente = tipoFuente.fuenteDatos(hoja);
        HSSFDataFormat format = hoja.createDataFormat();

        //estiloCelda.setWrapText(true);
        estiloCeldaNumeroSinColor.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCeldaNumeroSinColor.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCeldaNumeroSinColor.setFont(estiloFuente);

        // También, podemos establecer bordes...
        estiloCeldaNumeroSinColor.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        estiloCeldaNumeroSinColor.setBottomBorderColor((short)8); //EL 8 ES PARA EL COLOR DE LA LINEA
        estiloCeldaNumeroSinColor.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCeldaNumeroSinColor.setLeftBorderColor((short)8);
        estiloCeldaNumeroSinColor.setBorderRight(HSSFCellStyle.BORDER_THIN);
        estiloCeldaNumeroSinColor.setRightBorderColor((short)8);
        estiloCeldaNumeroSinColor.setBorderTop(HSSFCellStyle.BORDER_THIN);
        estiloCeldaNumeroSinColor.setTopBorderColor((short)8);
        estiloCeldaNumeroSinColor.setDataFormat(format.getFormat("#,##0"));
        
        return estiloCeldaNumeroSinColor;
    }
     
}

