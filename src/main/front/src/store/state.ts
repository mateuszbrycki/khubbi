import {initialShelvesState, ShelvesState} from "../shelves/store/state";
import {initialAuthorizationState, AuthorizationState} from "../authorization/store/state";

export interface ApplicationState {
    readonly application: State
}

export interface State {
    readonly shelvesState: ShelvesState
    readonly authorizationState: AuthorizationState

}

const initialState: ApplicationState = {
    application: {
        shelvesState: initialShelvesState,
        authorizationState: initialAuthorizationState,
    }
}

export {initialState}