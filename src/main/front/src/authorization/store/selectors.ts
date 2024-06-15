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
        return state.jwtToken != null && state.expiresIn != null && state.expiresIn > Date.now()
    }
)

const getJWTToken = createSelector(
    getAuthorizationState,
    (state: AuthorizationState) => {
        return state.jwtToken
    }
)


export {getUserState, isAuthenticated, getJWTToken}