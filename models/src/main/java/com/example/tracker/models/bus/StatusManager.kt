package com.example.tracker.models.bus

import kotlinx.coroutines.flow.Flow

interface StatusManager {

    fun getGpsStatus(): Flow<Boolean>

    fun setGpsStatus(isEnabled: Boolean)

    fun getServiceStatus(): Flow<Boolean>

    fun setServiceStatus(isEnabled: Boolean)

    fun getLocationsCounter(): Flow<Int>

    fun setLocationsCounter(count: Int)

}
