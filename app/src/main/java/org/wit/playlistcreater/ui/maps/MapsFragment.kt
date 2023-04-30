package org.wit.playlistcreater.ui.maps

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.playlistcreater.R
import org.wit.playlistcreater.databinding.FragmentMapsBinding
import org.wit.playlistcreater.models.eventModel.EventModel
import org.wit.playlistcreater.utils.createLoader
import org.wit.playlistcreater.utils.hideLoader
import org.wit.playlistcreater.utils.showLoader


class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {

    private val mapsViewModel: MapsViewModel by activityViewModels()
    private var _fragBinding: FragmentMapsBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var loader: AlertDialog
    private var myMarker: Marker? = null

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel.map = googleMap
        mapsViewModel.map.isMyLocationEnabled = true
        mapsViewModel.currentLocation.observe(viewLifecycleOwner) {
            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )
            mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14f))
            mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
            mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true
        }
        mapsViewModel.eventLocations.observe(
            viewLifecycleOwner,
            Observer { events ->
                events?.let {
                    render(events)
                    hideLoader(loader)
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loader = createLoader(requireActivity())
        _fragBinding = FragmentMapsBinding.inflate(inflater, container, false)
        setCloseBinding(fragBinding)

        return fragBinding.root;
    }

    private fun setCloseBinding(fragBinding: FragmentMapsBinding) {
        fragBinding.closePopup.setOnClickListener {
            fragBinding.eventCard.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun render(eventList: ArrayList<EventModel>) {
        fragBinding.eventCard.visibility = View.GONE
        if (eventList.isNotEmpty()) {
            mapsViewModel.map.clear()
            eventList.forEach {
                mapsViewModel.map.setOnMarkerClickListener(this)
                myMarker = mapsViewModel.map.addMarker(
                    MarkerOptions().position(LatLng(it.latitude!!, it.longitude!!))
                )
                mapsViewModel.map.setOnMarkerClickListener(this)
                myMarker!!.tag = it.id
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader)
        mapsViewModel.updateEventList()
    }


    override fun onMarkerClick(p0: Marker): Boolean {
        for (event in mapsViewModel.eventLocations.value!!) {
            if (p0.tag == event.id) {
                fragBinding.eventCard.visibility = View.VISIBLE
                fragBinding.eventType.text = event.type.toString()
                fragBinding.EventDescription.text = event.description.toString()
                fragBinding.EventTimeAndDate.text = "${event.time} - ${event.date}"

                if (event.uid != mapsViewModel.currentUser!!.uid) {
                    fragBinding.deleteBtn.visibility = View.GONE
                    fragBinding.editBtn.visibility = View.GONE
                } else {
                    fragBinding.deleteBtn.visibility = View.VISIBLE
                    fragBinding.editBtn.visibility = View.VISIBLE
                    fragBinding.editBtn.setOnClickListener {
                        val action =
                            MapsFragmentDirections.actionMapsFragmentToCreateEventFragment()
                                .setEventId(event.id)
                        findNavController().navigate(action)
                    }
                    fragBinding.deleteBtn.setOnClickListener {
                        mapsViewModel.deleteEvent(event.id.toString())
                        Toast.makeText(
                            context, "Event Deleted", Toast.LENGTH_LONG
                        ).show()
                        fragBinding.eventCard.visibility = View.GONE
                        p0.isVisible = false
                    }
                }

                return true
            }
        }
        return false
        //https://stackoverflow.com/questions/14226453/google-maps-api-v2-how-to-make-markers-clickable
    }

}
