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

export {getUserState}