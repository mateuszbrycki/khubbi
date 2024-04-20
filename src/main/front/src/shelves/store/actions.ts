import {Shelf} from "../../types";
import {List, Set} from "immutable";

enum Types {
    LoadShelves = "SHELVES_LOAD",
    ShelvesLoaded = "SHELVES_LOADED",
}

export interface LoadShelves {
    readonly type: Types.LoadShelves
}

export interface ShelvesLoaded {
    readonly type: Types.ShelvesLoaded
    readonly payload: {
        shelves: List<Shelf>
    }
}

const LoadShelvesAction = (): LoadShelves => ({
    type: Types.LoadShelves,
})

const ShelvesLoadedAction = (shelves: List<Shelf>): ShelvesLoaded => ({
    type: Types.ShelvesLoaded,
    payload: {
        shelves: shelves
    }
})

export {
    Types,
    LoadShelvesAction,
    ShelvesLoadedAction
}