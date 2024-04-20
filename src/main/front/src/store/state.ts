import {initialShelvesState, ShelvesState} from "../shelves/store/state";

export interface State {
    readonly shelvesState: ShelvesState

}

const initialState: State = {
    shelvesState: initialShelvesState,
}

export { initialState }