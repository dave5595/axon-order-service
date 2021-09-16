package com.finevotech.orderservicecqrs

import com.finevotech.marketorderapigateway.common.JsonMapper
import com.finevotech.marketorderapigateway.common.Logger
import com.finevotech.orderservicecqrs.common.Codecs
import com.finevotech.orderservicecqrs.exchange.ExecutionReport
import com.finevotech.orderservicecqrs.order.command.NewOrderCommand
import com.finevotech.orderservicecqrs.order.command.OrderCommand
import com.finevotech.orderservicecqrs.order.enum.OrderAction
import com.finevotech.orderservicecqrs.order.enum.OrderSide
import com.finevotech.orderservicecqrs.order.enum.OrderType
import com.finevotech.orderservicecqrs.order.enum.OrderValidity
import org.junit.jupiter.api.Test
import org.springframework.util.Assert
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import org.springframework.web.reactive.socket.client.StandardWebSocketClient
import org.springframework.web.reactive.socket.client.WebSocketClient
import reactor.core.publisher.Flux
import reactor.core.publisher.ReplayProcessor
import java.net.URI
import java.time.Duration
import java.time.LocalTime
import java.util.*


//@SpringBootTest
class OrderServiceCqrsApplicationTests {
    val log by Logger()

    @Test
    fun codecTest() {
        val uuid = UUID.randomUUID().toString()
        println("uuid:$uuid")
        val encoded = Codecs.encodeToBase64(uuid)
        println("encoded: $encoded")
        val decoded = Codecs.decodeFromBase64(encoded)
        println("decoded: $decoded")
        Assert.isTrue(uuid == decoded, "uuid should equals to decoded")
    }

    @Test
    fun executionReportDeserializeTest() {
        val erString =
            "{\"-1\":\"executed\",\"-2\":\"2021-06-24T01:23:52.278Z\",\"1\":\"000437939\",\"11\":\"BM202106240000006\",\"14\":0.0,\"17\":\"198\",\"22\":\"99\",\"37\":\"202106247\",\"38\":168800.0,\"39\":\"0\",\"40\":\"2\",\"44\":4.01,\"48\":\"6888\",\"54\":\"2\",\"59\":\"0\",\"60\":\"2021-06-24T01:23:52.276Z\",\"150\":\"0\",\"151\":168800.0,\"529\":\"R\",\"762\":\"NM\",\"4536\":\"FX054USR03\",\"4512\":\"054915\",\"4501\":\"054\",\"4544\":\"FX054USR03\",\"4511\":\"lwpdm\",\"4504\":\"054\",\"4503\":\"12046\"}"
        JsonMapper.readValue<ExecutionReport>(erString).also {
            println("deserialized: $it")
        }
    }

    @Test
    fun testWebsocketClient() {
		val count = 100
		val input = Flux.range(1, count).map { buildCreateOrderCommand(UUID.randomUUID()) }

		val client = ReactorNettyWebSocketClient()
		client.execute(URI("ws://127.0.0.1:8080/api-ws")) { session: WebSocketSession ->
			log.info("connected to ws server...")
			log.info("sending commands")
			session
				.send(input.map(JsonMapper::writeValueAsString).map(session::textMessage))
				.then()
		}
			.block(Duration.ofMillis(5000))


       /* val client = ReactorNettyWebSocketClient()
        val commands = Flux.create<OrderCommand> { sink ->
            repeat(100) {
                val id = UUID.randomUUID()
                val cmd = buildCreateOrderCommand(id)
                sink.next(cmd)
            }
        }.map(JsonMapper::writeValueAsString)
        client.execute(URI("ws://127.0.0.1:8080/api-ws")) { session ->
			log.info("connected to ws server...")
			log.info("sending commands")
			session.send(commands.map(session::textMessage)).then()
		}.block(Duration.ofMillis(100000))*/
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

}
