package com.finevotech.orderservicecqrs

import com.fasterxml.jackson.databind.ObjectMapper
import com.finevotech.orderservicecqrs.exchange.Exchange
import com.finevotech.orderservicecqrs.order.command.NewOrderCommand
import com.finevotech.orderservicecqrs.order.command.Order
import com.finevotech.orderservicecqrs.order.command.OrderManagerSaga
import com.finevotech.orderservicecqrs.order.enum.OrderAction
import com.finevotech.orderservicecqrs.order.enum.OrderSide
import com.finevotech.orderservicecqrs.order.enum.OrderType
import com.finevotech.orderservicecqrs.order.enum.OrderValidity
import com.lmax.disruptor.BusySpinWaitStrategy
import com.lmax.disruptor.YieldingWaitStrategy
import org.axonframework.axonserver.connector.event.axon.AxonServerEventStore
import org.axonframework.common.caching.Cache
import org.axonframework.common.caching.WeakReferenceCache
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.config.SagaConfigurer
import org.axonframework.disruptor.commandhandling.DisruptorCommandBus
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.eventhandling.tokenstore.inmemory.InMemoryTokenStore
import org.axonframework.eventsourcing.AggregateFactory
import org.axonframework.eventsourcing.AggregateLoadTimeSnapshotTriggerDefinition
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.messaging.interceptors.CorrelationDataInterceptor
import org.axonframework.modelling.saga.repository.CachingSagaStore
import org.axonframework.modelling.saga.repository.SagaStore
import org.axonframework.modelling.saga.repository.inmemory.InMemorySagaStore
import org.axonframework.serialization.json.JacksonSerializer
import org.axonframework.spring.config.AxonConfiguration
import org.axonframework.spring.eventhandling.scheduling.java.SimpleEventSchedulerFactoryBean
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotter
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotterFactoryBean
import org.axonframework.spring.eventsourcing.SpringPrototypeAggregateFactory
import org.springframework.beans.factory.FactoryBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.time.LocalTime
import java.util.*
import java.util.concurrent.Executors


@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class OrderServiceCqrsApplication {

    /**
     * For Axon default configurations
     * @see SnapshotTriggerDefinition
     *
     */

    @Bean
    fun simpleEventSchedulerFactoryBean(): SimpleEventSchedulerFactoryBean {
        return SimpleEventSchedulerFactoryBean()
    }

    //configuring serializers
    @Bean
    fun eventSerializer(mapper: ObjectMapper): JacksonSerializer {
        return JacksonSerializer.Builder()
            .objectMapper(mapper)
            .build()
    }

    @Bean
    fun sagaStore(sagaCache: Cache, associationsCache: Cache): CachingSagaStore<Any>{
        val sagaStore = InMemorySagaStore()
        return CachingSagaStore.Builder<Any>()
            .delegateSagaStore(sagaStore)
            .associationsCache(associationsCache)
            .sagaCache(sagaCache)
            .build()
    }

    @Bean
    fun snapshotterFactoryBean(): FactoryBean<SpringAggregateSnapshotter> {
        return SpringAggregateSnapshotterFactoryBean()
    }

    @Bean
    fun commandBusCache(): Cache {
        return WeakReferenceCache()
    }

    @Bean
    fun sagaCache(): Cache {
        return WeakReferenceCache()
    }

    @Bean
    fun associationsCache(): Cache {
        return WeakReferenceCache()
    }

    /**
     *  notes: get throughput of 100tx/1sec with average latency of ~10ms
     *   - config -  buffersize = default | executor = fixThreadPool(8) | invokerThread & publisherThreadCount = 1
     *   - infra - axon server | cached disruptor command bus | cached in-memory saga
     * */
    @Bean
    fun commandBus(
        commandBusCache: Cache,
        orderAggregateFactory: SpringPrototypeAggregateFactory<Order>,
        eventStore: EventStore,
        txManager: TransactionManager,
        axonConfiguration: AxonConfiguration,
        snapshotter: SpringAggregateSnapshotter
    ): DisruptorCommandBus {
        val commandBus = DisruptorCommandBus.builder()
            .waitStrategy(BusySpinWaitStrategy())
            .executor(Executors.newFixedThreadPool(8))
            .publisherThreadCount(1)
            .invokerThreadCount(1)
            .transactionManager(txManager)
            .cache(commandBusCache)
            .messageMonitor(axonConfiguration.messageMonitor(DisruptorCommandBus::class.java, "commandBus"))
            .build()
        //todo ask stephen about the issue with loading aggregate, and sensible values for the wait time
//        commandBus.createRepository(
//            eventStore,
//            orderAggregateFactory,
//            AggregateLoadTimeSnapshotTriggerDefinition(snapshotter, 5))
        commandBus.registerHandlerInterceptor(CorrelationDataInterceptor(axonConfiguration.correlationDataProviders()))
        return commandBus
    }
}

fun main(args: Array<String>) {
    val context = runApplication<OrderServiceCqrsApplication>(*args)
    /*val commandGateway = context.getBean(CommandGateway::class.java)
    repeat(100) {
        Thread.sleep(10)
        val cmd = buildCreateOrderCommand(UUID.randomUUID())
        commandGateway.send<NewOrderCommand>(cmd)
    }*/
}

private fun buildCreateOrderCommand(id: UUID): NewOrderCommand {
    return NewOrderCommand(
        orderId = id.toString(),
        price = 2.5,
        quantity = 100,
        accountId = "account001",
        dealerCode = "dealer001",
        securitySubType = "NM",
        securityId = "1023",
        cdsNumber = "000034",
        action = OrderAction.New,
        side = OrderSide.Bid,
        type = OrderType.Limit,
        validity = OrderValidity.SessionOrDay,
        created = LocalTime.now()
    )
}
