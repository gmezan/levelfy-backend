package com.uc.backend.service;

import com.uc.backend.dto.RegistroPagoDto;
import com.uc.backend.entity.Clase;
import com.uc.backend.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

import static com.uc.backend.utils.CustomConstants.ADMINS;
import static com.uc.backend.utils.CustomConstants.METODOS_DE_PAGO;

@Component
public class CustomEmailService {

    @Autowired
    public JavaMailSender javaMailSender;

    public void sendSimpleMessage(
            String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);

    }

    public void sendSimpleMail(String to, String subject, String title, String message) throws MessagingException, IOException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("<html> <body>" +
                "<h1>"+title+"</h1> " +
                "<p>"+message+" </p>" +
                "</body></html>", true);
        //helper.setText(message);
        javaMailSender.send(msg);
    }

    public void sendHtmlMail(String to, String subject, String title, String message) throws MessagingException, IOException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("<html> <body>" +
                "<h1>"+title+"</h1> "
                +message+
                "</body></html>", true);
        //helper.setText(message);
        javaMailSender.send(msg);
    }

    public void sendHtmlMail(String[] to, String subject, String title, String message) throws MessagingException, IOException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("<html> <body>" +
                "<h1>"+title+"</h1> "
                +message+
                "</body></html>", true);
        //helper.setText(message);
        javaMailSender.send(msg);
    }

    public void sendAsesPerRegisterMail(Usuario usuario, RegistroPagoDto registroPagoDto) throws IOException, MessagingException {
        String mensaje =
                "<p>Hola se ha registrado el pago con los siguientes datos:<br/>" + "Usuario: <b>" + usuario.getNombre()
                        + "</b> <br/>Mensaje o número de operación: <b>" + registroPagoDto.getMensaje() + "</b><br/>Fecha del pago: <b>" + registroPagoDto.getFecha() +
                        "</b><br/>Monto: <b>" + registroPagoDto.getMonto() + "</b> a través de <b>" + METODOS_DE_PAGO[registroPagoDto.getMetodo()] + "</b><br/><br/> Verficaremos los datos y procederemos a activar el curso " +
                        "<br/> Si confundiste alguno de los datos vuelve a registrar el pago.<br/> " +
                        "cupón usado:"+registroPagoDto.getCupon() + "</p>";
            sendHtmlMail(ADMINS, "Inscripción - Asesoría Personalizada","Registro de pago", mensaje);
            sendHtmlMail(usuario.getCorreo(), "Inscripción - Asesoría Personalizada","Registro de pago", mensaje);

    }
    public void sendMailToUsersMensaje(List<Usuario> listaUsuarios, Clase clase , String mensaje) throws IOException, MessagingException {
        mensaje= mensaje+ "Tomar en cuenta el siguiente enlace de zoom para su clase:" + clase.getCurso().getNombre() +" de " +clase.getCurso().getUnivNameStr();
        String[] correos = new String[listaUsuarios.size()];
        for (int i = 0; i<listaUsuarios.size(); i++){
Array.set(correos,i,listaUsuarios.get(i).getCorreo());
             }
        sendHtmlMail(correos, "University Class - Asesoría "+clase.getCurso().getNombre(),"Información importante acerca de la asesoría", mensaje);
    sendHtmlMail(ADMINS, "University Class - Asesoría "+clase.getCurso().getNombre(),"Información importante acerca de la asesoría", mensaje);

    }


}
