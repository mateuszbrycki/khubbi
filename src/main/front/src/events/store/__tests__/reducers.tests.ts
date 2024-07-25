import {initialEventsState} from "../state";
import {eventsReducer} from "../reducers";
import {CloseAddEventFormAction, EventsLoadedAction, OpenAddEventFormAction} from "../actions";
import {List} from "immutable";
import {EventDate, EventForms} from "../../../types";

// TODO mateusz.brycki shouldn't we tet the root reducer?
test('should return initial state', () => {
    expect(eventsReducer(undefined, {type: 'unknown'})).toEqual({...initialEventsState})
})

test('should store events list', () => {
    expect(eventsReducer(initialEventsState, EventsLoadedAction(List.of({
            note: "event-1",
            date: EventDate.ofDateAndTime("2024-07-12T20:00"),
            id: "event-1"
        }, {
            note: "event-2",
            date: EventDate.ofDateAndTime("2024-07-12T21:00"),
            id: "event-2"
        }))
    )).toEqual({
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
    })
})

test('should store opened event form', () => {
    expect(eventsReducer(initialEventsState, OpenAddEventFormAction(EventForms.NOTE)
    )).toEqual({
        ...initialEventsState,
        addEventForm: EventForms.NOTE
    })
})

test('should remove opened event form', () => {
    expect(eventsReducer(initialEventsState, CloseAddEventFormAction()
    )).toEqual({
        ...initialEventsState,
        addEventForm: null
    })
})