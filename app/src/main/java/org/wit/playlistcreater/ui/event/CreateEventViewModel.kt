package org.wit.playlistcreater.ui.event

import androidx.lifecycle.ViewModel
import org.wit.playlistcreater.models.AppManager
import org.wit.playlistcreater.models.eventModel.EventModel

class CreateEventViewModel : ViewModel() {

    fun createEvent(event: EventModel) {
        AppManager.createEvent(event)
    }

    fun updateEvent(event: EventModel, updatedEvent: EventModel) {
        AppManager.updateEvent(event, updatedEvent)
    }

    fun getEvent(id: String): EventModel? {
        return AppManager.getEventById(id)
    }

}