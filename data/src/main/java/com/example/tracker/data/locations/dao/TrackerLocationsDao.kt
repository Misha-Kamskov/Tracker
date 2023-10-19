package com.example.tracker.data.locations.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TrackerLocationsDao {

    @Upsert(entity = TrackerLocationEntity::class)
    suspend fun upsertLocation(locationEntity: TrackerLocationEntity)

    @Delete(entity = TrackerLocationEntity::class)
    suspend fun deleteLocation(locationEntity: TrackerLocationEntity)

    @Query("DELETE FROM trackerLocations")
    suspend fun deleteAllLocations()

    @Query("SELECT * FROM trackerLocations")
    suspend fun getLocations(): List<TrackerLocationEntity>

}
