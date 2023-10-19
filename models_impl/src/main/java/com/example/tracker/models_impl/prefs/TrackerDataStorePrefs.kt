package com.example.tracker.models_impl.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.tracker.models.prefs.TrackerPrefs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TrackerDataStorePrefs(private val context: Context) : TrackerPrefs {

    private val Context.trackerDataStore: DataStore<Preferences> by preferencesDataStore(name = "tracker")
    private val KEY_TRACKER_STATUS = booleanPreferencesKey("tracker_status")


    override suspend fun getTrackerStatus(): Boolean {
        return context.trackerDataStore.data.map { it[KEY_TRACKER_STATUS] ?: false }.first()
    }

    override suspend fun putTrackerStatus(trackerStatus: Boolean) {
        context.trackerDataStore.edit { it[KEY_TRACKER_STATUS] = trackerStatus }
    }

}
