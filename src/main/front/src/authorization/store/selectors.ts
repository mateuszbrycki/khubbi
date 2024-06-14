import {createSelector} from 'reselect'
import {ApplicationState} from "../../store/state";
import {AuthorizationState} from "./state";

const getAuthorizationState = (state: ApplicationState): AuthorizationState => {
    return state.application.authorizationState
}
const getUserState = createSelector(
    getAuthorizationState,
    (state: AuthorizationState) => state?.user
)

const isAuthenticated = createSelector(
    getAuthorizationState,
    (state: AuthorizationState) => {
        // TODO mateusz.brycki compare expiresIn with the current date so that the token expires
        return state.jwtToken != null && state.expiresIn != null
    }
)

const getJWTToken = createSelector(
    getAuthorizationState,
    (state: AuthorizationState) => {
        return state.jwtToken
    }
)


export {getUserState, isAuthenticated, getJWTToken}