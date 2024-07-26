import React from "react";
import {Event, EventDate, EventForms} from "../../types";
import {List} from "immutable";
import EventsTimeline from "../../events/components/EventsTimeline";
import EventsManagement from "../../events/components/EventsManagement";

export interface DashboardProps {
    readonly events: List<Event>,
    readonly showEventForm: EventForms | null
}

export interface DashboardActionProps {
    readonly loadEvents: () => void,
    readonly openAddEventForm: (type: EventForms) => void,
    readonly closeAddEventForm: () => void
    readonly addNote: (note: string, date: EventDate) => void
    readonly addPhoto: (description: string, photo: File, date: EventDate) => void
}

const Dashboard: React.FC<DashboardProps & DashboardActionProps> = (props) => {
    const {events, showEventForm, loadEvents, openAddEventForm, closeAddEventForm, addNote, addPhoto} = props

    return <>
        <div className="d-flex justify-content-center">
            <EventsManagement showEventForm={showEventForm}
                              openAddEventForm={openAddEventForm}
                              closeAddEventForm={closeAddEventForm}
                              addNote={addNote}
                              addPhoto={addPhoto}
            />
        </div>

        <div className="d-flex justify-content-center">
            <EventsTimeline events={events} loadEvents={loadEvents}/>
        </div>
    </>
}

export default Dashboard