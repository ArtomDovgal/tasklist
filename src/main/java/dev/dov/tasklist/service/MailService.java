package dev.dov.tasklist.service;

import dev.dov.tasklist.domain.MailType;
import dev.dov.tasklist.domain.user.User;

import java.util.Properties;

public interface MailService {

    void sendEmail(User user, MailType type, Properties params);


}
