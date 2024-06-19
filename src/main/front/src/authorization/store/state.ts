import {User} from "../../types";

export interface AuthorizationState {
    readonly user: User | null
    readonly jwtToken: TokenState | null
    readonly refreshToken: TokenState | null
}

export interface TokenState {
    readonly token: string
    readonly expiresIn: number
}

const initialState: AuthorizationState = {
    user: null,
    jwtToken: null,
    refreshToken: null
}


export {
    initialState as initialAuthorizationState,
}