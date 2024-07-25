import {initialEventsState, EventsState} from "../events/store/state";
import {initialAuthorizationState, AuthorizationState} from "../authorization/store/state";

export interface ApplicationState {
    readonly eventsState: EventsState
    readonly authorizationState: AuthorizationState
}


const initialState: ApplicationState = {
    eventsState: initialEventsState,
    authorizationState: initialAuthorizationState,
}

export {initialState}