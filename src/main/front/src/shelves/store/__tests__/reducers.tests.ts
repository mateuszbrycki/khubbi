import {initialShelvesState} from "../state";
import {shelvesReducer} from "../reducers";
import {ShelvesLoadedAction} from "../actions";
import {List} from "immutable";

// TODO mateusz.brycki shouldn't we tet the root reducer?
test('should return initial state', () => {
    expect(shelvesReducer(undefined, {type: 'unknown'})).toEqual({...initialShelvesState})
})

test('should store shelves list', () => {
    expect(shelvesReducer(initialShelvesState, ShelvesLoadedAction(List.of({
            name: "shelf-1",
            id: "shelf-1"
        }, {name: "shelf-2", id: "shelf-2"}))
    )).toEqual({
        ...initialShelvesState,
        shelves: List.of({name: "shelf-1", id: "shelf-1"}, {name: "shelf-2", id: "shelf-2"})
    })
})