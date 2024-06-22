import {ApplicationState} from "../../store/state";
import {createSelector} from "reselect";
import {ShelvesState} from "./state";


const getShelvesState = (state: ApplicationState): ShelvesState => {
    return state.application.shelvesState
}


const getShelves = createSelector(
    getShelvesState,
    (state: ShelvesState) => {
        return state.shelves;
    }
)

export {getShelves}
