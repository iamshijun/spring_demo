package javax.jms.tutorial;
/*
 *
 * Copyright 2002 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 * 
 */
import javax.jms.*;
import javax.naming.*;

public class AckEquivExample {
    final String  CONTROL_QUEUE = "controlQueue";
    final String  queueName = "jms/Queue";
    final String  topicName = "jms/Topic";
    final String  conFacName = "DurableTopicCF";

    /**
     * The SynchSender class creates a session in
     * CLIENT_ACKNOWLEDGE mode and sends a message.
     */
    public class SynchSender extends Thread {
        
        /**
         * Runs the thread.
         */
        public void run() {
            QueueConnectionFactory queueConnectionFactory = null;
            QueueConnection        queueConnection = null;
            QueueSession           queueSession = null;
            Queue                  queue = null;
            QueueSender            queueSender = null;
            final String           MSG_TEXT = 
              new String("Here is a client-acknowledge message");
            TextMessage             message = null;

            try {
                queueConnectionFactory = 
                    SampleUtilities.getQueueConnectionFactory();
                queueConnection = 
                  queueConnectionFactory.createQueueConnection();
                queueSession = 
                    queueConnection.createQueueSession(false, 
                        Session.CLIENT_ACKNOWLEDGE);
                queue = SampleUtilities.getQueue(queueName, 
                    queueSession);
            } catch (Exception e) {
                System.err.println("Connection problem: " + 
                    e.toString());
                if (queueConnection != null) {
                    try {
                        queueConnection.close();
                    } catch (JMSException ee) {}
                }
                System.exit(1);
            }

            /*
             * Create client-acknowledge sender.
             * Create and send message.
             */
            try {
                System.out.println("  SENDER: Created " +
                    "client-acknowledge session");
                queueSender = queueSession.createSender(queue);
                message = queueSession.createTextMessage();
                message.setText(MSG_TEXT);
                System.out.println("  SENDER: Sending " +
                    "message: " + message.getText());
                queueSender.send(message);
            } catch (JMSException e) {
                System.err.println("Exception occurred: " + 
                    e.toString());
            } finally {
                if (queueConnection != null) {
                    try {
                        queueConnection.close();
                    } catch (JMSException e) {}
                }
            }
        }
    }
    
    /**
     * The SynchReceiver class creates a session in
     * CLIENT_ACKNOWLEDGE mode and receives the message sent by
     * the SynchSender class.
     */
    public class SynchReceiver extends Thread {

        /**
         * Runs the thread.
         */
        public void run() {
            QueueConnectionFactory queueConnectionFactory = null;
            QueueConnection        queueConnection = null;
            QueueSession           queueSession = null;
            Queue                  queue = null;
            QueueReceiver          queueReceiver = null;
            TextMessage            message = null;

            try {
                queueConnectionFactory = 
                    SampleUtilities.getQueueConnectionFactory();
                queueConnection = 
                  queueConnectionFactory.createQueueConnection();
                queueSession = 
                    queueConnection.createQueueSession(false, 
                        Session.CLIENT_ACKNOWLEDGE);
                queue = 
                    SampleUtilities.getQueue(queueName, 
                        queueSession);
            } catch (Exception e) {
                System.err.println("Connection problem: " + 
                    e.toString());
                if (queueConnection != null) {
                    try {
                        queueConnection.close();
                    } catch (JMSException ee) {}
                }
                System.exit(1);
            }
   	
            /*
             * Create client-acknowledge receiver.
             * Receive message and process it.
             * Acknowledge message.
             */
             try {
                System.out.println("  RECEIVER: Created " +
                    "client-acknowledge session");
                queueReceiver = 
                    queueSession.createReceiver(queue);
                queueConnection.start();
                message = (TextMessage) queueReceiver.receive();
                System.out.println("  RECEIVER: Processing " +
                    "message: " + message.getText());
                System.out.println("  RECEIVER: Now I'll " +
                    "acknowledge the message");
                message.acknowledge();
            } catch (JMSException e) {
                System.err.println("Exception occurred: " + 
                    e.toString());
            } finally {
                if (queueConnection != null) {
                    try {
                        queueConnection.close();
                    } catch (JMSException e) {}
                }
            }
        }  	    
    }
    
