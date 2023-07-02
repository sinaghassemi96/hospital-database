package com.example.hospitaldatabase.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageComponent {
    private final MessageSource messageSource;

    @Autowired
    public MessageComponent(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String s) {
        return this.messageSource.getMessage(s, null, Locale.ENGLISH);
    }

    public String getMessage(String s, Object[] objects, String s1) {
        return this.messageSource.getMessage(s, objects, s1, Locale.ENGLISH);
    }

    public String getMessage(String s, Object[] objects) {
        return messageSource.getMessage(s, objects, Locale.ENGLISH);
    }

    public String getMessage(MessageSourceResolvable messageSourceResolvable) {
        return this.messageSource.getMessage(messageSourceResolvable, Locale.ENGLISH);
    }
}
