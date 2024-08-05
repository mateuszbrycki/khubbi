import {createSelector} from "reselect";
import {ApplicationState} from "../../store/state";
import {AlertsState} from "./state";

const getAlertsState = (state: ApplicationState): AlertsState => {
    return state.alertsState
}


const getAlerts = createSelector(
    getAlertsState,
    (state: AlertsState) => {
        return state.alerts
    }
)

export {getAlerts}