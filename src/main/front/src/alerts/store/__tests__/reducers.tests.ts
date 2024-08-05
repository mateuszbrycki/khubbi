import {alertsReducer} from "../reducers";
import {initialAlertsState} from "../state";
import {HideAlertAction, ShowAlertAction} from "../actions";
import {AlertMessage} from "../../../types";
import {List} from "immutable";


test('should store Alert Message', () => {
    const alert = AlertMessage.success("test-success")
    expect(alertsReducer(initialAlertsState, ShowAlertAction(alert)))
        .toEqual({
            ...initialAlertsState,
            alerts: List.of(alert)
        })
})

test('should store multiple Alert Messages', () => {
    const alert1 = AlertMessage.success("test-success")
    const alert2 = AlertMessage.error("test-error")
    expect(alertsReducer({
        ...initialAlertsState,
        alerts: List.of(alert1)
    }, ShowAlertAction(alert2)))
        .toEqual({
            ...initialAlertsState,
            alerts: List.of(alert1, alert2)
        })
})

test('should return empty list when removing non existent alert', () => {
    expect(alertsReducer(initialAlertsState, HideAlertAction(AlertMessage.success("test-success"))))
        .toEqual({
            ...initialAlertsState,
            alerts: List.of()
        })
})

test('should remove alert from the list', () => {
    const alert = AlertMessage.success("test-success");
    expect(alertsReducer({
        ...initialAlertsState,
        alerts: List.of(alert)
    }, HideAlertAction(alert)))
        .toEqual({
            ...initialAlertsState,
            alerts: List.of()
        })
})
test('should remove only one alert from the list', () => {
    const alert1 = AlertMessage.success("test-success");
    const alert2 = AlertMessage.error("test-error");
    expect(alertsReducer({
        ...initialAlertsState,
        alerts: List.of(alert1, alert2)
    }, HideAlertAction(alert2)))
        .toEqual({
            ...initialAlertsState,
            alerts: List.of(alert1)
        })
})