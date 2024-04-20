import {initialShelvesListState, ShelvesListState, ShelvesState } from "./state";
import {Action, combineReducers, Reducer} from "redux";

const shelvesListReducer = (
    state: ShelvesListState | undefined = initialShelvesListState,
    incomingAction: Action,
): ShelvesListState => {
    /* const action = incomingAction as RidesListActions
    switch (action.type) {
        default:
            return state
    } */
    return state
}

const shelvesReducer: Reducer<ShelvesState> = combineReducers({
        shelves: shelvesListReducer
    }
)

export {
    shelvesReducer
}