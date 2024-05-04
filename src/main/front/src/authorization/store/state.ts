import {User} from "../../types";

export interface AuthorizationState {
    readonly user: User | null
    // TODO mateusz.brycki encapsulate into object
    // TODO mateusz.brycki add more details into the returned object - user name etc.
    readonly jwtToken: string | null
    readonly expiresIn: number | null
}

const initialState: AuthorizationState = {
    user: null,
    jwtToken: null,
    expiresIn: null
}


export {
    initialState as initialAuthorizationState,
}