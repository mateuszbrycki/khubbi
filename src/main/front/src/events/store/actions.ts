import {Event, EventDate, EventForms} from "../../types";
import {List} from "immutable";

enum Types {
    LoadEvents = "LOAD_EVENTS",
    EventsLoaded = "EVENTS_LOADED",
    OpenAddEventForm = "OPEN_ADD_EVENT_FORM",
    CloseAddEventForm = "CLOSE_ADD_EVENT_FORM",
    AddNote = "ADD_NOTE",
}

export interface LoadEvents {
    readonly type: Types.LoadEvents
}

export interface EventsLoaded {
    readonly type: Types.EventsLoaded
    readonly payload: {
        events: List<Event>
    }
}

export interface OpenAddEventForm {
    readonly type: Types.OpenAddEventForm
    readonly payload: {
        formType: EventForms
    }
}

export interface CloseAddEventForm {
    readonly type: Types.CloseAddEventForm
}

export interface AddNote {
    readonly type: Types.AddNote
    readonly payload: {
        note: string,
        date: EventDate
    }
}

const LoadEventsAction = (): LoadEvents => ({
    type: Types.LoadEvents,
})

const EventsLoadedAction = (events: List<Event>): EventsLoaded => ({
    type: Types.EventsLoaded,
    payload: {
        events: events
    }
})

const OpenAddEventFormAction = (formType: EventForms) => ({
    type: Types.OpenAddEventForm,
    payload: {
        formType: formType
    }
})

const CloseAddEventFormAction = (): CloseAddEventForm => ({
    type: Types.CloseAddEventForm,
})

const AddNoteAction = (note: string, date: EventDate) => ({
    type: Types.AddNote,
    payload: {
        note: note,
        date: date
    }
})

export {
    Types,
    LoadEventsAction,
    EventsLoadedAction,
    OpenAddEventFormAction,
    CloseAddEventFormAction,
    AddNoteAction
}