import {initialAuthorizationState, AuthorizationState } from "./state";
import {Action, combineReducers, Reducer} from "redux";

const authorizationReducer = (
    state: AuthorizationState | undefined = initialAuthorizationState,
    incomingAction: Action,
): AuthorizationState => {
    /* const action = incomingAction as RidesListActions
    switch (action.type) {
        default:
            return state
    } */
    return state
}

export {
    authorizationReducer
}