package com.finevotech.orderservicecqrs.order.persistence

import com.finevotech.orderservicecqrs.order.enum.OrderState
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table
class OrderEventLog(@Id val orderId: UUID, orderState: OrderState)