import {User} from "../../types";

export interface AuthorizationState {
    readonly user: User | null
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