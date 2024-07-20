import {State} from "./state";
import { combineReducers, Reducer } from "redux";
import {eventsReducer} from "../events/store/reducers";
import {authorizationReducer} from "../authorization/store/reducers";

const rootReducer: Reducer<State> = combineReducers({
    eventsState: eventsReducer,
    authorizationState: authorizationReducer,
})

export { rootReducer }