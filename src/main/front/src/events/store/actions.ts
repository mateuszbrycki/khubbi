import {Event} from "../../types";
import {List, Set} from "immutable";

enum Types {
    LoadEvents = "LOAD_EVENTS",
    EventsLoaded = "EVENTS_LOADED",
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

const LoadEventsAction = (): LoadEvents => ({
    type: Types.LoadEvents,
})

const EventsLoadedAction = (events: List<Event>): EventsLoaded => ({
    type: Types.EventsLoaded,
    payload: {
        events: events
    }
})

export {
    Types,
    LoadEventsAction,
    EventsLoadedAction
}