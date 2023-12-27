package com.ark.security.service.user;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ark.security.service.EmailDetailsService;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EmailDetailsService.class})
@ExtendWith(SpringExtension.class)
class EmailDetailsServiceTest {
    @Autowired
    private EmailDetailsService emailDetailsService;

    @MockBean
    private JavaMailSender javaMailSender;

    /**
     * Method under test: {@link EmailDetailsService#sendMail(String, String)}
     */
    @Test
    void testSendMail() throws MessagingException, UnsupportedEncodingException, MailException {
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        emailDetailsService.sendMail("Recipient", "Link");
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }

    /**
     * Method under test: {@link EmailDetailsService#sendOrderInformationEmail(String, String)}
     */
    @Test
    void testSendOrderInformationEmail() throws MessagingException, UnsupportedEncodingException, MailException {
        doNothing().when(javaMailSender).send(Mockito.<MimeMessage>any());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        emailDetailsService.sendOrderInformationEmail("Recipient", "Template");
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(Mockito.<MimeMessage>any());
    }
}


