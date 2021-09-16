package com.finevotech.orderservicecqrs.order.command

import com.finevotech.marketorderapigateway.common.Logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("rabbit-executor")
@Component
class RabbitExecutionReportListener {
    private val log by Logger()

    //listen to events by exchange
//    @RabbitListener(queues = ["BM_054915_ExecutedOrder"])
//    fun listen(executionReport: String) {
//        log.info("received report: $executionReport")
////        commandGateway.send<Any>(executionReport.toCommand())
//    }
}