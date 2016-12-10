package com.slq.learningpath.jms.chapter5;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JmsAsyncReceiver implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(JmsAsyncReceiver.class);

    public JmsAsyncReceiver() {
        try {
            Connection connection = new ActiveMQConnectionFactory("tcp://localhost:61616").createConnection();
            connection.start();
            boolean notTransacted = false;
            Session session = connection.createSession(notTransacted, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("EM_TRADE.Q");
            MessageConsumer receiver = session.createConsumer(queue);

            receiver.setMessageListener(this);
            logger.info("waiting for messages");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage msg = (TextMessage) message;
            logger.info(msg.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(JmsAsyncReceiver::new).start();
    }
}


