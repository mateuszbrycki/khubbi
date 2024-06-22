enum Types {
    LoadDashboard = "LOAD_DASHBOARD",
}

export interface LoadDashboard {
    readonly type: Types.LoadDashboard
    readonly payload: {}
}


const LoadDashboardAction = (): LoadDashboard => ({
    type: Types.LoadDashboard,
    payload: {}
})


export {
    Types,
    LoadDashboardAction,
}