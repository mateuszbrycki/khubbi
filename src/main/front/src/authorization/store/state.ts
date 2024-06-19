export interface AuthorizationState {
    readonly jwtToken: TokenState | null
    readonly refreshToken: TokenState | null
}

export interface TokenState {
    readonly token: string
    readonly expiresIn: number
}

const initialState: AuthorizationState = {
    jwtToken: null,
    refreshToken: null
}


export {
    initialState as initialAuthorizationState,
}