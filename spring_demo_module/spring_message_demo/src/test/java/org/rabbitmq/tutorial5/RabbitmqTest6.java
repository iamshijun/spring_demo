package org.rabbitmq.tutorial5;

/**
 * Topic exchange<p>
 * 
 * Topic exchange is powerful and can behave like other exchanges.<p>
 * 
 * When a queue is bound with "#" (hash) binding key - it will receive all
 * the messages, regardless of the routing key - like in fanout exchange.<p>
 * 
 * When special characters "*" (star) and "#" (hash) aren't used in
 * bindings, the topic exchange will behave just like a direct one
 */
public class RabbitmqTest6 {
}
