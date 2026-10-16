package com.bccard.munki.core.extensions

import android.util.Base64
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.Types
import org.json.JSONObject
import java.lang.reflect.Type

private object ByteArrayAdapter {
    @ToJson
    fun toJson(bytes: ByteArray): String =
        Base64.encodeToString(bytes, Base64.NO_WRAP)

    @FromJson
    fun fromJson(base64: String): ByteArray =
        Base64.decode(base64, Base64.NO_WRAP)
}

// enum 은 이름으로 읽고, 서버가 새 값을 내려줘도 예외 대신 null (기본 어댑터는 모르는 값에 JsonDataException → 응답 전체 실패)
private object LenientEnumAdapterFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
        val raw = Types.getRawType(type)
        if (!raw.isEnum || annotations.isNotEmpty()) return null

        @Suppress("UNCHECKED_CAST")
        val constants = raw.enumConstants as Array<Enum<*>>

        return object : JsonAdapter<Enum<*>>() {
            override fun fromJson(reader: JsonReader): Enum<*>? = reader.nextString().let { name -> constants.firstOrNull { it.name == name } }
            override fun toJson(writer: JsonWriter, value: Enum<*>?) { writer.value(value?.name) }
        }.nullSafe()
    }
}

// 모든 모델이 @JsonClass codegen 이라 리플렉션 어댑터(KotlinJsonAdapterFactory) 없이 동작
val moshi: Moshi = Moshi.Builder()
    .add(ByteArrayAdapter)
    .add(LenientEnumAdapterFactory)
    .build()

// JSON 문자열 → 객체 변환
inline fun <reified T> String.transformJsonToObject() = try {
    moshi.adapter(T::class.java).fromJson(this)
} catch (ex: Exception) {
    null
}

// (특수 케이스) JSONObject로 감싸서 → 객체 변환
inline fun <reified T> String?.transformJsonObjectToObject() = try {
    if (this == null) null
    else {
        val jSONObject = JSONObject(this)
        jSONObject.toString().transformJsonToObject<T>()
    }
} catch (ex: Exception) {
    null
}