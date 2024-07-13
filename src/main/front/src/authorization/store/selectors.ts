import {createSelector} from 'reselect'
import {ApplicationState} from "../../store/state";
import {AuthorizationState} from "./state";

const getAuthorizationState = (state: ApplicationState): AuthorizationState => {
    return state.application.authorizationState
}

const isAuthenticated = createSelector(
    getAuthorizationState,
    (state: AuthorizationState) => {
        return state.jwtToken != null && state.jwtToken.token != null && state.jwtToken.expiresIn > Date.now()
    }
)

const getJWTToken = createSelector(
    getAuthorizationState,
    (state: AuthorizationState) => {
        return state.jwtToken?.token
    }
)

const getRefreshToken = createSelector(
    getAuthorizationState,
    (state: AuthorizationState) => {
        return state.refreshToken?.token
    }
)


export {isAuthenticated, getJWTToken, getRefreshToken}