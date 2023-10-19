package com.example.tracker.models_impl.locations.cache

import com.example.tracker.models.locations.Location
import com.example.tracker.models.locations.cache.LocationsCache
import com.example.tracker.models.locations.cache.LocationsSet
import com.example.tracker.models.prefs.MapPrefs
import java.util.Collections

class LocationsCacheImpl(private val prefs: MapPrefs) :
    LocationsCache {
    private var loadedRanges: MutableList<Pair<Long, Long>>? = null
    private var locationsMap = mutableMapOf<Pair<Long, Long>, List<Location>>()

    override suspend fun getLocations(startDate: Long, endDate: Long): LocationsSet {
        if (loadedRanges?.isEmpty() == true) {
            // if ranges are initialized and there is nothing
            return LocationsSet(Collections.emptyList(), false)
        }
        if (loadedRanges == null) {
            loadedRanges = mutableListOf()
            loadedRanges?.addAll(prefs.getLoadedRanges())
        }
        return loadedRanges?.firstOrNull { p -> p.first <= startDate && p.second >= endDate }
            ?.let { p ->
                // range is loaded, but maybe data is not cached
                val locations = locationsMap[p]?.filter { l ->
                    l.time.toLong() in startDate..endDate
                } ?: listOf()
                LocationsSet(locations, true)
            } ?: kotlin.run {
            // if ranges not found
           LocationsSet(Collections.emptyList(), false)
        }
    }

    override suspend fun putLocations(locations: List<Location>, startDate: Long, endDate: Long) {
        // find ranges, that need to be updated
        val ranges = loadedRanges?.filter { p ->
            (p.first <= startDate && startDate <= p.second) || (p.first <= endDate && endDate <= p.second)
        } ?: listOf()
        var range: Pair<Long, Long> = Pair(startDate, endDate)
        val list: MutableList<Location> = ArrayList(locations)
        for (p in ranges) {
            if (p.first <= startDate && startDate <= p.second && endDate >= p.second) {
                // range, that contains our start date. Change our start date
                range = Pair(p.first, range.second)
            } else if (p.first <= endDate && endDate <= p.second && p.first >= startDate) {
                // range, that contains our end date. Change our end date
                range = Pair(range.first, p.second)
            }
            locationsMap[p]?.let {
                list.addAll(it)
            }
            locationsMap.remove(p)
        }
        list.sortWith { o1, o2 -> o1.time.toLong().compareTo(o2.time.toLong()) }
        loadedRanges?.removeAll(ranges)
        loadedRanges?.add(range)
        locationsMap[range] = list
        loadedRanges?.let {
            prefs.putLoadedRanges(it)
        }
    }

    override suspend fun clear() {
        locationsMap.clear()
        loadedRanges = null
        prefs.clear()
    }

}
