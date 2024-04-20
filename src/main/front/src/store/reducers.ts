import {State} from "./state";
import { combineReducers, Reducer } from "redux";
import {shelvesReducer} from "../shelves/store/reducers";
import {authorizationReducer} from "../authorization/store/reducers";

const rootReducer: Reducer<State> = combineReducers({
    shelvesState: shelvesReducer,
    authorizationState: authorizationReducer,
})

export { rootReducer }