import {DashboardState, initialDashboardState} from "./state";
import {Action} from "redux";
import {LoadShelves, Types} from "./actions";


type DashboardActions =
    | LoadShelves

const dashboardReducer = (
    state: DashboardState | undefined = initialDashboardState,
    incomingAction: Action,
): DashboardState => {
    const action = incomingAction as DashboardActions
    switch (action.type) {
        case Types.LoadShelves:
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