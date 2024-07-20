import {initialEventsState} from "../state";
import {eventsReducer} from "../reducers";
import {EventsLoadedAction} from "../actions";
import {List} from "immutable";

// TODO mateusz.brycki shouldn't we tet the root reducer?
test('should return initial state', () => {
    expect(eventsReducer(undefined, {type: 'unknown'})).toEqual({...initialEventsState})
})

test('should store events list', () => {
    expect(eventsReducer(initialEventsState, EventsLoadedAction(List.of({
            name: "event-1",
            id: "event-1"
        }, {name: "event-2", id: "event-2"}))
    )).toEqual({
        ...initialEventsState,
        events: List.of({name: "event-1", id: "event-1"}, {name: "event-2", id: "event-2"})
    })
})