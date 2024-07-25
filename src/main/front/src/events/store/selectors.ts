import {ApplicationState} from "../../store/state";
import {createSelector} from "reselect";
import {EventsState} from "./state";


const getEventsState = (state: ApplicationState): EventsState => {
    return state.eventsState
}


const getEvents = createSelector(
    getEventsState,
    (state: EventsState) => {
        return state.events;
    }
)

const getOpenAddEventForm = createSelector(
    getEventsState,
    (state: EventsState) => {
        return state.addEventForm;
    }
)

export {getEvents, getOpenAddEventForm}
