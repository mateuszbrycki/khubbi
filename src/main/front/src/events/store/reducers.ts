import {initialEventsState, EventsState} from "./state";
import {Action} from "redux";
import {CloseAddEventForm, EventsLoaded, OpenAddEventForm, Types} from "./actions";


type EventsActions =
    | EventsLoaded
    | OpenAddEventForm
    | CloseAddEventForm

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
        case Types.OpenAddEventForm:
            return {
                ...state,
                addEventForm: action.payload.formType
            }
        case Types.CloseAddEventForm:
            return {
                ...state,
                addEventForm: null
            }
        default:
            return state
    }
}

export {
    eventsReducer
}