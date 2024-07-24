import {List} from "immutable";
import {getEvents, getOpenAddEventForm} from "../selectors";
import {initialEventsState} from "../state";
import {initialAuthorizationState} from "../../../authorization/store/state";
import {EventForms} from "../../../types";

test('should return events from state', () => {
    const state = {
        application: {
            authorizationState: initialAuthorizationState,
            eventsState: {
                ...initialEventsState,
                events: List.of({
                    note: "event-1",
                    date: "22/07/2025",
                    id: "event-1"
                }, {
                    note: "event-2",
                    date: "22/07/2024",
                    id: "event-2"
                })
            }
        }

    }

    const result = getEvents(state)
    expect(result).toEqual(List.of({
        note: "event-1",
        date: "22/07/2025",
        id: "event-1"
    }, {
        note: "event-2",
        date: "22/07/2024",
        id: "event-2"
    }))
})

test('should return open add event form', () => {
    const state = {
        application: {
            authorizationState: initialAuthorizationState,
            eventsState: {
                ...initialEventsState,
                addEventForm: EventForms.NOTE
            }
        }

    }

    const result = getOpenAddEventForm(state)
    expect(result).toEqual(EventForms.NOTE)
})