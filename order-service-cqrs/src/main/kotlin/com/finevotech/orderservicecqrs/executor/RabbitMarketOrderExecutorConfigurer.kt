package com.finevotech.orderservicecqrs.executor

import com.yoda.mo.api.provider.executor.MarketOrderExecutor
import com.yoda.mo.api.provider.executor.impl.amqp.AmqpMarketOrderExecutor
import org.springframework.amqp.core.AcknowledgeMode
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.util.concurrent.ConcurrentHashMap

@Configuration
@Profile("rabbit-executor")
class RabbitMarketOrderExecutorConfigurer : ExecutorConfigurer {
    val marketOrderExecutorMap = ConcurrentHashMap<String, MarketOrderExecutor>()

    @Autowired
    lateinit var amqpAdmin: AmqpAdmin
    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate
    @Autowired
    lateinit var listenerContainerFactory: SimpleRabbitListenerContainerFactory

    val messageConverter = Jackson2JsonMessageConverter()

    override fun marketOrderExecutor(resourceId: String): MarketOrderExecutor {
        var marketOrderExecutor = marketOrderExecutorMap[resourceId]

        if (marketOrderExecutor == null) {
            rabbitTemplate.messageConverter = messageConverter
            rabbitTemplate.containerAckMode(AcknowledgeMode.AUTO)
            marketOrderExecutor =
                AmqpMarketOrderExecutor(
                    resourceId,
                    amqpAdmin,
                    rabbitTemplate,
                    listenerContainerFactory,
                    messageConverter
                )
        }
        return marketOrderExecutor
    }
}