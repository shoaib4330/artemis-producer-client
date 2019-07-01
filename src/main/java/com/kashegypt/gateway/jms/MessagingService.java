package com.kashegypt.gateway.jms;


import com.kashegypt.gateway.common.type.NotificationMessageType;
import com.kashegypt.gateway.jms.annotations.CustomJMS;
import com.kashegypt.gateway.jms.annotations.MailQueue;
import com.kashegypt.gateway.jms.annotations.NotificationQueue;
import com.kashegypt.gateway.jms.annotations.SMSQueue;
import com.kashegypt.gateway.jms.dto.MessageRequest;
import com.kashegypt.gateway.jms.dto.SMSMessageRequestDTO;
import com.kashegypt.gateway.jms.dto.UnicastNotificationMessageDTO;
import com.kashegypt.gateway.jms.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.*;
import java.util.List;

@Stateless
@LocalBean
public class MessagingService {
    private final static Logger logger = LoggerFactory.getLogger(MessagingService.class);

    @Inject
    @CustomJMS
    private JMSContext jmsContext;

    @Inject
    @NotificationQueue
    private Queue notificationQueue;

    @Inject
    @MailQueue
    private Queue mailQueue;

    @Inject
    @SMSQueue
    private Queue smsQueue;

    @Inject
    private MessageCompletionListener messageCompletionListener;

    /**
     * Sends a message to the JMS queue.
     * Does not run as part of the transaction
     * @param message the contents of the message.
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void sendMessage(MessageRequest message) {
        switch (message.getDestination()) {
            case NOTIFICATIONS: {
                jmsContext.createProducer().setJMSType(message.getJmsType()).setAsync(messageCompletionListener).send(notificationQueue, jmsContext.createTextMessage(JsonUtil.toJsonString(message.getMessage())));
                break;
            }
            case MAIL: {
                jmsContext.createProducer().setAsync(messageCompletionListener).send(mailQueue, jmsContext.createTextMessage(JsonUtil.toJsonString(message.getMessage())));
                break;
            }
            case SMS: {
                jmsContext.createProducer().setAsync(messageCompletionListener).send(smsQueue, jmsContext.createTextMessage(JsonUtil.toJsonString(message.getMessage())));
                break;
            }
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void sendUnicastBulkNotifications(List<UnicastNotificationMessageDTO> messagesList) {
        JMSProducer producer = jmsContext.createProducer().setJMSType(String.valueOf(NotificationMessageType.UNICAST.getValue()));
        messagesList.stream().forEach(message -> producer.send(notificationQueue, JsonUtil.toJsonString(message)));
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void sendBulkSms(List<SMSMessageRequestDTO> messagesList) {
        JMSProducer producer = jmsContext.createProducer();
        messagesList.stream().forEach(message -> producer.send(smsQueue, JsonUtil.toJsonString(message)));
    }


    @PreDestroy
    private void preDestroy() {
        jmsContext.close();
    }
}
