import {Action} from "redux";
import {AlertMessage} from "../../types";
import {HideAlert, ShowAlert, Types} from "./actions";
import {AlertsState, initialAlertsState} from "./state";

type AlertsActions =
    | ShowAlert
    | HideAlert

const alertsReducer = (
    state: AlertsState | undefined = initialAlertsState,
    incomingAction: Action,
): AlertsState => {
    const action = incomingAction as AlertsActions
    switch (action.type) {
        case Types.ShowAlert:
            return {
                ...state,
                alerts: state?.alerts.push(action.payload.alertMessage)
            }
        case Types.HideAlert:
            return {
                ...state,
                alerts: state?.alerts.filter((alert: AlertMessage) => alert.id !== action.payload.alertMessage.id)
            }
        default:
            return state
    }
}

export {
    alertsReducer
}