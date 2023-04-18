package ru.pgk63.core_common.kotlinxSerialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = String::class)
object DateSerialization : KSerializer<String> {

    override fun deserialize(decoder: Decoder): String {
        val fromFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val parse = fromFormat.parse(decoder.decodeString())
        val toFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return toFormat.format(parse!!)
    }

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }
}