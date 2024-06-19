import {initialAuthorizationState, AuthorizationState} from "./state";
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
                jwtToken: {
                    token: action.payload.jwtToken.token,
                    expiresIn: action.payload.jwtToken.expiresIn
                },
                refreshToken: {
                    token: action.payload.refreshToken.token,
                    expiresIn: action.payload.refreshToken.expiresIn
                }
            }
        default:
            return state
    }
}

export {
    authorizationReducer
}