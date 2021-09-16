package com.finevotech.marketorderapigateway.common

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.introspect.VisibilityChecker
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.IOException

object JsonMapper {

    public val objectMapper = ObjectMapper()
    inline fun <reified T> convertValue(fromValue: Any): T {
        return objectMapper.convertValue(fromValue, T::class.java)
    }

    fun <T> convertValue(fromValue: Any?, toValueType: Class<T>?): T {
        return objectMapper.convertValue(fromValue, toValueType)
    }

    fun <T> convertValue(fromValue: Any?, valueTypeRef: TypeReference<T>?): T {
        return objectMapper.convertValue(fromValue, valueTypeRef)
    }

    fun createArrayNode(capacity: Int): ArrayNode {
        return objectMapper.deserializationConfig.nodeFactory.arrayNode(capacity)
    }

    fun createArrayNode(): ArrayNode {
        return objectMapper.deserializationConfig.nodeFactory.arrayNode()
    }

    fun createObjectNode(): ObjectNode {
        return objectMapper.createObjectNode()
    }

    @Throws(IOException::class)
    fun readTree(content: String?): JsonNode {
        return objectMapper.readTree(content)
    }

    @Throws(IOException::class)
    inline fun <reified T> readValue(content: String): T {
        return objectMapper.readValue(content, T::class.java)
    }

    @Throws(IOException::class)
    fun <T> readValue(content: String?, valueType: Class<T>?): T {
        return objectMapper.readValue(content, valueType)
    }

    @Throws(IOException::class)
    fun <T> readValue(content: String?, valueTypeRef: TypeReference<T>?): T {
        return objectMapper.readValue(content, valueTypeRef)
    }

    @Throws(IOException::class)
    fun <T> treeToValue(fromNode: TreeNode?, valueType: Class<T>?): T {
        return objectMapper.treeToValue(fromNode, valueType)
    }

    fun <T : JsonNode?> valueToTree(fromValue: Any?): T {
        return objectMapper.valueToTree<T>(fromValue)
    }

    @Throws(IOException::class)
    fun writeValueAsString(value: Any?): String {
        return objectMapper.writeValueAsString(value)
    }

    init {
        objectMapper.registerModule(KotlinModule(nullIsSameAsDefault = true))
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
        objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
        objectMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
        objectMapper.nodeFactory = JsonNodeFactory.withExactBigDecimals(true)
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY))
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        objectMapper.findAndRegisterModules()
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }
}