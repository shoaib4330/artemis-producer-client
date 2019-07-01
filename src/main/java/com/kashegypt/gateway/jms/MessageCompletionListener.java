package com.kashegypt.gateway.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.CompletionListener;
import javax.jms.Message;

public class MessageCompletionListener implements CompletionListener {
    private final Logger logger = LoggerFactory.getLogger(MessageCompletionListener.class);

    @Override
    public void onCompletion(Message message) {
        logger.info("JmsMessage onCompletion",message.toString());
    }

    @Override
    public void onException(Message message, Exception e) {
        logger.error("Error sending message", e.getMessage(), message.toString());
    }
}
