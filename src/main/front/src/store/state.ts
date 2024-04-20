import {initialShelvesState, ShelvesState} from "../shelves/store/state";
import {initialAuthorizationState, AuthorizationState} from "../authorization/store/state";

export interface State {
    readonly shelvesState: ShelvesState
    readonly authorizationState: AuthorizationState

}

const initialState: State = {
    shelvesState: initialShelvesState,
    authorizationState: initialAuthorizationState,
}

export { initialState }