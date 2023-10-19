package com.example.tracker.data.locations.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tracker.models.locations.Location

@Entity(MapLocationEntity.LOADED_LOCATIONS_TABLE_NAME)
class MapLocationEntity(
    @PrimaryKey
    val time: String,
    val ownerId: String,
    val latitude: Double,
    val longitude: Double
) {

    fun toLocation(): Location = Location(
        time = time,
        ownerId = ownerId,
        latitude = latitude,
        longitude = longitude
    )

    companion object {

        const val LOADED_LOCATIONS_TABLE_NAME = "mapLocations"

        fun toLocationEntity(location: Location) = MapLocationEntity(
            time = location.time,
            ownerId = location.ownerId,
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

}
