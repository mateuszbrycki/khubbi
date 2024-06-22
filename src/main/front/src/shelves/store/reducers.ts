import {initialShelvesState, ShelvesState} from "./state";
import {Action} from "redux";
import {ShelvesLoaded, Types} from "./actions";


type ShelvesActions =
    | ShelvesLoaded

const shelvesReducer = (
    state: ShelvesState | undefined = initialShelvesState,
    incomingAction: Action,
): ShelvesState => {
    const action = incomingAction as ShelvesActions
    switch (action.type) {
        case Types.ShelvesLoaded:
            return {
                ...state,
                shelves: action.payload.shelves
            }
        default:
            return state
    }
}

export {
    shelvesReducer
}