package dev.dov.tasklist.service.impl;

import dev.dov.tasklist.domain.MailType;
import dev.dov.tasklist.domain.user.User;
import dev.dov.tasklist.service.MailService;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.MailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MailService {

    private final Configuration configuration;
    private final JavaMailSender emailSender;

    @Override
    public void sendEmail(User user, MailType type, Properties params) {

    switch (type){
        case REGISTRATION -> sendRegistrationEmail(user, params);
        case REMINDER -> sendReminderEmail(user, params);
        default -> {}
    }
    }

    @SneakyThrows
    private void  sendRegistrationEmail(User user, Properties properties) {
        MimeMessage mimeMailMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, false, "UTF-8");

        helper.setSubject("Thank you for registration, " + user.getName());
        helper.setTo(user.getUsername());
        String emailContent = getRegistrationEmailContent(user, properties);
        helper.setText(emailContent, true);
        emailSender.send(mimeMailMessage);
    }

    @SneakyThrows
    private void  sendReminderEmail(User user, Properties properties) {
        MimeMessage mimeMailMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, false, "UTF-8");

        helper.setSubject("You have task to do in 1 hour, " + user.getName());
        helper.setTo(user.getUsername());
        String emailContent = getReminderEmailContent(user, properties);
        helper.setText(emailContent, true);
        emailSender.send(mimeMailMessage);
    }

    @SneakyThrows
    private String getRegistrationEmailContent(User user, Properties properties) {

        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        configuration.getTemplate("register.ftlh")
                .process(model, writer);

        return writer.getBuffer().toString();

    }

    @SneakyThrows
    private String getReminderEmailContent(User user, Properties properties) {

        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("title", properties.getProperty("task.title"));
        model.put("description", properties.getProperty("task.description"));
        configuration.getTemplate("reminder.ftlh")
                .process(model, writer);

        return writer.getBuffer().toString();

    }

}
