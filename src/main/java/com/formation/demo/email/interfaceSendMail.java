package com.formation.demo.email;

public interface interfaceSendMail {

    public String sendSimpleMessage(BodyEmail email);

    public String sendEmailWithAttachment(BodyEmail email);
}
