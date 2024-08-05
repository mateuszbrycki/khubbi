import {AlertMessage} from "../../types";

enum Types {

    ShowAlert = "SHOW_ALERT",
    HideAlert = "HIDE_ALERT"
}

export interface ShowAlert {
    readonly type: Types.ShowAlert,
    readonly payload: {
        alertMessage: AlertMessage,
    }
}

export interface HideAlert {
    readonly type: Types.HideAlert,
    readonly payload: {
        alertMessage: AlertMessage,
    }
}

const ShowAlertAction = (alertMessage: AlertMessage): ShowAlert => ({
    type: Types.ShowAlert,
    payload: {
        alertMessage: alertMessage
    }
})

const HideAlertAction = (alertMessage: AlertMessage): HideAlert => ({
    type: Types.HideAlert,
    payload: {
        alertMessage: alertMessage
    }
})

export {
    Types,
    ShowAlertAction,
    HideAlertAction,
}