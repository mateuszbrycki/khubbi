import {initialEventsState, EventsState} from "./state";
import {Action} from "redux";
import {EventsLoaded, Types} from "./actions";


type EventsActions =
    | EventsLoaded

const eventsReducer = (
    state: EventsState | undefined = initialEventsState,
    incomingAction: Action,
): EventsState => {
    const action = incomingAction as EventsActions
    switch (action.type) {
        case Types.EventsLoaded:
            return {
                ...state,
                events: action.payload.events
            }
        default:
            return state
    }
}

export {
    eventsReducer
}