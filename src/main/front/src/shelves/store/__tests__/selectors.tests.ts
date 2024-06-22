import {List} from "immutable";
import {getShelves} from "../selectors";
import {initialShelvesState} from "../state";
import {initialAuthorizationState} from "../../../authorization/store/state";

test('should return shelves from state', () => {
    const state = {
        application: {
            authorizationState: initialAuthorizationState,
            shelvesState: {
                ...initialShelvesState,
                shelves: List.of({
                    name: "shelf-1",
                    id: "shelf-1"
                }, {name: "shelf-2", id: "shelf-2"})
            }
        }

    }

    const result = getShelves(state)
    expect(result).toEqual(List.of({
        name: "shelf-1",
        id: "shelf-1"
    }, {name: "shelf-2", id: "shelf-2"}))
})