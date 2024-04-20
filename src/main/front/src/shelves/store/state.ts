import {Shelf} from "../../types";
import {List,Set} from "immutable";

export interface ShelvesListState {
    readonly shelves: List<Shelf>
}

export interface ShelvesState {
    readonly shelves: ShelvesListState
}

const initialShelvesListState: ShelvesListState = {
    shelves: List()
}


const initialState: ShelvesState = {
    shelves: initialShelvesListState
}

export {
    initialState as initialShelvesState,
    initialShelvesListState
}