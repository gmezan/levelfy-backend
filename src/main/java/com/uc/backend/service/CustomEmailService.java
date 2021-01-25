package com.uc.backend.service;

import com.uc.backend.dto.PaymentDto;
import com.uc.backend.entity.Service;
import com.uc.backend.entity.User;
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

import static com.uc.backend.util.CustomConstants.ADMINS;
import static com.uc.backend.util.CustomConstants.METODOS_DE_PAGO;

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

    public void sendAsesPerRegisterMail(User user, PaymentDto paymentDto) throws IOException, MessagingException {
        String mensaje =
                "<p>Hola se ha registrado el pago con los siguientes datos:<br/>" + "Usuario: <b>" + user.getName()
                        + "</b> <br/>Mensaje o número de operación: <b>" + paymentDto.getMessage() + "</b><br/>Fecha del pago: <b>" + paymentDto.getDate() +
                        "</b><br/>Monto: <b>" + paymentDto.getAmount() + "</b> a través de <b>" + METODOS_DE_PAGO[paymentDto.getMethod()] + "</b><br/><br/> Verficaremos los datos y procederemos a activar el curso " +
                        "<br/> Si confundiste alguno de los datos vuelve a registrar el pago.<br/> " +
                        "cupón usado:"+ paymentDto.getCoupon() + "</p>";
            sendHtmlMail(ADMINS, "Inscripción - Asesoría Personalizada","Registro de pago", mensaje);
            sendHtmlMail(user.getEmail(), "Inscripción - Asesoría Personalizada","Registro de pago", mensaje);

    }
    public void sendMailToUsersMensaje(List<User> listaUsers, Service service, String mensaje) throws IOException, MessagingException {
        mensaje= mensaje+ "Tomar en cuenta el siguiente enlace de zoom para su clase:" + service.getCourse().getName() +" de " + service.getCourse().getUnivNameStr();
        String[] correos = new String[listaUsers.size()];
        for (int i = 0; i< listaUsers.size(); i++){
Array.set(correos,i, listaUsers.get(i).getEmail());
             }
        sendHtmlMail(correos, "University Class - Asesoría "+ service.getCourse().getName(),"Información importante acerca de la asesoría", mensaje);
    sendHtmlMail(ADMINS, "University Class - Asesoría "+ service.getCourse().getName(),"Información importante acerca de la asesoría", mensaje);

    }


}
