package com.slq.learningpath.jms.chapter5;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JmsReceiver {

    private static final Logger logger = LoggerFactory.getLogger(JmsReceiver.class);

    public static void main(String[] args) throws JMSException {
        Connection connection = new ActiveMQConnectionFactory("tcp://localhost:61616").createConnection();
        connection.start();

        boolean notTransacted = false;
        Session session = connection.createSession(notTransacted, Session.AUTO_ACKNOWLEDGE);

        // will be created if does not exist
        Queue queue = session.createQueue("EM_TRADE.Q");
        MessageConsumer receiver = session.createConsumer(queue);

        // blocking wait
        TextMessage msg = (TextMessage) receiver.receive();

        // message if exists or null immediately
//        TextMessage msg = (TextMessage) receiver.receiveNoWait();

        // wait 10 sec
//        TextMessage msg = (TextMessage) receiver.receive(10000);

        String traderName = msg.getStringProperty("TraderName");

        logger.info("{}, trader: {}", msg.getText(), traderName);

        // will close session internally
        connection.close();
    }
}


