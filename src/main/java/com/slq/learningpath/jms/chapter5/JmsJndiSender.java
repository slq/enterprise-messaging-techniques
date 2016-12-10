package com.slq.learningpath.jms.chapter5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsJndiSender {

    private static final Logger logger = LoggerFactory.getLogger(JmsJndiSender.class);

    public static void main(String[] args) throws JMSException, NamingException {

        Context ctx = new InitialContext();
        Connection connection = ((ConnectionFactory) ctx.lookup("ConnectionFactory")).createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue) ctx.lookup("EM_TRADE.Q");

        MessageProducer sender = session.createProducer(queue);

        TextMessage message = session.createTextMessage("BUY AAPL 2000 SHARES");
        message.setStringProperty("TraderName", "SLQ");
        sender.send(message);
        logger.info("Message is sent");

        // will close session internally
        connection.close();
    }
}



