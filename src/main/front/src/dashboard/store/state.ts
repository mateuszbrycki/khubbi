import {User} from "../../types";

export interface DashboardState {
    readonly user: User | null
}

const initialState: DashboardState = {
    user: null,
}


export {
    initialState as initialDashboardState,
}