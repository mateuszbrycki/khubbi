import {createSelector} from 'reselect'
import {State} from "../../store/state";
import {AuthorizationState} from "./state";

const getAuthorizationState = (state: State): AuthorizationState => state.authorizationState
const getUserState = createSelector(
    getAuthorizationState,
    (state: AuthorizationState) => state.user
)

export { getUserState }