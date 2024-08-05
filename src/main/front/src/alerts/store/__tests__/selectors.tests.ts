import {initialState} from "../../../store/state";
import {initialAlertsState} from "../state";
import {List} from "immutable";
import {AlertMessage} from "../../../types";
import {getAlerts} from "../selectors";

describe('getAlerts', () => {
    test('should return alerts from store', () => {
        const alerts = List.of(AlertMessage.error("test-error"))
        const state = {
            ...initialState,
            alertsState: {
                ...initialAlertsState,
                alerts: alerts
            },

        }
        const result = getAlerts(state)
        expect(result).toEqual(alerts)
    })
})
