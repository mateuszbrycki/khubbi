import {Shelf} from "../../types";
import {List} from "immutable";

export interface ShelvesState {
    readonly shelves: List<Shelf>
}

const initialState: ShelvesState = {
    shelves: List()
}

export {
    initialState as initialShelvesState,
}