package com.kashegypt.gateway.jms;

import com.kashegypt.gateway.jms.annotations.CustomJMS;
import com.kashegypt.gateway.jms.annotations.MailQueue;
import com.kashegypt.gateway.jms.annotations.NotificationQueue;
import com.kashegypt.gateway.jms.annotations.SMSQueue;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@Singleton
public class MessagingConfiguration {

    private static Logger logger = LoggerFactory.getLogger(MessagingConfiguration.class);

    @Inject
    @ConfigProperty(name = "connectionFactory.ConnectionFactory")
    private String messageBrokerUrl;

    @Inject
    @ConfigProperty(name = "java.naming.factory.initial")
    private String initialContextFactoryName;

    @Inject
    @ConfigProperty(name = "jms.address.notification")
    private String notificationAddress;

    @Inject
    @ConfigProperty(name = "jms.address.mail")
    private String mailAddress;

    @Inject
    @ConfigProperty(name = "jms.address.sms")
    private String smsAddress;

    @Inject
    @ConfigProperty(name = "jms.queue.name.notification")
    private String notificationQueueName;

    @Inject
    @ConfigProperty(name = "jms.queue.name.mail")
    private String mailQueueName;

    @Inject
    @ConfigProperty(name = "jms.queue.name.sms")
    private String smsQueueName;

    private InitialContext jndiContext;

    @Produces
    private ConnectionFactory connectionFactory;

    @Produces @NotificationQueue
    private Queue notificationQueue;

    @Produces @MailQueue
    private Queue mailQueue;

    @Produces @SMSQueue
    private Queue smsQueue;

    @PostConstruct
    private void init() {
        Properties props = buildJNDIContext();
        try {
            jndiContext = new InitialContext(props);
            connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
            notificationQueue = (Queue) jndiContext.lookup("queues/" + notificationQueueName);
            mailQueue = (Queue) jndiContext.lookup("queues/" + mailQueueName);
            smsQueue = (Queue) jndiContext.lookup("queues/" + smsQueueName);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @RequestScoped
    @Produces
    @CustomJMS
    public JMSContext jmsContext(){
        if(connectionFactory==null){
            throw new RuntimeException("Cannot connect to provider");
        }
        logger.info("JMSContext--", "@CustomJMS producer method called");
        return connectionFactory.createContext();
    }

    private Properties buildJNDIContext() {
        Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", initialContextFactoryName);
        properties.setProperty("connectionFactory.ConnectionFactory", messageBrokerUrl);
        properties.setProperty("queue." + addressProperty(notificationAddress), notificationQueueName);
        properties.setProperty("queue." + addressProperty(mailAddress), mailQueueName);
        properties.setProperty("queue." + addressProperty(smsAddress), smsQueueName);
        return properties;
    }

    private String addressProperty(String name){
        /**
         * queues/notificationAddress
         */
        return "queues/" + name;
    }
}
