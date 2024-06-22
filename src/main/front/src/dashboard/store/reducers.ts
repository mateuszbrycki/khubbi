import {DashboardState, initialDashboardState} from "./state";
import {Action} from "redux";
import {LoadDashboard, Types} from "./actions";


type DashboardActions =
    | LoadDashboard

const dashboardReducer = (
    state: DashboardState | undefined = initialDashboardState,
    incomingAction: Action,
): DashboardState => {
    const action = incomingAction as DashboardActions
    switch (action.type) {
        case Types.LoadDashboard:
            return {
                ...state,
            }
        default:
            return state
    }
}

export {
    dashboardReducer
}