package org.rabbitmq.tutorial6;

/**
 * Rabbirmq tutorial <a href="http://www.rabbitmq.com/tutorials/tutorial-six-java.html" > RPC</a>
 * @author Administrator
 *
 */
public class RabbitmqTest7 {

	/*
	 * 这里提及一下 BindingKey 和 RoutingKey 但使用的是非topic类型的exchange的时候BindingKey和RoutingKey是一致的
	 * 如果是是topic类型的exchange 1 如果BindingKey中没有含有 * 或者 # 的话和direct类型的exchange所起的作用是一样的(另外BindingKey还是和RoutingKey一样)
	 * 如果BindingKey中含有 # 或者/和 *, 那么这个BindingKey就会和多个Routingkey相对应(模式匹配成功的)
	 */
}
