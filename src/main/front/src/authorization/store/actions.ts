enum Types {
    RegisterUser = "REGISTER_USER",
    LoginUser = "LOGIN_USER",
    UserRegistered = "USER_REGISTERED",
    UserLoggedIn = "USER_LOGGED_IN",
    LogoutUser = "LOGOUT_USER",
    UserLoggedOut = "USER_LOGGED_OUT",
    UserJWTTokenRefreshed = "USER_JWT_TOKEN_REFRESHED",
    UserJWTTokenRefreshFailed = "USER_JWT_TOKEN_REFRESH_FAILED",
    CheckJWTExpired = "CHECK_JWT_EXPIRED",
    RefreshUserJWT = "REFRESH_USER_JWT",
}

export interface RegisterUser {
    readonly type: Types.RegisterUser
    readonly payload: {
        email: string,
        password: string,
        repeatedPassword: string
    }
}

export interface LoginUser {
    readonly type: Types.LoginUser
    readonly payload: {
        email: string,
        password: string,
    }
}

export interface UserRegistered {
    readonly type: Types.UserRegistered
    readonly payload: {
        id: string,
        email: string,
        createdAt: string,
    }
}

export interface UserLoggedIn {
    readonly type: Types.UserLoggedIn
    readonly payload: {
        jwtToken: { token: string, expiresIn: number },
        refreshToken: { token: string, expiresIn: number },
    }
}

export interface LogoutUser {
    readonly type: Types.LogoutUser
}

export interface UserLoggedOut {
    readonly type: Types.UserLoggedOut
}

export interface UserJWTTokenRefreshed {
    readonly [extraProps: string]: unknown;

    readonly type: Types.UserJWTTokenRefreshed
    readonly payload: {
        jwtToken: string,
        expiresIn: number
    }
}

export interface UserJWTTokenRefreshFailed {
    readonly [extraProps: string]: unknown;

    readonly type: Types.UserJWTTokenRefreshFailed
}

export interface CheckJWTExpired {
    readonly type: Types.CheckJWTExpired
}

export interface RefreshUserJWT {
    readonly [extraProps: string]: unknown;

    readonly type: Types.RefreshUserJWT
}

const RegisterUserAction = (email: string, password: string, repeatedPassword: string): RegisterUser => ({
    type: Types.RegisterUser,
    payload: {
        email: email,
        password: password,
        repeatedPassword: repeatedPassword,
    }
})

const UserRegisteredAction = (id: string, email: string, createdAt: string): UserRegistered => ({
    type: Types.UserRegistered,
    payload: {
        id: id,
        email: email,
        createdAt: createdAt,
    }
})

const LoginUserAction = (email: string, password: string): LoginUser => ({
    type: Types.LoginUser,
    payload: {
        email: email,
        password: password,
    }
})

const UserLoggedInAction = (jwtToken: { token: string, expiresIn: number }, refreshToken: {
    token: string,
    expiresIn: number
}): UserLoggedIn => ({
    type: Types.UserLoggedIn,
    payload: {
        jwtToken: {
            token: jwtToken.token,
            expiresIn: jwtToken.expiresIn
        },
        refreshToken: {
            token: refreshToken.token,
            expiresIn: refreshToken.expiresIn
        }
    }
})

const UserJWTTokenRefreshedAction = (jwtToken: string, expiresIn: number): UserJWTTokenRefreshed => ({
    type: Types.UserJWTTokenRefreshed,
    payload: {
        jwtToken: jwtToken,
        expiresIn: expiresIn
    }
})

const UserJWTTokenRefreshFailedAction = (): UserJWTTokenRefreshFailed => ({
    type: Types.UserJWTTokenRefreshFailed
})

const LogoutUserAction = (): LogoutUser => ({
    type: Types.LogoutUser,
})

const UserLoggedOutAction = (): UserLoggedOut => ({
    type: Types.UserLoggedOut
})

const CheckJWTExpiredAction = (): CheckJWTExpired => ({
    type: Types.CheckJWTExpired,
})

const RefreshUserJWTAction = (): RefreshUserJWT => ({
    type: Types.RefreshUserJWT,
})


export {
    Types,
    RegisterUserAction,
    LoginUserAction,
    UserRegisteredAction,
    UserLoggedInAction,
    LogoutUserAction,
    UserLoggedOutAction,
    UserJWTTokenRefreshedAction,
    UserJWTTokenRefreshFailedAction,
    CheckJWTExpiredAction,
    RefreshUserJWTAction
}