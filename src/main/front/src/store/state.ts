import {EventsState, initialEventsState} from "../events/store/state";
import {AuthorizationState, initialAuthorizationState} from "../authorization/store/state";
import {AlertsState, initialAlertsState} from "../alerts/store/state";

export interface ApplicationState {
    readonly eventsState: EventsState
    readonly authorizationState: AuthorizationState
    readonly alertsState: AlertsState
}


const initialState: ApplicationState = {
    eventsState: initialEventsState,
    authorizationState: initialAuthorizationState,
    alertsState: initialAlertsState
}

export {initialState}