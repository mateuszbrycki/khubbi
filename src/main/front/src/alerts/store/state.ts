import {List} from "immutable";
import {AlertMessage} from "../../types";

export interface AlertsState {
    readonly alerts: List<AlertMessage>
}

const initialState: AlertsState = {
    alerts: List<AlertMessage>(),
}


export {
    initialState as initialAlertsState,
}