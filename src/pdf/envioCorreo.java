package pdf;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Daniel
 */

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

//clase para enviar correos, que recibe como parametros la path y el nombre del archivo
public class envioCorreo {
    
    public static void envio(String path,String nombre){
        try
        {
          // se obtiene el objeto Session. La configuración es para
          // una cuenta de gmail.
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.tie.cl");//se necesita el servidor de correo saliente SMPT de hochschild(tie)
            //informatica@mhochschild.cl
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "25");
            props.setProperty("mail.smtp.user", "informatica@mhochschild.cl");
            props.setProperty("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props, null);
            // session.setDebug(true);

            // Se compone la parte del texto
            BodyPart texto = new MimeBodyPart();
            texto.setText("Adjunto reporte de ventas y kilos año 2014 para todo el territorio");

            // Se compone el adjunto con la imagen
            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(
                new DataHandler(new FileDataSource(path)));
            adjunto.setFileName(nombre);

            // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            multiParte.addBodyPart(adjunto);

            // Se compone el correo, dando to, from, subject y el
            // contenido.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("informatica@mhochschild.cl"));
            
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("daniel.abrilot@gmail.com, mixed_a.lo.cubano@hotmail.com"));
            
            
            //message.addRecipient(
            //Message.RecipientType.TO,
            //new InternetAddress("daniel.abrilot@gmail.com","mixed_a.lo.cubano@hotmail.com"));
            
            message.setSubject("Reporte Hochschild para alertas");
            message.setContent(multiParte);

            // Se envia el correo.
            Transport t = session.getTransport("smtp");
            t.connect("informatica@mhochschild.cl", "****");
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            JOptionPane.showMessageDialog(null,"El archivo fue enviado al correo");
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "El archivo no fue enviado al correo!","!",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
}
