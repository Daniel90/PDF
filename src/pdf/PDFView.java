/*
 * PDFView.java
 * author: Daniel
 */

package pdf;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import consulta.sql;
import consulta.sqlKilos;
import java.awt.Cursor;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import pdf.calendarioPDF;
import java.sql.*;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import pdf.formatoPDF;
import java.text.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import pdf.envioCorreo;
/**
 * The application's main frame.
 */
public class PDFView extends FrameView {

    public PDFView(SingleFrameApplication app) {
        super(app);

        initComponents();
        
        
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = PDFApp.getApplication().getMainFrame();
            aboutBox = new PDFAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        PDFApp.getApplication().show(aboutBox);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(pdf.PDFApp.class).getContext().getResourceMap(PDFView.class);
        mainPanel.setBackground(resourceMap.getColor("mainPanel.background")); // NOI18N
        mainPanel.setName("mainPanel"); // NOI18N

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jTextField2.setText(resourceMap.getString("txtAno.text")); // NOI18N
        jTextField2.setName("txtAno"); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));
        jComboBox1.setName("jComboBox1"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                .addGap(265, 265, 265))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(pdf.PDFApp.class).getContext().getActionMap(PDFView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
  
   
   public static PdfPTable createTableEneroV(int mes,ResultSet documentosPower7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidths(new float[]{0.01f,0.1f,0.1f,0.1f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(4);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        
        
        int j=1;
        float sumaEnero = 0,sumaT = 0;
        float sumaHorizontal = 0;
        while(documentosPower7.next()){
                PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAMN87"),cuerpo));
                celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaFAM);

                PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DESN87"),cuerpo));
                celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaDES);


                DecimalFormat df = new DecimalFormat("###,###,###.00");
                String cadena = "";
                sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01N87"));
                
                cadena = String.valueOf(df.format(Float.parseFloat(documentosPower7.getString("V01N87"))));
                PdfPCell celda = new PdfPCell(new Phrase(cadena,cuerpo));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celda);
                
                DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
                sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01N87"));
                sumaT = sumaT + sumaHorizontal;
                PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(cell);
                sumaHorizontal = 0;
                
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(df.format(sumaT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        
        return tabla;
   }  
   public static PdfPTable createTableEFV(int mes,ResultSet documentosPower7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        
        
        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidths(new float[]{0.015f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(5);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaT = 0;
        while(documentosPower7.next()){
                PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAMN87"),cuerpo));
                celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaFAM);
                
                PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DESN87"),cuerpo));
                celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaDES);


                DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
                String cadenaEne = "";
                sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01N87"));
                cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01N87"))));
                PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
                celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celdaEne);

                DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
                String cadenaFeb = "";
                sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02N87"));
                cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02N87"))));
                PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
                celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celdaFeb);
                
                DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
                sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01N87"))+
                                                  Float.parseFloat(documentosPower7.getString("V02N87"));
                sumaT = sumaT + sumaHorizontal;
                PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(cell);
                sumaHorizontal = 0;
        }    
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumaT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMV(int mes,ResultSet documentosPower7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidths(new float[]{0.017f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(6);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumT = 0;
         while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01N87"))+
                                              Float.parseFloat(documentosPower7.getString("V02N87"))+
                                              Float.parseFloat(documentosPower7.getString("V03N87"));
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            sumT = sumT + sumaHorizontal;
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAV(int mes,ResultSet documentosPower7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidths(new float[]{0.02f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(7);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumT = 0;
        
        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01N87"))+
                                              Float.parseFloat(documentosPower7.getString("V02N87"))+
                                              Float.parseFloat(documentosPower7.getString("V03N87"))+
                                              Float.parseFloat(documentosPower7.getString("V04N87"));
            sumT = sumT = sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMV(int mes,ResultSet documentosPower7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(8);
        tabla.setWidths(new float[]{0.023f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(8);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumT = 0;
        
        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01N87"))+
                                              Float.parseFloat(documentosPower7.getString("V02N87"))+
                                              Float.parseFloat(documentosPower7.getString("V03N87"))+
                                              Float.parseFloat(documentosPower7.getString("V04N87"))+
                                              Float.parseFloat(documentosPower7.getString("V05N87"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJV(int mes,ResultSet documentosPower7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(9);
        tabla.setWidths(new float[]{0.028f,0.11f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(9);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Ventas $ Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumT = 0;
        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06N87"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosPower7.getString("V06N87"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01N87"))+
                                              Float.parseFloat(documentosPower7.getString("V02N87"))+
                                              Float.parseFloat(documentosPower7.getString("V03N87"))+
                                              Float.parseFloat(documentosPower7.getString("V04N87"))+
                                              Float.parseFloat(documentosPower7.getString("V05N87"))+
                                              Float.parseFloat(documentosPower7.getString("V06N87"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJV(int mes,ResultSet documentosPower7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(10);
        tabla.setWidths(new float[]{0.033f,0.13f,0.1f,0.11f,0.1f,0.1f,0.1f,0.1f,0.1f,0.12f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(10);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Ventas $ Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Ventas $ Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumT = 0;
        
        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06N87"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosPower7.getString("V06N87"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07N87"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosPower7.getString("V07N87"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01N87"))+
                                              Float.parseFloat(documentosPower7.getString("V02N87"))+
                                              Float.parseFloat(documentosPower7.getString("V03N87"))+
                                              Float.parseFloat(documentosPower7.getString("V04N87"))+
                                              Float.parseFloat(documentosPower7.getString("V05N87"))+
                                              Float.parseFloat(documentosPower7.getString("V06N87"))+
                                              Float.parseFloat(documentosPower7.getString("V07N87"));
            sumT = sumT +sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJAV(int mes,ResultSet documentosPower7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(8);
        cuerpo.setSize(7);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(11);
        tabla.setWidths(new float[]{0.31f,1.2f,1,1,1,1,1,1,1,1,1});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(11);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Ventas $ Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Ventas $ Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Ventas $ Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumT = 0;
        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06N87"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosPower7.getString("V06N87"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07N87"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosPower7.getString("V07N87"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08N87"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosPower7.getString("V08N87"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V02N87")) +
                                              Float.parseFloat(documentosPower7.getString("V03N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V04N87")) +
                                              Float.parseFloat(documentosPower7.getString("V05N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V06N87")) +
                                              Float.parseFloat(documentosPower7.getString("V07N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V08N87"));
            sumT = sumT +sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
            
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASV(int mes,ResultSet documentosPower7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(7);
        cuerpo.setSize(6);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(12);
        tabla.setWidths(new float[]{0.5f,2,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.5f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(12);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Ventas $ Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Ventas $ Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Ventas $ Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Ventas $ Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumT = 0;

        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06N87"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosPower7.getString("V06N87"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07N87"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosPower7.getString("V07N87"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08N87"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosPower7.getString("V08N87"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosPower7.getString("V09N87"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosPower7.getString("V09N87"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V02N87")) +
                                              Float.parseFloat(documentosPower7.getString("V03N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V04N87")) +
                                              Float.parseFloat(documentosPower7.getString("V05N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V06N87")) +
                                              Float.parseFloat(documentosPower7.getString("V07N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V08N87")) +
                                              Float.parseFloat(documentosPower7.getString("V09N87"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASOV(int mes,ResultSet documentosPower7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(7);
        cuerpo.setSize(6);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(13);
        tabla.setWidths(new float[]{0.5f,1.8f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.5f,1.5f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(13);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Ventas $ Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Ventas $ Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Ventas $ Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Ventas $ Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Ventas $ Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumT = 0;
        
        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.0");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.0");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.0");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.0");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.0");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.0");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06N87"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosPower7.getString("V06N87"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.0");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07N87"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosPower7.getString("V07N87"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.0");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08N87"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosPower7.getString("V08N87"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.0");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosPower7.getString("V09N87"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosPower7.getString("V09N87"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###,###,###.0");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosPower7.getString("V10N87"));
            cadenaOct = String.valueOf(dfOct.format(Float.parseFloat(documentosPower7.getString("V10N87"))));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.0");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V02N87")) +
                                              Float.parseFloat(documentosPower7.getString("V03N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V04N87")) +
                                              Float.parseFloat(documentosPower7.getString("V05N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V06N87")) +
                                              Float.parseFloat(documentosPower7.getString("V07N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V08N87")) +
                                              Float.parseFloat(documentosPower7.getString("V09N87")) +
                                              Float.parseFloat(documentosPower7.getString("V10N87"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        } 
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.0");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.0");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.0");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.0");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.0");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.0");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.0");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.0");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.0");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfOct = new DecimalFormat("###,###,###.0");
        PdfPCell cellOctu = new PdfPCell(new Phrase(dfOct.format(sumaOct),fuente));
        cellOctu.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellOctu.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOctu);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.0");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASONV(int mes,ResultSet documentosPower7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(6);
        cuerpo.setSize(5);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(14);
        tabla.setWidths(new float[]{0.45f,1.5f,1.2f,1.3f,1.2f,1.2f,1.1f,1.2f,1.2f,1.3f,1.2f,1.2f,1.5f,1.5f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(14);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Ventas $ Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Ventas $ Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Ventas $ Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Ventas $ Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Ventas $ Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);

        PdfPCell cellNov = new PdfPCell(new Paragraph("Ventas $ Noviembre",fuente));
        cellNov.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellNov.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNov);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumaNov = 0,sumT = 0;
        
        while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.0");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.0");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.0");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.0");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.0");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.0");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06N87"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosPower7.getString("V06N87"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.0");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07N87"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosPower7.getString("V07N87"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.0");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08N87"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosPower7.getString("V08N87"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.0");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosPower7.getString("V09N87"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosPower7.getString("V09N87"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###,###,###.0");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosPower7.getString("V10N87"));
            cadenaOct = String.valueOf(dfOct.format(Float.parseFloat(documentosPower7.getString("V10N87"))));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);

            DecimalFormat dfNov = new DecimalFormat("###,###,###.0");
            String cadenaNov = "";
            sumaNov = sumaNov + Float.parseFloat(documentosPower7.getString("V11N87"));
            cadenaNov = String.valueOf(dfNov.format(Float.parseFloat(documentosPower7.getString("V11N87"))));
            PdfPCell celdaNov = new PdfPCell(new Phrase(cadenaNov,cuerpo));
            celdaNov.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaNov);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.0");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V02N87")) +
                                              Float.parseFloat(documentosPower7.getString("V03N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V04N87")) +
                                              Float.parseFloat(documentosPower7.getString("V05N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V06N87")) +
                                              Float.parseFloat(documentosPower7.getString("V07N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V08N87")) +
                                              Float.parseFloat(documentosPower7.getString("V09N87")) +
                                              Float.parseFloat(documentosPower7.getString("V10N87")) +
                                              Float.parseFloat(documentosPower7.getString("V11N87"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
            
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.0");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.0");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.0");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.0");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.0");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.0");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.0");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.0");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.0");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfOct = new DecimalFormat("###,###,###.0");
        PdfPCell cellOctu = new PdfPCell(new Phrase(dfOct.format(sumaOct),fuente));
        cellOctu.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellOctu.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOctu);
        
        DecimalFormat dfNov = new DecimalFormat("###,###,###.0");
        PdfPCell cellNovi = new PdfPCell(new Phrase(dfNov.format(sumaNov),fuente));
        cellNovi.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellNovi.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNovi);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.0");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASONDV(int mes,ResultSet documentosPower7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(6);
        cuerpo.setSize(5);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(15);
        tabla.setWidths(new float[]{0.5f,1.8f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.5f,1.5f,1.5f,1.8f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("VENTA EN PESOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(15);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Ventas $ Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Ventas $ Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Ventas $ Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Ventas $ Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Ventas $ Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Ventas $ Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Ventas $ Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Ventas $ Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Ventas $ Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Ventas $ Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);

        PdfPCell cellNov = new PdfPCell(new Paragraph("Ventas $ Noviembre",fuente));
        cellNov.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellNov.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNov);

        PdfPCell cellDic = new PdfPCell(new Paragraph("Ventas $ Diciembre",fuente));
        cellDic.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDic.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDic);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumaNov = 0,sumaDic = 0,sumT = 0;

         while(documentosPower7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosPower7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosPower7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.0");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosPower7.getString("V01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosPower7.getString("V01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.0");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosPower7.getString("V02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosPower7.getString("V02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.0");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosPower7.getString("V03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosPower7.getString("V03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.0");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosPower7.getString("V04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosPower7.getString("V04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.0");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosPower7.getString("V05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosPower7.getString("V05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.0");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosPower7.getString("V06N87"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosPower7.getString("V06N87"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.0");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosPower7.getString("V07N87"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosPower7.getString("V07N87"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.0");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosPower7.getString("V08N87"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosPower7.getString("V08N87"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.0");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosPower7.getString("V09N87"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosPower7.getString("V09N87"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###,###,###.0");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosPower7.getString("V10N87"));
            cadenaOct = String.valueOf(dfOct.format(Float.parseFloat(documentosPower7.getString("V10N87"))));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);

            DecimalFormat dfNov = new DecimalFormat("###,###,###.0");
            String cadenaNov = "";
            sumaNov = sumaNov + Float.parseFloat(documentosPower7.getString("V11N87"));
            cadenaNov = String.valueOf(dfNov.format(Float.parseFloat(documentosPower7.getString("V11N87"))));
            PdfPCell celdaNov = new PdfPCell(new Phrase(cadenaNov,cuerpo));
            celdaNov.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaNov);

            DecimalFormat dfDic = new DecimalFormat("###,###,###.0");
            String cadenaDic = "";
            sumaDic = sumaDic + Float.parseFloat(documentosPower7.getString("V12N87"));
            cadenaDic = String.valueOf(dfDic.format(Float.parseFloat(documentosPower7.getString("V12N87"))));
            PdfPCell celdaDic = new PdfPCell(new Phrase(cadenaDic,cuerpo));
            celdaDic.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaDic);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.0");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosPower7.getString("V01N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V02N87")) +
                                              Float.parseFloat(documentosPower7.getString("V03N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V04N87")) +
                                              Float.parseFloat(documentosPower7.getString("V05N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V06N87")) +
                                              Float.parseFloat(documentosPower7.getString("V07N87")) + 
                                              Float.parseFloat(documentosPower7.getString("V08N87")) +
                                              Float.parseFloat(documentosPower7.getString("V09N87")) +
                                              Float.parseFloat(documentosPower7.getString("V10N87")) +
                                              Float.parseFloat(documentosPower7.getString("V11N87")) +
                                              Float.parseFloat(documentosPower7.getString("V12N87"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
         PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.0");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.0");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.0");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.0");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.0");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.0");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.0");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.0");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.0");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfOct = new DecimalFormat("###,###,###.0");
        PdfPCell cellOctu = new PdfPCell(new Phrase(dfOct.format(sumaOct),fuente));
        cellOctu.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellOctu.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOctu);
        
        DecimalFormat dfNov = new DecimalFormat("###,###,###.0");
        PdfPCell cellNovi = new PdfPCell(new Phrase(dfNov.format(sumaNov),fuente));
        cellNovi.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellNovi.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNovi);
        
        DecimalFormat dfDic = new DecimalFormat("###,###,###.0");
        PdfPCell cellDici = new PdfPCell(new Phrase(dfDic.format(sumaDic),fuente));
        cellDici.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellDici.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDici);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.0");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   
   public static PdfPTable createTableEneroK(int mes,ResultSet documentosKilos7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidths(new float[]{0.01f,0.1f,0.1f,0.1f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(4);
        tabla.addCell(cellTitulo);
        
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);

        int j=1;
        float sumaEnero = 0,sumaHorizontal = 0,sumaT = 0;        
        while(documentosKilos7.next()){
                PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAMN87"),cuerpo));
                celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaFAM);

                PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DESN87"),cuerpo));
                celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaDES);


                DecimalFormat df = new DecimalFormat("###,###,###.00");
                String cadena = "";
                sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01N87"));
                
                cadena = String.valueOf(df.format(Float.parseFloat(documentosKilos7.getString("K01N87"))));
                PdfPCell celda = new PdfPCell(new Phrase(cadena,cuerpo));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celda);
                
                DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
                sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01N87"));
                sumaT = sumaT + sumaHorizontal;
                PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(cell);
                sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumaT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        
        return tabla;
   }
   public static PdfPTable createTableEFK(int mes,ResultSet documentosKilos7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidths(new float[]{0.015f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(5);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaT = 0;
        while(documentosKilos7.next()){
                PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAMN87"),cuerpo));
                celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaFAM);

                PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DESN87"),cuerpo));
                celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaDES);


                DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
                String cadenaEne = "";
                cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01N87"))));
                sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01N87"));
                PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
                celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celdaEne);

                DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
                String cadenaFeb = "";
                cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02N87"))));
                sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02N87"));
                PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
                celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(celdaFeb);
                
                DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
                sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01N87"))+
                                                  Float.parseFloat(documentosKilos7.getString("K02N87"));
                sumaT = sumaT + sumaHorizontal;
                PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(cell);
                sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumaT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMK(int mes,ResultSet documentosKilos7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidths(new float[]{0.017f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(6);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumT = 0;
        
         while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01N87"))));
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01N87"));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02N87"))));
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02N87"));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03N87"))));
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03N87"));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K02N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K03N87"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAK(int mes,ResultSet documentosKilos7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidths(new float[]{0.02f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(7);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumT = 0;

        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K02N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K03N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K04N87"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMK(int mes,ResultSet documentosKilos7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(8);
        tabla.setWidths(new float[]{0.023f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(8);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumT = 0;

        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K02N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K03N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K04N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K05N87"));
            sumT = sumT + sumaHorizontal;                                 
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJK(int mes,ResultSet documentosKilos7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(9);
        tabla.setWidths(new float[]{0.028f,0.11f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(9);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Kilos Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumT = 0;

        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosKilos7.getString("K06N87"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosKilos7.getString("K06N87"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K02N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K03N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K04N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K05N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K06N87"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJK(int mes,ResultSet documentosKilos7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(9);
        cuerpo.setSize(8);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(10);
        tabla.setWidths(new float[]{0.033f,0.13f,0.1f,0.11f,0.1f,0.1f,0.1f,0.1f,0.1f,0.12f});
        
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(10);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Kilos Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Kilos Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);
        
        PdfPCell cellTot = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTot);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumT = 0;

        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);


            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosKilos7.getString("K06N87"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosKilos7.getString("K06N87"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosKilos7.getString("K07N87"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosKilos7.getString("K07N87"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K02N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K03N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K04N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K05N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K06N87"))+
                                              Float.parseFloat(documentosKilos7.getString("K07N87"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellT = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJAK(int mes,ResultSet documentosKilos7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(8);
        cuerpo.setSize(7);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(11);
        tabla.setWidths(new float[]{0.31f,1.2f,1,1,1,1,1,1,1,1,1});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(11);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Kilos Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Kilos Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Kilos Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumT = 0;
        
        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosKilos7.getString("K06N87"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosKilos7.getString("K06N87"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosKilos7.getString("K07N87"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosKilos7.getString("K07N87"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);
            
            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosKilos7.getString("K08N87"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosKilos7.getString("K08N87"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K02N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K03N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K04N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K05N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K06N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K07N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K08N87"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASK(int mes,ResultSet documentosKilos7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(7);
        cuerpo.setSize(6);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(12);
        tabla.setWidths(new float[]{0.5f,2,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.5f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(12);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Kilos Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Kilos Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Kilos Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Kilos Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumT = 0;

        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosKilos7.getString("K06N87"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosKilos7.getString("K06N87"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosKilos7.getString("K07N87"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosKilos7.getString("K07N87"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosKilos7.getString("K08N87"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosKilos7.getString("K08N87"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosKilos7.getString("K09N87"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosKilos7.getString("K09N87"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K02N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K03N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K04N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K05N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K06N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K07N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K08N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K09N87"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASOK(int mes,ResultSet documentosKilos7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(7);
        cuerpo.setSize(6);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(13);
        tabla.setWidths(new float[]{0.5f,1.8f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.5f,1.5f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(13);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Kilos Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Kilos Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Kilos Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Kilos Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Kilos Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumT = 0;

        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosKilos7.getString("K06N87"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosKilos7.getString("K06N87"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosKilos7.getString("K07N87"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosKilos7.getString("K07N87"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosKilos7.getString("K08N87"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosKilos7.getString("K08N87"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosKilos7.getString("K09N87"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosKilos7.getString("K09N87"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###,###,###.00");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosKilos7.getString("K10N87"));
            cadenaOct = String.valueOf(dfOct.format(Float.parseFloat(documentosKilos7.getString("K10N87"))));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K02N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K03N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K04N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K05N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K06N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K07N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K08N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K09N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K10N87"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfOct = new DecimalFormat("###,###,###.00");
        PdfPCell cellOctu = new PdfPCell(new Phrase(dfOct.format(sumaOct),fuente));
        cellOctu.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellOctu.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOctu);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASONK(int mes,ResultSet documentosKilos7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(6);
        cuerpo.setSize(5);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(14);
        tabla.setWidths(new float[]{0.45f,1.5f,1.2f,1.3f,1.2f,1.2f,1.1f,1.2f,1.2f,1.3f,1.2f,1.2f,1.5f,1.5f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(14);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Kilos Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Kilos Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Kilos Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Kilos Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Kilos Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);

        PdfPCell cellNov = new PdfPCell(new Paragraph("Kilos Noviembre",fuente));
        cellNov.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellNov.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNov);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumaNov = 0,sumT = 0;

        while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosKilos7.getString("K06N87"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosKilos7.getString("K06N87"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosKilos7.getString("K07N87"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosKilos7.getString("K07N87"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosKilos7.getString("K08N87"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosKilos7.getString("K08N87"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosKilos7.getString("K09N87"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosKilos7.getString("K09N87"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###,###,###.00");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosKilos7.getString("K10N87"));
            cadenaOct = String.valueOf(dfOct.format(Float.parseFloat(documentosKilos7.getString("K10N87"))));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);

            DecimalFormat dfNov = new DecimalFormat("###,###,###.00");
            String cadenaNov = "";
            sumaNov = sumaNov + Float.parseFloat(documentosKilos7.getString("K11N87"));
            cadenaNov = String.valueOf(dfNov.format(Float.parseFloat(documentosKilos7.getString("K11N87"))));
            PdfPCell celdaNov = new PdfPCell(new Phrase(cadenaNov,cuerpo));
            celdaNov.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaNov);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K02N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K03N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K04N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K05N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K06N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K07N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K08N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K09N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K10N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K11N87"));
            sumT = sumT + sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfOct = new DecimalFormat("###,###,###.00");
        PdfPCell cellOctu = new PdfPCell(new Phrase(dfOct.format(sumaOct),fuente));
        cellOctu.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellOctu.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOctu);
        
        DecimalFormat dfNov = new DecimalFormat("###,###,###.00");
        PdfPCell cellNovi = new PdfPCell(new Phrase(dfNov.format(sumaNov),fuente));
        cellNovi.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellNovi.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNovi);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
   public static PdfPTable createTableEFMAMJJASONDK(int mes,ResultSet documentosKilos7) throws DocumentException, SQLException{
        Font fuente= new Font();
        Font cuerpo = new Font();
        fuente.setSize(6);
        cuerpo.setSize(5);
        fuente.setStyle(Font.BOLD);
        
        PdfPTable tabla = new PdfPTable(15);
        tabla.setWidths(new float[]{0.5f,1.8f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.8f,1.5f,1.5f,1.5f,1.8f});
        PdfPCell cellTitulo = new PdfPCell(new Paragraph("KILOS AÑO 2014 - NACIONAL",FontFactory.getFont(FontFactory.TIMES_BOLD,10)));
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellTitulo.setColspan(15);
        tabla.addCell(cellTitulo);
        
        PdfPCell cellFAM = new PdfPCell(new Paragraph("FAM",fuente));
        cellFAM.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFAM.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFAM);

        PdfPCell cellDESN = new PdfPCell(new Paragraph("Descripción familia",fuente));
        cellDESN.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDESN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDESN);

        PdfPCell cellVENERO = new PdfPCell(new Paragraph("Kilos Enero",fuente));
        cellVENERO.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellVENERO.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellVENERO);

        PdfPCell cellFeb = new PdfPCell(new Paragraph("Kilos Febrero",fuente));
        cellFeb.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellFeb.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFeb);

        PdfPCell cellMar = new PdfPCell(new Paragraph("Kilos Marzo",fuente));
        cellMar.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMar.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMar);

        PdfPCell cellAbr = new PdfPCell(new Paragraph("Kilos Abril",fuente));
        cellAbr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAbr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbr);

        PdfPCell cellMayo = new PdfPCell(new Paragraph("Kilos Mayo",fuente));
        cellMayo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMayo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMayo);

        PdfPCell cellJun = new PdfPCell(new Paragraph("Kilos Junio",fuente));
        cellJun.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJun.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJun);

        PdfPCell cellJul = new PdfPCell(new Paragraph("Kilos Julio",fuente));
        cellJul.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellJul.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJul);

        PdfPCell cellAgo = new PdfPCell(new Paragraph("Kilos Agosto",fuente));
        cellAgo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellAgo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgo);

        PdfPCell cellSep = new PdfPCell(new Paragraph("Kilos Septiembre",fuente));
        cellSep.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellSep.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSep);

        PdfPCell cellOct = new PdfPCell(new Paragraph("Kilos Octubre",fuente));
        cellOct.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellOct.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOct);

        PdfPCell cellNov = new PdfPCell(new Paragraph("Kilos Noviembre",fuente));
        cellNov.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellNov.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNov);

        PdfPCell cellDic = new PdfPCell(new Paragraph("Kilos Diciembre",fuente));
        cellDic.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellDic.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDic);
        
        PdfPCell cellT = new PdfPCell(new Paragraph("TOTAL Familia",fuente));
        cellT.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellT.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellT);
        float sumaHorizontal = 0,sumaEnero = 0,sumaFeb = 0,sumaMar = 0,sumaAbr = 0,sumaMay = 0,sumaJun = 0,sumaJul = 0,sumaAgos = 0,sumaSep = 0,sumaOct = 0,sumaNov = 0,sumaDic = 0,sumT = 0;

         while(documentosKilos7.next()){
            PdfPCell celdaFAM = new PdfPCell(new Phrase(documentosKilos7.getString("FAMN87"),cuerpo));
            celdaFAM.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaFAM);

            PdfPCell celdaDES = new PdfPCell(new Phrase(documentosKilos7.getString("DESN87"),cuerpo));
            celdaDES.setHorizontalAlignment(Element.ALIGN_LEFT);
            tabla.addCell(celdaDES);

            DecimalFormat dfEne = new DecimalFormat("###,###,###.00");
            String cadenaEne = "";
            sumaEnero = sumaEnero + Float.parseFloat(documentosKilos7.getString("K01N87"));
            cadenaEne = String.valueOf(dfEne.format(Float.parseFloat(documentosKilos7.getString("K01N87"))));
            PdfPCell celdaEne = new PdfPCell(new Phrase(cadenaEne,cuerpo));
            celdaEne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaEne);

            DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
            String cadenaFeb = "";
            sumaFeb = sumaFeb + Float.parseFloat(documentosKilos7.getString("K02N87"));
            cadenaFeb = String.valueOf(dfFeb.format(Float.parseFloat(documentosKilos7.getString("K02N87"))));
            PdfPCell celdaFeb = new PdfPCell(new Phrase(cadenaFeb,cuerpo));
            celdaFeb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaFeb); 

            DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
            String cadenaMar = "";
            sumaMar = sumaMar + Float.parseFloat(documentosKilos7.getString("K03N87"));
            cadenaMar = String.valueOf(dfMar.format(Float.parseFloat(documentosKilos7.getString("K03N87"))));
            PdfPCell celdaMar = new PdfPCell(new Phrase(cadenaMar,cuerpo));
            celdaMar.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMar);

            DecimalFormat dfAbr = new DecimalFormat("###,###,###.00");
            String cadenaAbr = "";
            sumaAbr = sumaAbr + Float.parseFloat(documentosKilos7.getString("K04N87"));
            cadenaAbr = String.valueOf(dfAbr.format(Float.parseFloat(documentosKilos7.getString("K04N87"))));
            PdfPCell celdaAbr = new PdfPCell(new Phrase(cadenaAbr,cuerpo));
            celdaAbr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAbr);

            DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
            String cadenaMayo = "";
            sumaMay = sumaMay + Float.parseFloat(documentosKilos7.getString("K05N87"));
            cadenaMayo = String.valueOf(dfMayo.format(Float.parseFloat(documentosKilos7.getString("K05N87"))));
            PdfPCell celdaMayo = new PdfPCell(new Phrase(cadenaMayo,cuerpo));
            celdaMayo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaMayo);

            DecimalFormat dfJun = new DecimalFormat("###,###,###.00");
            String cadenaJun = "";
            sumaJun = sumaJun + Float.parseFloat(documentosKilos7.getString("K06N87"));
            cadenaJun = String.valueOf(dfJun.format(Float.parseFloat(documentosKilos7.getString("K06N87"))));
            PdfPCell celdaJun = new PdfPCell(new Phrase(cadenaJun,cuerpo));
            celdaJun.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJun);

            DecimalFormat dfJul = new DecimalFormat("###,###,###.00");
            String cadenaJul = "";
            sumaJul = sumaJul + Float.parseFloat(documentosKilos7.getString("K07N87"));
            cadenaJul = String.valueOf(dfJul.format(Float.parseFloat(documentosKilos7.getString("K07N87"))));
            PdfPCell celdaJul = new PdfPCell(new Phrase(cadenaJul,cuerpo));
            celdaJul.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaJul);

            DecimalFormat dfAgo = new DecimalFormat("###,###,###.00");
            String cadenaAgo = "";
            sumaAgos = sumaAgos + Float.parseFloat(documentosKilos7.getString("K08N87"));
            cadenaAgo = String.valueOf(dfAgo.format(Float.parseFloat(documentosKilos7.getString("K08N87"))));
            PdfPCell celdaAgo = new PdfPCell(new Phrase(cadenaAgo,cuerpo));
            celdaAgo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaAgo);

            DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
            String cadenaSep = "";
            sumaSep = sumaSep + Float.parseFloat(documentosKilos7.getString("K09N87"));
            cadenaSep = String.valueOf(dfSep.format(Float.parseFloat(documentosKilos7.getString("K09N87"))));
            PdfPCell celdaSep = new PdfPCell(new Phrase(cadenaSep,cuerpo));
            celdaSep.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaSep);

            DecimalFormat dfOct = new DecimalFormat("###,###,###.00");
            String cadenaOct = "";
            sumaOct = sumaOct + Float.parseFloat(documentosKilos7.getString("K10N87"));
            cadenaOct = String.valueOf(dfOct.format(Float.parseFloat(documentosKilos7.getString("K10N87"))));
            PdfPCell celdaOct = new PdfPCell(new Phrase(cadenaOct,cuerpo));
            celdaOct.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaOct);

            DecimalFormat dfNov = new DecimalFormat("###,###,###.00");
            String cadenaNov = "";
            sumaNov = sumaNov + Float.parseFloat(documentosKilos7.getString("K11N87"));
            cadenaNov = String.valueOf(dfNov.format(Float.parseFloat(documentosKilos7.getString("K11N87"))));
            PdfPCell celdaNov = new PdfPCell(new Phrase(cadenaNov,cuerpo));
            celdaNov.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaNov);

            DecimalFormat dfDic = new DecimalFormat("###,###,###.00");
            String cadenaDic = "";
            sumaDic = sumaDic + Float.parseFloat(documentosKilos7.getString("K12N87"));
            cadenaDic = String.valueOf(dfDic.format(Float.parseFloat(documentosKilos7.getString("K12N87"))));
            PdfPCell celdaDic = new PdfPCell(new Phrase(cadenaDic,cuerpo));
            celdaDic.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(celdaDic);
            
            DecimalFormat dfTot = new DecimalFormat("###,###,###.00");
            sumaHorizontal = sumaHorizontal + Float.parseFloat(documentosKilos7.getString("K01N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K02N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K03N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K04N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K05N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K06N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K07N87")) + 
                                              Float.parseFloat(documentosKilos7.getString("K08N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K09N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K10N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K11N87")) +
                                              Float.parseFloat(documentosKilos7.getString("K12N87"));
            sumT = sumT +sumaHorizontal;
            PdfPCell cell = new PdfPCell(new Phrase(dfTot.format(sumaHorizontal),cuerpo));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(cell);
            sumaHorizontal = 0;
        }    
        PdfPCell cell = new PdfPCell(new Phrase("TOTAL",fuente));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        tabla.addCell(cell);
        
        DecimalFormat df = new DecimalFormat("###,###,###.00");
        PdfPCell cellN = new PdfPCell(new Phrase(df.format(sumaEnero),fuente));
        cellN.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellN.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellN);
        
        DecimalFormat dfFeb = new DecimalFormat("###,###,###.00");
        PdfPCell cellFebr = new PdfPCell(new Phrase(dfFeb.format(sumaFeb),fuente));
        cellFebr.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellFebr.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellFebr);
        
        DecimalFormat dfMar = new DecimalFormat("###,###,###.00");
        PdfPCell cellMarz = new PdfPCell(new Phrase(dfMar.format(sumaMar),fuente));
        cellMarz.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMarz.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMarz);
        
        DecimalFormat dfAbri = new DecimalFormat("###,###,###.00");
        PdfPCell cellAbri = new PdfPCell(new Phrase(dfAbri.format(sumaAbr),fuente));
        cellAbri.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAbri.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAbri);
        
        DecimalFormat dfMayo = new DecimalFormat("###,###,###.00");
        PdfPCell cellMay = new PdfPCell(new Phrase(dfMayo.format(sumaMay),fuente));
        cellMay.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellMay.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellMay);
        
        DecimalFormat dfJunio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJunio = new PdfPCell(new Phrase(dfJunio.format(sumaJun),fuente));
        cellJunio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJunio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJunio);
        
        DecimalFormat dfJulio = new DecimalFormat("###,###,###.00");
        PdfPCell cellJulio = new PdfPCell(new Phrase(dfJulio.format(sumaJul),fuente));
        cellJulio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellJulio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellJulio);
        
        DecimalFormat dfAgos = new DecimalFormat("###,###,###.00");
        PdfPCell cellAgos = new PdfPCell(new Phrase(dfAgos.format(sumaAgos),fuente));
        cellAgos.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellAgos.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellAgos);
        
        DecimalFormat dfSep = new DecimalFormat("###,###,###.00");
        PdfPCell cellSept = new PdfPCell(new Phrase(dfSep.format(sumaSep),fuente));
        cellSept.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellSept.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellSept);
        
        DecimalFormat dfOct = new DecimalFormat("###,###,###.00");
        PdfPCell cellOctu = new PdfPCell(new Phrase(dfOct.format(sumaOct),fuente));
        cellOctu.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellOctu.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellOctu);
        
        DecimalFormat dfNov = new DecimalFormat("###,###,###.00");
        PdfPCell cellNovi = new PdfPCell(new Phrase(dfNov.format(sumaNov),fuente));
        cellNovi.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellNovi.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellNovi);
        
        DecimalFormat dfDic = new DecimalFormat("###,###,###.00");
        PdfPCell cellDici = new PdfPCell(new Phrase(dfDic.format(sumaDic),fuente));
        cellDici.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellDici.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellDici);
        
        DecimalFormat dfT = new DecimalFormat("###,###,###.00");
        PdfPCell cellTo = new PdfPCell(new Phrase(dfT.format(sumT),fuente));
        cellTo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTo.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cellTo);
        return tabla;
   }
    //////////////////////////////////////////////////////////////////////
   
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        try{
            int mes = 0;
            int Ano = Integer.parseInt(jTextField2.getText());
            if(jComboBox1.getSelectedItem().equals("Enero"))
                mes = 1;
            if(jComboBox1.getSelectedItem().equals("Febrero"))
                mes = 2;
            if(jComboBox1.getSelectedItem().equals("Marzo"))
                mes = 3;
            if(jComboBox1.getSelectedItem().equals("Abril"))
                mes = 4;
            if(jComboBox1.getSelectedItem().equals("Mayo"))
                mes = 5;
            if(jComboBox1.getSelectedItem().equals("Junio"))
                mes = 6;
             if(jComboBox1.getSelectedItem().equals("Julio"))
                mes = 7;
            if(jComboBox1.getSelectedItem().equals("Agosto"))
                mes = 8;
            if(jComboBox1.getSelectedItem().equals("Septiembre"))
                mes = 9;
            if(jComboBox1.getSelectedItem().equals("Octubre"))
                mes = 10;
            if(jComboBox1.getSelectedItem().equals("Noviembre"))
                mes = 11;
            if(jComboBox1.getSelectedItem().equals("Diciembre"))
                mes = 12;
            calendarioPDF obtenerFechas = new calendarioPDF();
            Calendar fecha = Calendar.getInstance();
            int anoA = fecha.get(Calendar.YEAR);
            int mesA = fecha.get(Calendar.MONTH);
            int diaA = fecha.get(Calendar.DAY_OF_MONTH);
            int hora = fecha.get(Calendar.HOUR_OF_DAY);
            int minuto = fecha.get(Calendar.MINUTE);
            int segundo = fecha.get(Calendar.SECOND);
            mesA = mesA + 1;
            int anoActual = obtenerFechas.anoActual();
            System.out.println(anoActual);
            
                //valida año (cambiar por variable cargada con año actual).
            if(Ano > anoActual)
                JOptionPane.showMessageDialog(null,"Error en el año");
                //procesa si no hay errores.
            else{
                int fechaInicialPower = obtenerFechas.fechaMinimaPower(Integer.toString(mes), jTextField2.getText());
                int fechaFinalPower = obtenerFechas.fechaMaximaPower(Integer.toString(mes), jTextField2.getText());
                //procesa
                sql consultaPower = new sql();
                sqlKilos consultaKilos = new sqlKilos();
                
                Document documento = new Document(PageSize.LETTER.rotate());
                
               
                documento.setMargins(-70, -70, 25, 20);
                
                ResultSet documentosPower7 = consultaPower.documentosPower(fechaInicialPower, fechaFinalPower,mes,Ano);
                ResultSet documentosKilos = consultaKilos.documentosKilos(fechaInicialPower, fechaFinalPower, mes, Ano);
                
                String path = "C:\\\\reportes\\\\"+anoA+"_"+mesA+"_"+diaA+"_"+hora+"_"+minuto+"_"+segundo+".pdf";
                String nombre = ""+anoA+"_"+mesA+"_"+diaA+"_"+hora+"_"+minuto+"_"+segundo+".pdf";
                FileOutputStream ficheroPdf; 
                try 
                {
                    File fichero = new File("C:/reportes");
                    if(fichero.exists()){
                        ficheroPdf = new FileOutputStream(path);
                        PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(400);
                    }
                    else{
                        fichero.mkdir();
                        ficheroPdf = new FileOutputStream(path);
                        PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(400);
                    }
                    
                    
                }
                catch (Exception ex) 
                {
                    System.out.println(ex.toString());
                }
                try{
                     if(mes == 01){
                            documento.open();
                            PdfPTable tabla = createTableEneroV(mes,documentosPower7);
                            documento.add(tabla);
                            tabla = createTableEneroK(mes,documentosKilos);                           
                            tabla.setSpacingBefore(30);
                            tabla.setSpacingAfter(30);  
                            documento.add(tabla);    
                            documento.close();
                            JOptionPane.showMessageDialog(null,"Se creó el pdf");
                            //envioCorreo.envio(path, nombre);
                            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                     }
                     else{
                         if(mes == 02){
                            documento.open();
                            PdfPTable tabla = createTableEFV(mes,documentosPower7);
                            documento.add(tabla);
                            tabla = createTableEFK(mes,documentosKilos);                           
                            tabla.setSpacingBefore(30);
                            tabla.setSpacingAfter(30);  
                            documento.add(tabla);    
                            documento.close();
                         
                            JOptionPane.showMessageDialog(null,"Se creó el pdf");
                            //envioCorreo.envio(path, nombre);
                            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                         }   
                         else{
                             if(mes == 03){
                                documento.open();
                                PdfPTable tabla = createTableEFMV(mes,documentosPower7);
                                documento.add(tabla);
                                tabla = createTableEFMK(mes,documentosKilos);                           
                                tabla.setSpacingBefore(30);
                                tabla.setSpacingAfter(30);  
                                documento.add(tabla);    
                                documento.close();
                                
                                JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                //envioCorreo.envio(path, nombre);
                                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                             }
                             else{
                                 if(mes == 04){
                                    documento.open();
                                    PdfPTable tabla = createTableEFMAV(mes,documentosPower7);
                                    documento.add(tabla);
                                    tabla = createTableEFMAK(mes,documentosKilos);                           
                                    tabla.setSpacingBefore(30);
                                    tabla.setSpacingAfter(30);  
                                    documento.add(tabla);    
                                    documento.close();
                                    
                                    JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                    //envioCorreo.envio(path, nombre);
                                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                 }
                                 else{
                                     if(mes == 05){
                                        documento.open();
                                        PdfPTable tabla = createTableEFMAMV(mes,documentosPower7);
                                        documento.add(tabla);
                                        tabla = createTableEFMAMK(mes,documentosKilos);                           
                                        tabla.setSpacingBefore(30);
                                        tabla.setSpacingAfter(30);  
                                        documento.add(tabla);    
                                        documento.close();
                                        
                                        JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                        //envioCorreo.envio(path, nombre);
                                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                     }
                                     else{
                                         if(mes == 06){
                                            documento.open();
                                            PdfPTable tabla = createTableEFMAMJV(mes,documentosPower7);
                                            documento.add(tabla);
                                            tabla = createTableEFMAMJK(mes,documentosKilos);                           
                                            tabla.setSpacingBefore(30);
                                            tabla.setSpacingAfter(30);  
                                            documento.add(tabla);    
                                            documento.close();
                                            
                                            JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                            //envioCorreo.envio(path, nombre);
                                            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                         }
                                         else{
                                             if(mes == 07){
                                                documento.open();
                                                PdfPTable tabla = createTableEFMAMJJV(mes,documentosPower7);
                                                documento.add(tabla);
                                                tabla = createTableEFMAMJJK(mes,documentosKilos);                           
                                                tabla.setSpacingBefore(30);
                                                tabla.setSpacingAfter(30);  
                                                documento.add(tabla);    
                                                documento.close();
                                                
                                                JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                                //envioCorreo.envio(path, nombre);
                                                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                             }
                                             else{
                                                 if(mes == 8){
                                                    documento.open();
                                                    PdfPTable tabla = createTableEFMAMJJAV(mes,documentosPower7);
                                                    documento.add(tabla);
                                                    tabla = createTableEFMAMJJAK(mes,documentosKilos);                           
                                                    tabla.setSpacingBefore(30);
                                                    tabla.setSpacingAfter(30);  
                                                    documento.add(tabla);    
                                                    documento.close();
                                                    
                                                    JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                                    //envioCorreo.envio(path, nombre);
                                                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                                 }
                                                 else{
                                                     if(mes == 9){
                                                        documento.open();
                                                        PdfPTable tabla = createTableEFMAMJJASV(mes,documentosPower7);
                                                        documento.add(tabla);
                                                        tabla = createTableEFMAMJJASK(mes,documentosKilos);                           
                                                        tabla.setSpacingBefore(30);
                                                        tabla.setSpacingAfter(30);  
                                                        documento.add(tabla);    
                                                        documento.close();
                                                       
                                                        JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                                        //envioCorreo.envio(path, nombre);
                                                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                                     }
                                                     else{
                                                         if(mes == 10){
                                                            documento.setMargins(-90, -90, 25, 20);
                                                            documento.open();
                                                            PdfPTable tabla = createTableEFMAMJJASOV(mes,documentosPower7);
                                                            documento.add(tabla);
                                                            tabla = createTableEFMAMJJASOK(mes,documentosKilos);                           
                                                            tabla.setSpacingBefore(30);
                                                            tabla.setSpacingAfter(30);  
                                                            documento.add(tabla);    
                                                            documento.close();
                                                            
                                                            JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                                            //envioCorreo.envio(path, nombre);
                                                            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                                         }
                                                         else{
                                                             if(mes == 11){
                                                                documento.setMargins(-97, -97, 25, 20);
                                                                documento.open();
                                                                PdfPTable tabla = createTableEFMAMJJASONV(mes,documentosPower7);
                                                                documento.add(tabla);
                                                                tabla = createTableEFMAMJJASONK(mes,documentosKilos);                           
                                                                tabla.setSpacingBefore(30);
                                                                tabla.setSpacingAfter(30);  
                                                                documento.add(tabla);    
                                                                documento.close();
                                                                JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                                                //envioCorreo.envio(path, nombre);
                                                                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                                             }
                                                             else{
                                                                 if(mes == 12){
                                                                    documento.setMargins(-97, -97, 25, 20); 
                                                                    documento.open();
                                                                    PdfPTable tabla = createTableEFMAMJJASONDV(mes,documentosPower7);
                                                                    documento.add(tabla);
                                                                    tabla = createTableEFMAMJJASONDK(mes,documentosKilos);                           
                                                                    tabla.setSpacingBefore(40);
                                                                    tabla.setSpacingAfter(40);  
                                                                    documento.add(tabla);    
                                                                    documento.close();
                                                                    JOptionPane.showMessageDialog(null,"Se creó el pdf");
                                                                    //envioCorreo.envio(path, nombre);
                                                                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+path);
                                                                 }
                                                             }
                                                         }
                                                     }
                                                         
                                                 }
                                             }
                                         }
                                     }
                                 }
                             }
                         }
                           
                     }
                
            
                }catch(Exception ex){
                    System.out.println(ex.toString());
                }

            } 
        }
        catch(Exception e){
            System.out.println("Error");
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;

    
}
