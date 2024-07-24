import React from "react";
import {Event, EventForms} from "../../types";
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
    readonly addNote: (note: string, date: string) => void
}

const Dashboard: React.FC<DashboardProps & DashboardActionProps> = (props) => {
    const {events, showEventForm, loadEvents, openAddEventForm, closeAddEventForm, addNote} = props

    return <>
        <div className="d-flex justify-content-center">
            <EventsManagement showEventForm={showEventForm}
                              openAddEventForm={openAddEventForm}
                              closeAddEventForm={closeAddEventForm}
                              addNote={addNote}
            />
        </div>

        <div className="d-flex justify-content-center">
            <EventsTimeline events={events} loadEvents={loadEvents}/>
        </div>
    </>
}

export default Dashboard