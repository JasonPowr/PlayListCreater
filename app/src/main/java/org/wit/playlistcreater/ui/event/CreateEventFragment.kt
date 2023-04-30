package org.wit.playlistcreater.ui.event

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.wit.playlistcreater.databinding.FragmentCreateEventBinding
import org.wit.playlistcreater.models.eventModel.EventModel
import org.wit.playlistcreater.ui.maps.MapsViewModel


class CreateEventFragment : Fragment() {

    private val args by navArgs<CreateEventFragmentArgs>()
    private lateinit var createEventViewModel: CreateEventViewModel
    private var _fragBinding: FragmentCreateEventBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val mapsViewModel: MapsViewModel by activityViewModels()
    var date = ""
    var event = EventModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentCreateEventBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        createEventViewModel = ViewModelProvider(this)[CreateEventViewModel::class.java]
        createEventBtn(fragBinding)

        fragBinding.eventDate.setOnDateChangeListener { _, i, i1, i2 ->
            date = "" + i2 + "/" + (i1 + 1) + "/" + i
        }//https://www.digitalocean.com/community/tutorials/android-calendar-view


        if (args.eventId != "default") {
            event = createEventViewModel.getEvent(args.eventId.toString())!!
            fragBinding.text.text = "Update the Event"
            fragBinding.createEventBtn.text = "Update the event"
            fragBinding.eventDescription.setText(event.description)
            fragBinding.typeText.setText(event.type)
        }

        return root;
    }


    fun createEventBtn(layout: FragmentCreateEventBinding) {
        layout.createEventBtn.setOnClickListener {
            val eventDescription = layout.eventDescription.text.toString()
            val hour: String = layout.timePicker1.hour.toString()
            val min: String =
                layout.timePicker1.minute.toString() //https://www.tutorialspoint.com/android/android_timepicker_control.htm#:~:text=Android%20Time%20Picker%20allows%20you,this%20functionality%20through%20TimePicker%20class.
            val time = "$hour:$min"
            val type = layout.typeText.text.toString()

            if (eventDescription.isNotBlank() && type.isNotBlank() && (date != "")) {
                if (args.eventId == "default") {
                    createEventViewModel.createEvent(
                        EventModel(
                            "",
                            "",
                            eventDescription,
                            date,
                            time,
                            type,
                            mapsViewModel.currentLocation.value!!.latitude,
                            mapsViewModel.currentLocation.value!!.longitude
                        )
                    )
                    Toast.makeText(
                        context, "Event Created!", Toast.LENGTH_LONG
                    ).show()
                } else {
                    val updatedEvent = EventModel(
                        event.id,
                        event.uid,
                        eventDescription,
                        date,
                        time,
                        type,
                        mapsViewModel.currentLocation.value!!.latitude,
                        mapsViewModel.currentLocation.value!!.longitude
                    )
                    createEventViewModel.updateEvent(event, updatedEvent)
                    Toast.makeText(
                        context, "Event Updated!", Toast.LENGTH_LONG
                    ).show()
                    findNavController().popBackStack()
                }

            } else {
                Toast.makeText(
                    context, "Please fill out everything", Toast.LENGTH_LONG
                ).show()
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}