import {Event} from "../../types";
import {List} from "immutable";

export interface EventsState {
    readonly events: List<Event>
}

const initialState: EventsState = {
    events: List<Event>()
}

export {
    initialState as initialEventsState,
}