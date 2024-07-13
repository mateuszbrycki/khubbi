import {Shelf} from "../../types";
import {List} from "immutable";

export interface ShelvesState {
    readonly shelves: List<Shelf>
}

const initialState: ShelvesState = {
    shelves: List<Shelf>()
}

export {
    initialState as initialShelvesState,
}