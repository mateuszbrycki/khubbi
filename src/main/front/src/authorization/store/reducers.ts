import {initialAuthorizationState, AuthorizationState } from "./state";
import {Action, combineReducers, Reducer} from "redux";
import {Types, UserLoggedIn} from "./actions";


type AuthorizationActions =
    | UserLoggedIn

const authorizationReducer = (
    state: AuthorizationState | undefined = initialAuthorizationState,
    incomingAction: Action,
): AuthorizationState => {
    const action = incomingAction as AuthorizationActions
    switch (action.type) {
        case Types.UserLoggedIn:
            return {
                ...state,
                jwtToken: action.payload.jwtToken,
                expiresIn: action.payload.expiresIn
            }
        default:
            return state
    }
}

export {
    authorizationReducer
}