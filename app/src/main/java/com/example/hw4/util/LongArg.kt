package com.example.hw4.util

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object LongArg : ReadWriteProperty<Bundle, Long?> {

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Long?) {
        thisRef.putLong(property.name, value?: 0L)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Long? {
        return thisRef.getLong(property.name)
    }
}