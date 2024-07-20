import {List} from "immutable";
import {getEvents} from "../selectors";
import {initialEventsState} from "../state";
import {initialAuthorizationState} from "../../../authorization/store/state";

test('should return events from state', () => {
    const state = {
        application: {
            authorizationState: initialAuthorizationState,
            eventsState: {
                ...initialEventsState,
                events: List.of({
                    name: "event-1",
                    id: "event-1"
                }, {name: "event-2", id: "event-2"})
            }
        }

    }

    const result = getEvents(state)
    expect(result).toEqual(List.of({
        name: "event-1",
        id: "event-1"
    }, {name: "event-2", id: "event-2"}))
})