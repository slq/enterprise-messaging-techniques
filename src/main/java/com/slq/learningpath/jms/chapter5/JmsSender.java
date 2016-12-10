package com.slq.learningpath.jms.chapter5;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JmsSender {

    private static final Logger logger = LoggerFactory.getLogger(JmsSender.class);

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = factory.createConnection();
        connection.start();

        boolean notTransacted = false;
        Session session = connection.createSession(notTransacted, Session.AUTO_ACKNOWLEDGE);

        // will be created if does not exist
        Queue queue = session.createQueue("EM_TRADE.Q");
        MessageProducer sender = session.createProducer(queue);

        TextMessage message = session.createTextMessage("BUY AAPL 1000 SHARES");
        sender.send(message);
        logger.info("Message is sent");

        // will close session internally
        connection.close();
    }
}


