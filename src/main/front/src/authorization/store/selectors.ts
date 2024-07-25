import {createSelector} from 'reselect'
import {ApplicationState} from "../../store/state";
import {AuthorizationState} from "./state";

const getAuthorizationState = (state: ApplicationState): AuthorizationState => {
    return state.authorizationState
}

// this selector cannot be memoized since it operates both on state and the current time
const isAuthenticated = (appState: ApplicationState) => {
    const state: AuthorizationState = getAuthorizationState(appState)
    return state.jwtToken != null && state.jwtToken.token != null && state.jwtToken.expiresIn > Date.now()
}

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

const isRefreshTokenValid = createSelector(
    getAuthorizationState,
    (state: AuthorizationState) => {
        return state.refreshToken != null && state.refreshToken.token != null && state.refreshToken.expiresIn > Date.now()
    }
)


export {isAuthenticated, getJWTToken, getRefreshToken, isRefreshTokenValid}