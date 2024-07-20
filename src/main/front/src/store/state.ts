import {initialEventsState, EventsState} from "../events/store/state";
import {initialAuthorizationState, AuthorizationState} from "../authorization/store/state";

export interface ApplicationState {
    readonly application: State
}

export interface State {
    readonly eventsState: EventsState
    readonly authorizationState: AuthorizationState

}

const initialState: ApplicationState = {
    application: {
        eventsState: initialEventsState,
        authorizationState: initialAuthorizationState,
    }
}

export {initialState}