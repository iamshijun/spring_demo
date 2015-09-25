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
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SampleUtilities {
    public static final String  QUEUECONFAC = 
                                        "QueueConnectionFactory";
    public static final String  TOPICCONFAC = 
                                        "TopicConnectionFactory";
    private static Context      jndiContext = null;
    
    /**
     * Returns a QueueConnectionFactory object.
     *
     * @return    a QueueConnectionFactory object
     * @throws    javax.naming.NamingException (or other
     *            exception) if name cannot be found
     */
    public static QueueConnectionFactory 
            getQueueConnectionFactory() throws Exception {
        return (QueueConnectionFactory) jndiLookup(QUEUECONFAC);
    }
    
    /**
     * Returns a TopicConnectionFactory object.
     *
     * @return    a TopicConnectionFactory object
     * @throws    javax.naming.NamingException (or other
     *            exception) if name cannot be found
     */
    public static TopicConnectionFactory 
            getTopicConnectionFactory() throws Exception {
        return (TopicConnectionFactory) jndiLookup(TOPICCONFAC);
    }
    
    /**
     * Returns a Queue object.
     *
     * @param name       String specifying queue name
     * @param session    a QueueSession object
     *
     * @return           a Queue object
     * @throws           javax.naming.NamingException (or other
     *                   exception) if name cannot be found
     */
    public static Queue getQueue(String name, 
            QueueSession session) throws Exception {
        return (Queue) jndiLookup(name);
    }
    
    /**
     * Returns a Topic object.
     *
     * @param name       String specifying topic name
     * @param session    a TopicSession object
     *
     * @return           a Topic object
     * @throws           javax.naming.NamingException (or other
     *                   exception) if name cannot be found
     */
    public static Topic getTopic(String name, 
            TopicSession session) throws Exception {
        return (Topic) jndiLookup(name);
    }
    
    /**
     * Creates a JNDI API InitialContext object if none exists
     * yet. Then looks up the string argument and returns the
     * associated object.
     *
     * @param name    the name of the object to be looked up
     *
     * @return        the object bound to name
     * @throws        javax.naming.NamingException (or other
     *                exception) if name cannot be found
     */
    public static Object jndiLookup(String name) 
        throws NamingException {
        Object    obj = null;

        if (jndiContext == null) {
            try {
                jndiContext = new InitialContext();
            } catch (NamingException e) {
                System.err.println("Could not create JNDI API " +
                    "context: " + e.toString());
                throw e;
            }
        }
        try {
           obj = jndiContext.lookup(name);
        } catch (NamingException e) {
            System.err.println("JNDI API lookup failed: " + 
                e.toString());
            throw e;
        }
        return obj;
    }
      
    /**
     * Waits for 'count' messages on controlQueue before
     * continuing.  Called by a publisher to make sure that
     * subscribers have started before it begins publishing
     * messages.
     * 
     * If controlQueue does not exist, the method throws an 
     * exception.
     *
     * @param prefix    prefix (publisher or subscriber) to be 
     *                  displayed
     * @param controlQueueName  name of control queue 
     * @param count     number of messages to receive
     */
    public static void receiveSynchronizeMessages(String prefix,
            String controlQueueName, int count) 
            throws Exception {
        QueueConnectionFactory  queueConnectionFactory = null;
        QueueConnection         queueConnection = null;
        QueueSession            queueSession = null;
        Queue                   controlQueue = null;
        QueueReceiver           queueReceiver = null;

        try {
            queueConnectionFactory = 
                SampleUtilities.getQueueConnectionFactory();
            queueConnection = 
                queueConnectionFactory.createQueueConnection();
            queueSession = 
                queueConnection.createQueueSession(false, 
                    Session.AUTO_ACKNOWLEDGE);
            controlQueue = getQueue(controlQueueName, 
                queueSession);
            queueConnection.start();
        } catch (Exception e) {
            System.err.println("Connection problem: " + 
                e.toString());
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException ee) {}
            }
            throw e;
        } 

        try {
            System.out.println(prefix +
                "Receiving synchronize messages from " +
                controlQueueName + "; count = " + count);
            queueReceiver = 
                queueSession.createReceiver(controlQueue);
            while (count > 0) {
                queueReceiver.receive();
                count--;
                System.out.println(prefix +
                    "Received synchronize message; " +
                    " expect " + count + " more");
            }
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + 
                e.toString());
            throw e;
        } finally {
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException e) {}
            }
        }
    }

    /**
     * Sends a message to controlQueue.  Called by a subscriber
     * to notify a publisher that it is ready to receive
     * messages.
     * <p>
     * If controlQueue doesn't exist, the method throws an 
     * exception.
     *
     * @param prefix    prefix (publisher or subscriber) to be 
     *                  displayed
     * @param controlQueueName  name of control queue
     */
    public static void sendSynchronizeMessage(String prefix,
            String controlQueueName) 
            throws Exception {
        QueueConnectionFactory  queueConnectionFactory = null;
        QueueConnection         queueConnection = null;
        QueueSession            queueSession = null;
        Queue                   controlQueue = null;
        QueueSender             queueSender = null;
        TextMessage             message = null;

        try {
            queueConnectionFactory = 
                SampleUtilities.getQueueConnectionFactory();
            queueConnection = 
                queueConnectionFactory.createQueueConnection();
            queueSession = 
                queueConnection.createQueueSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            controlQueue = getQueue(controlQueueName, 
                queueSession);
        } catch (Exception e) {
            System.err.println("Connection problem: " + 
                e.toString());
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException ee) {}
            }
            throw e;
        } 

        try {
            queueSender = 
                queueSession.createSender(controlQueue);
            message = queueSession.createTextMessage();
            message.setText("synchronize");
            System.out.println(prefix + 
                "Sending synchronize message to " +
                controlQueueName);
            queueSender.send(message);
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + 
                e.toString());
            throw e;
        } finally {
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException e) {}
            }
        }
    }

    /**
     * Monitor class for asynchronous examples.  Producer signals
     * end of message stream; listener calls allDone() to notify
     * consumer that the signal has arrived, while consumer calls
     * waitTillDone() to wait for this notification.
     */
    static public class DoneLatch {
        boolean  done = false;

        /**
         * Waits until done is set to true.
         */
        public void waitTillDone() {
            synchronized (this) {
                while (! done) {
                    try {
                        this.wait();
                    } catch (InterruptedException ie) {}
                }
            }
        }
        
        /**
         * Sets done to true.
         */
        public void allDone() {
            synchronized (this) {
                done = true;
                this.notify();
            }
        }
    }
}