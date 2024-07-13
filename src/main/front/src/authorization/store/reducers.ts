import {AuthorizationState, initialAuthorizationState} from "./state";
import {Action} from "redux";
import {Types, UserJWTTokenRefreshed, UserLoggedIn, UserLoggedOut} from "./actions";


type AuthorizationActions =
    | UserLoggedIn
    | UserLoggedOut
    | UserJWTTokenRefreshed

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
        case Types.UserJWTTokenRefreshed:
            return {
                ...state,
                jwtToken: {
                    token: action.payload.jwtToken,
                    expiresIn: action.payload.expiresIn
                }
            }
        default:
            return state
    }
}

export {
    authorizationReducer
}