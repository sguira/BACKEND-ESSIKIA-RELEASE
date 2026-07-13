package com.formation.demo.email;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImp implements interfaceSendMail {

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.mail.from.name}")
    private String senderName;

    @Override
    public String sendSimpleMessage(BodyEmail details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderName + " <" + sender + ">");
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMessage());
            mailMessage.setSubject(details.getBody());
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        }
    }

    public String sendSimpleMessage(BodyEmail details, String from) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMessage());
            mailMessage.setSubject(details.getBody());
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        }
    }

    public String sendHtlmlMail(BodyEmail email, String htmlBody) {
        MimeMessage minMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(minMessage, true, "UTF-8");
            mimeMessageHelper.setTo(email.recipient);
            mimeMessageHelper.setFrom(sender, senderName);
            mimeMessageHelper.setSubject(email.body);
            mimeMessageHelper.setText(htmlBody, true);
            ClassPathResource logo = new ClassPathResource("assets/essikia2.png");
            if (logo.exists()) {
                mimeMessageHelper.addInline("essikia-logo", logo, "image/png");
            }
            javaMailSender.send(minMessage);
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        }
    }

    public String sendHtmlMailWithCc(BodyEmail email, String htmlBody, List<String> cc) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(sender, senderName);
            helper.setTo(email.getRecipient());
            helper.setSubject(email.getBody());
            helper.setText(htmlBody, true);
            ClassPathResource logo = new ClassPathResource("assets/essikia2.png");
            if (logo.exists()) {
                helper.addInline("essikia-logo", logo, "image/png");
            }
            if (cc != null && !cc.isEmpty()) {
                helper.setCc(cc.toArray(new String[0]));
            }
            javaMailSender.send(mimeMessage);
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        }
    }

    @Override
    public String sendEmailWithAttachment(BodyEmail details) {
        jakarta.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender, senderName);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMessage());
            mimeMessageHelper.setSubject(details.getBody());
            FileSystemResource file = new FileSystemResource(new File(details.getAttachement()));
            mimeMessageHelper.addAttachment(file.getFilename(), file);
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while sending mail!!!";
        }
    }
}
