package com.finevotech.orderservicecqrs.websocket

import com.finevotech.marketorderapigateway.common.JsonMapper
import com.finevotech.marketorderapigateway.common.Logger
import com.finevotech.marketorderapigateway.common.LookupRegistry
import org.reactivestreams.Publisher
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class WebsocketAccountSessionManager {
    private val log by Logger()
    private val sessions = AccountWebsocketSessionRegistry()

    fun register(accountSession: AccountSession<Long>) {
        val (accountId, wsSession) = accountSession
        if (sessions[accountId].isNullOrEmpty()) {
            sessions[accountId] = setOf(wsSession)
        } else {
            val newSessions = sessions[accountId]!!.toMutableSet()
            newSessions.add(wsSession)
            sessions[accountId] = newSessions
        }
    }

    fun removeAccountSession(accountId: Long, session: WebSocketSession) {
        val accountSessions = sessions[accountId]
        accountSessions?.let { set ->
            set
                .filter { it == session }
                .toSet()
                .also { sessions[accountId] = it }
        }
    }

    fun <T> notifyAccountSessions(data: T, accountId: Long) {
        sessions[accountId]?.onEach { session ->
            try {
                session.send(textMessageConverter(session, data))
            } catch (e: Exception) {
                log.warn("An error occurred while trying to send a message to a WebSocket", e)
            }
        }
    }

    private fun <T> textMessageConverter(session: WebSocketSession, data: T): Publisher<WebSocketMessage> = Mono.create { session.textMessage(JsonMapper.writeValueAsString(data)) }
}

data class AccountSession<T>(val accountId: T,val session: WebSocketSession)

class AccountWebsocketSessionRegistry : LookupRegistry<Long, Set<WebSocketSession>>()