import {List} from "immutable";
import {getEvents, getOpenAddEventForm} from "../selectors";
import {initialEventsState} from "../state";
import {initialAuthorizationState} from "../../../authorization/store/state";
import {EventDate, EventForms} from "../../../types";

test('should return events from state', () => {
    const state = {
        authorizationState: initialAuthorizationState,
        eventsState: {
            ...initialEventsState,
            events: List.of({
                note: "event-1",
                date: EventDate.ofDateAndTime("2024-07-12T20:00"),
                id: "event-1"
            }, {
                note: "event-2",
                date: EventDate.ofDateAndTime("2024-07-12T21:00"),
                id: "event-2"
            })
        }

    }

    const result = getEvents(state)
    expect(result).toEqual(List.of({
        note: "event-1",
        date: EventDate.ofDateAndTime("2024-07-12T20:00"),
        id: "event-1"
    }, {
        note: "event-2",
        date: EventDate.ofDateAndTime("2024-07-12T21:00"),
        id: "event-2"
    }))
})

test('should return open add event form', () => {
    const state = {
        authorizationState: initialAuthorizationState,
        eventsState: {
            ...initialEventsState,
            addEventForm: EventForms.NOTE
        }

    }

    const result = getOpenAddEventForm(state)
    expect(result).toEqual(EventForms.NOTE)
})