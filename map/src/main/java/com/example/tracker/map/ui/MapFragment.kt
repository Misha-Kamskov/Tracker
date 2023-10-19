package com.example.tracker.map.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.tracker.map.R
import com.example.tracker.map.databinding.FragmentMapBinding
import com.example.tracker.models.locations.Location
import com.example.tracker.mvi.fragments.HostedFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : HostedFragment<MapContract.View, MapContract.ViewModel, MapContract.Host>(),
    MapContract.View, View.OnClickListener {

    private var bind: FragmentMapBinding? = null

    override fun createModel(): MapContract.ViewModel {
        val viewModel: MapViewModel by viewModels()
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST) {}
        bind = FragmentMapBinding.inflate(layoutInflater)
        return (bind?.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind?.ibLogout?.setOnClickListener(this)
        bind?.btTimePicker?.setOnClickListener(this)
    }

    override fun proceedToLoginScreen() {
        fragmentHost?.proceedLocationToLoginScreen()
    }

    override fun showMapState(locationsList: List<Location>, isGetLocationsRunning: Boolean) {
        if (isGetLocationsRunning){
            bind?.mapBar?.visibility = View.VISIBLE
        }else{
            bind?.mapBar?.visibility = View.INVISIBLE
            val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
            mapFragment.getMapAsync { googleMap ->
                renderMarks(
                    googleMap,
                    locationsList.map { LatLng(it.latitude, it.longitude) })
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ib_logout -> model?.singOut()
            R.id.bt_time_picker -> showDateRangePicker()
        }
    }

    override fun showMessage(messageId: Int) {
        Toast.makeText(context, getString(messageId), Toast.LENGTH_LONG).show()
    }

    private fun showDateRangePicker() {
        val picker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTheme(R.style.CustomMaterialDatePickerTheme)
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            model?.getFilteredLocations(selection.first, selection.second)
        }
        picker.show(childFragmentManager, null)
    }

    private fun renderMarks(mMap: GoogleMap, list: List<LatLng>) {

        if (list.isEmpty()) {
            return
        }

        val polylineOptions = PolylineOptions()
            .clickable(true)
            .addAll(list)
            .color(ContextCompat.getColor(requireContext(), R.color.way_color))

        mMap.clear()
        mMap.addPolyline(polylineOptions)

        val boundsBuilder = LatLngBounds.Builder()
        var initialPoint: LatLng? = null

        for (point in list) {
            boundsBuilder.include(point)
            if (initialPoint == null) {
                initialPoint = point
            }
        }

        val bounds = boundsBuilder.build()

        val padding = resources.getDimensionPixelSize(R.dimen.dp_10)
        val cameraUpdate = if (initialPoint != null) {
            CameraUpdateFactory.newLatLngBounds(bounds.including(initialPoint), padding)
        } else {
            CameraUpdateFactory.newLatLngZoom(list.first(), 13f)
        }

        mMap.animateCamera(cameraUpdate)

    }

}
