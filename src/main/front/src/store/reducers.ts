import {State} from "./state";
import { combineReducers, Reducer } from "redux";
import {shelvesReducer} from "../shelves/store/reducers";

const rootReducer: Reducer<State> = combineReducers({
    shelvesState: shelvesReducer,
})

export { rootReducer }