package com.finevotech.orderservicecqrs.order.persistence

import com.finevotech.orderservicecqrs.order.enum.OrderState
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(value = "oms_order")
class OrderEntity(
    @Id
    private val id: UUID = UUID.randomUUID(),
    private val state: OrderState,
)