package com.github.openweather.library.models

import com.github.openweather.library.annotations.JsonKey
import java.util.*

@JsonKey("", "")
class UvIndex : JsonModel {
    private val tag: String = UvIndex::class.java.simpleName

    var coordinates: Coordinates = Coordinates()

    @JsonKey("", "date")
    var dateTime: Calendar = Calendar.getInstance()

    @JsonKey("", "value")
    var value: Double = 0.0

    override fun toString(): String = "{Class: $tag, Coordinates: $coordinates, DateTime: $dateTime, Value: $value}"
}