package ru.pgk63.core_model.serialozation

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

@ExperimentalSerializationApi
@Serializer(forClass = Date::class)
internal object DateSerialization : KSerializer<Date> {

    override fun deserialize(decoder: Decoder): Date {
        return Date(decoder.decodeLong())
    }

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeLong(value.time)
    }
}