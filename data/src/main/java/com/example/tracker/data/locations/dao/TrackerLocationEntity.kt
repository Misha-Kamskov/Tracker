package com.example.tracker.data.locations.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tracker.models.locations.Location

@Entity(TrackerLocationEntity.LOCATIONS_TABLE_NAME)
data class TrackerLocationEntity(
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

        const val LOCATIONS_TABLE_NAME = "trackerLocations"

        fun toLocationEntity(location: Location) = TrackerLocationEntity(
            time = location.time,
            ownerId = location.ownerId,
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

}
