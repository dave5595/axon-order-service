package com.finevotech.orderservicecqrs.exchange.enum

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

@JsonDeserialize(using = ExecutionRestatementReasonDeserializer::class)
enum class ExecutionRestatementReason(@JsonValue val reason: String, val description: String) {
    GTCorporateActions("0", "Expired due to corporate actions"),
    ExpiredCarriedForwardGT("3", "Expired due top carried forward GT"),
    ExpiredDueToDynamicLimit("6", "Expired due to breaking bursa dynamic limit"),
    Others("99", "Expired due to others reason");
}


class ExecutionRestatementReasonDeserializer: StdDeserializer<ExecutionRestatementReason>(ExecutionRestatementReason::class.java)
{
    override fun deserialize(jp: JsonParser?, ctxt: DeserializationContext?): ExecutionRestatementReason {
        val node = jp!!.codec.readTree<JsonNode>(jp)
        return when(node.asText()){
            ExecutionRestatementReason.ExpiredCarriedForwardGT.reason ->
                ExecutionRestatementReason.ExpiredCarriedForwardGT
            ExecutionRestatementReason.ExpiredDueToDynamicLimit.reason ->
                ExecutionRestatementReason.ExpiredDueToDynamicLimit
            ExecutionRestatementReason.GTCorporateActions.reason ->
                ExecutionRestatementReason.GTCorporateActions
            ExecutionRestatementReason.Others.reason ->
                ExecutionRestatementReason.Others
            else -> throw EnumConstantNotPresentException(ExecutionRestatementReason::class.java, node.asText())
        }
    }

}