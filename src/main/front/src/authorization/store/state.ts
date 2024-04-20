import {User} from "../../types";
import {List,Set} from "immutable";

export interface AuthorizationState {
    readonly user: User | null
}

const initialState: AuthorizationState = {
    user: null
}


export {
    initialState as initialAuthorizationState,
}