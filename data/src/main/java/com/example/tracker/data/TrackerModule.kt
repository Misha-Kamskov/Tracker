package com.example.tracker.data

import android.content.Context
import com.example.tracker.data.locations.dao.MapLocationsDao
import com.example.tracker.data.locations.dao.TrackerLocationsDao
import com.example.tracker.models.LocationsRepository
import com.example.tracker.data.locations.LocationsRepositoryImp
import com.example.tracker.models.auth.Auth
import com.example.tracker.models.bg.LocationController
import com.example.tracker.models.bg.work.WorkScheduler
import com.example.tracker.models.bus.StatusManager
import com.example.tracker.models.gps.LocationSource
import com.example.tracker.models.locations.cache.LocationsCache
import com.example.tracker.models.locations.network.LocationsNetwork
import com.example.tracker.models.prefs.MapPrefs
import com.example.tracker.models.prefs.TrackerPrefs
import com.example.tracker.models_impl.auth.FireBaseAuth
import com.example.tracker.models_impl.bg.LocationServiceController
import com.example.tracker.models_impl.bg.work.UploadWorkScheduler
import com.example.tracker.models_impl.bus.TrackerStatusManager
import com.example.tracker.models_impl.gps.DefaultLocationSource
import com.example.tracker.models_impl.locations.cache.LocationsCacheImpl
import com.example.tracker.models_impl.locations.network.FirebaseLocationsNetwork
import com.example.tracker.models_impl.prefs.MapDataStorePrefs
import com.example.tracker.models_impl.prefs.TrackerDataStorePrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TrackerModule {

    @Provides
    fun provideAuth(): Auth = FireBaseAuth()

    @Provides
    @Singleton
    fun provideDefaultLocationSource(@ApplicationContext context: Context): LocationSource {
        return DefaultLocationSource(context)
    }

    @Provides
    @Singleton
    fun provideDefaultLocationModel(): StatusManager = TrackerStatusManager()

    @Provides
    @Singleton
    fun provideLocationsCache(prefs: MapPrefs): LocationsCache {
        return LocationsCacheImpl(prefs)
    }

    @Provides
    fun provideRemoteDb(): LocationsNetwork =
        FirebaseLocationsNetwork()

    @Provides
    fun provideLocationServiceController(
        locationSource: LocationSource,
        model: StatusManager,
        locationRepository: LocationsRepository,
        uploadWorkScheduler: WorkScheduler,
        prefs: TrackerPrefs
    ): LocationController {
        return LocationServiceController(
            locationSource, model, locationRepository, uploadWorkScheduler, prefs
        )
    }

    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDB(context)
    }

    @Provides
    @Singleton
    fun provideLocationDao(database: AppDatabase): TrackerLocationsDao {
        return database.getLocationsDao()
    }

    @Provides
    @Singleton
    fun provideMapLocationDao(database: AppDatabase): MapLocationsDao {
        return database.getLoadedLocationsDao()
    }

    @Provides
    fun provideLocationRepository(
        trackerDao: TrackerLocationsDao,
        mapDao: MapLocationsDao,
        network: LocationsNetwork,
        cache: LocationsCache,
        auth: Auth
    ): LocationsRepository {
        return LocationsRepositoryImp(trackerDao, mapDao, network, auth, cache)
    }

    @Provides
    fun provideWorkController(@ApplicationContext context: Context): WorkScheduler {
        return UploadWorkScheduler(context)
    }

    @Provides
    @Singleton
    fun provideTrackerDataStorePrefs(@ApplicationContext context: Context): TrackerPrefs {
        return TrackerDataStorePrefs(context)
    }

    @Provides
    @Singleton
    fun provideMapDataStorePrefs(@ApplicationContext context: Context): MapPrefs {
        return MapDataStorePrefs(context)
    }

}