    /**
     * The AsynchSubscriber class creates a session in 
     * AUTO_ACKNOWLEDGE mode and fetches several messages from a
     * topic asynchronously, using a message listener,
     * TextListener.
     * 
     * Each message is acknowledged after the onMessage method 
     * completes.
     */
    public class AsynchSubscriber extends Thread {

        /**
         * The TextListener class implements the MessageListener
         * interface by defining an onMessage method for the 
         * AsynchSubscriber class.
         */
        private class TextListener implements MessageListener {
            final SampleUtilities.DoneLatch  monitor =
                new SampleUtilities.DoneLatch();

            /**
             * Casts the message to a TextMessage and displays 
             * its text. A non-text message is interpreted as the
             * end of the message stream, and the message
             * listener sets its monitor state to all done
             * processing messages.
             *
             * @param message    the incoming message
             */
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    TextMessage  msg = (TextMessage) message;
                    
                    try {
                        System.out.println("SUBSCRIBER: " +
                            "Processing message: " +
                            msg.getText());
                    } catch (JMSException e) {
                        System.err.println("Exception in " +
                            "onMessage(): " + e.toString());
                    } 
                } else {
                    monitor.allDone();
                }
            }
        }

        /**
         * Runs the thread.
         */
        public void run() {
            Context                jndiContext = null;
            TopicConnectionFactory topicConnectionFactory = null;
            TopicConnection        topicConnection = null;
            TopicSession           topicSession = null;
            Topic                  topic = null;
            TopicSubscriber        topicSubscriber = null;
            TextListener           topicListener = null;

            /* 
             * Create a JNDI API InitialContext object if none
             * exists yet.
             */
            try {
                jndiContext = new InitialContext();
            } catch (NamingException e) {
                System.err.println("Could not create JNDI API " +
                    "context: " + e.toString());
                System.exit(1);
            }

            /* 
             * Look up connection factory and topic.  If either
             * does not exist, exit.
             */
            try {
                topicConnectionFactory = (TopicConnectionFactory)
                    jndiContext.lookup(conFacName);
                topicConnection = 
                  topicConnectionFactory.createTopicConnection();
                topicSession = 
                    topicConnection.createTopicSession(false, 
                        Session.AUTO_ACKNOWLEDGE);
                System.out.println("SUBSCRIBER: Created " +
                    "auto-acknowledge session");
                topic = SampleUtilities.getTopic(topicName, 
                    topicSession);
            } catch (Exception e) {
                System.err.println("Connection problem: " + 
                    e.toString());
                if (topicConnection != null) {
                    try {
                        topicConnection.close();
                    } catch (JMSException ee) {}
                }
                System.exit(1);
            }
            
            /*
             * Create auto-acknowledge subscriber.
             * Register message listener (TextListener).
             * Start message delivery.
             * Send synchronize message to publisher, then wait
             *   till all messages have arrived.
             * Listener displays the messages obtained.
             */
            try {
                topicSubscriber = 
                    topicSession.createDurableSubscriber(topic,
                        "AckSub");
                topicListener = new TextListener();
               topicSubscriber.setMessageListener(topicListener);
                topicConnection.start();

                // Let publisher know that subscriber is ready.
                try {
           SampleUtilities.sendSynchronizeMessage("SUBSCRIBER: ",
                        CONTROL_QUEUE);
                } catch (Exception e) {
                    System.err.println("Queue probably " + 
                        "missing: " + e.toString());
                    if (topicConnection != null) {
                        try {
                            topicConnection.close();
                        } catch (JMSException ee) {}
                    }
    	            System.exit(1);
    	        }

                /*
                 * Asynchronously process messages.
                 * Block until publisher issues a control message
                 * indicating end of publish stream.
                 */
                topicListener.monitor.waitTillDone();
                topicSubscriber.close();
                topicSession.unsubscribe("AckSub");
            } catch (JMSException e) {
                System.err.println("Exception occurred: " + 
                    e.toString());
            } finally {
                if (topicConnection != null) {
                    try {
                        topicConnection.close();
                    } catch (JMSException e) {}
                }
            }
        }	    
    }

    /**
     * The MultiplePublisher class creates a session in 
     * AUTO_ACKNOWLEDGE mode and publishes three messages 
     * to a topic. 
     */
    public class MultiplePublisher extends Thread {
        
        /**
         * Runs the thread.
         */
        public void run() {
            TopicConnectionFactory topicConnectionFactory = null;
            TopicConnection        topicConnection = null;
            TopicSession           topicSession = null;
            Topic                  topic = null;
            TopicPublisher         topicPublisher = null;
            TextMessage            message = null;
            final int              NUMMSGS = 3;
            final String           MSG_TEXT = 
               new String("Here is an auto-acknowledge message");

            try {
                topicConnectionFactory = 
                    SampleUtilities.getTopicConnectionFactory();
                topicConnection = 
                  topicConnectionFactory.createTopicConnection();
                topicSession = 
                    topicConnection.createTopicSession(false, 
                        Session.AUTO_ACKNOWLEDGE);
                System.out.println("PUBLISHER: Created " +
                    "auto-acknowledge session");
                topic = 
                    SampleUtilities.getTopic(topicName, 
                        topicSession);
            } catch (Exception e) {
                System.err.println("Connection problem: " + 
                    e.toString());
                if (topicConnection != null) {
                    try {
                        topicConnection.close();
                    } catch (JMSException ee) {}
                }
                System.exit(1);
            }

            /*
             * After synchronizing with subscriber, create 
             *   publisher.
             * Send 3 messages, varying text slightly.
             * Send end-of-messages message.
             */
            try {
                /*
                 * Synchronize with subscriber.  Wait for message
                 * indicating that subscriber is ready to receive
                 * messages.
                 */
                try {
        SampleUtilities.receiveSynchronizeMessages("PUBLISHER: ",
                    CONTROL_QUEUE, 1);
                } catch (Exception e) {
                    System.err.println("Queue probably " +
                        "missing: " + e.toString());
                    if (topicConnection != null) {
                        try {
                            topicConnection.close();
                        } catch (JMSException ee) {}
                    }
    	            System.exit(1);
    	        }

                topicPublisher = 
                    topicSession.createPublisher(topic);
                message = topicSession.createTextMessage();
                for (int i = 0; i < NUMMSGS; i++) {
                    message.setText(MSG_TEXT + " " + (i + 1));
                    System.out.println("PUBLISHER: Publishing " +
                        "message: " + message.getText());
                    topicPublisher.publish(message);
                }
                
                /*
                 * Send a non-text control message indicating 
                 * end of messages. 
                 */
            topicPublisher.publish(topicSession.createMessage());
            } catch (JMSException e) {
                System.err.println("Exception occurred: " + 
                    e.toString());
            } finally {
                if (topicConnection != null) {
                    try {
                        topicConnection.close();
                    } catch (JMSException e) {}
                }
            }
        }
    }
    
    /**
     * Instantiates the sender, receiver, subscriber, and
     * publisher classes and starts their threads.
     * Calls the join method to wait for the threads to die.
     */
    public void run_threads() {
        SynchSender        synchSender = new SynchSender();
        SynchReceiver      synchReceiver = new SynchReceiver();
        AsynchSubscriber   asynchSubscriber = 
                                        new AsynchSubscriber();
        MultiplePublisher  multiplePublisher = 
                                       new MultiplePublisher();
        
        synchSender.start();
        synchReceiver.start();
        try {
            synchSender.join();
            synchReceiver.join();
        } catch (InterruptedException e) {}

        asynchSubscriber.start();
        multiplePublisher.start();
        try {
            asynchSubscriber.join();
            multiplePublisher.join();
        } catch (InterruptedException e) {}
    }

    /**
     * Reads the queue and topic names from the command line,
     * then calls the run_threads method to execute the program
     * threads.
     *
     * @param args    the topic used by the example
     */
    public static void main(String[] args) {
        AckEquivExample  aee = new AckEquivExample();
        
        if (args.length != 0) {
            System.out.println("Usage: java AckEquivExample");
            System.exit(1);
        }
        System.out.println("Queue name is " + aee.CONTROL_QUEUE);
        System.out.println("Queue name is " + aee.queueName);
        System.out.println("Topic name is " + aee.topicName);
        System.out.println("Connection factory name is " + 
            aee.conFacName);

        aee.run_threads();
    }
}