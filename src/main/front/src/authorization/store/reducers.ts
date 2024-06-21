import {AuthorizationState, initialAuthorizationState} from "./state";
import {Action} from "redux";
import {Types, UserLoggedIn, UserLoggedOut} from "./actions";


type AuthorizationActions =
    | UserLoggedIn
    | UserLoggedOut

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
        case Types.UserLoggedOut:
            return {
                ...state,
                jwtToken: null,
                refreshToken: null
            }
        default:
            return state
    }
}

export {
    authorizationReducer
}