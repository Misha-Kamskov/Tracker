package com.example.tracker.data.locations.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MapLocationsDao {

    @Insert(entity = MapLocationEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocations(locations: List<MapLocationEntity>)

    @Query("DELETE FROM mapLocations")
    suspend fun deleteAllLocations()

    @Query("SELECT * FROM mapLocations WHERE time BETWEEN :startDate and :endDate ORDER BY time ASC")
    suspend fun getLocations(startDate: Long, endDate: Long): List<MapLocationEntity>

}
