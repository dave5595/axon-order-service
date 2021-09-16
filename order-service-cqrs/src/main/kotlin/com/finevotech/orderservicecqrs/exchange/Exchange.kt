package com.finevotech.orderservicecqrs.exchange

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class Exchange {
    //todo exchangeId = fixChannelId?
    //todo exchange trading phase
    //for now we represent as a boolean flag for simplicity
    val open = true
}