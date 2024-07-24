import {Event, EventForms} from "../../types";
import {List} from "immutable";

export interface EventsState {
    readonly events: List<Event>
    readonly addEventForm: EventForms | null
}

const initialState: EventsState = {
    events: List<Event>(),
    addEventForm: null
}

export {
    initialState as initialEventsState,
}