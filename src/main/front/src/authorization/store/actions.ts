enum Types {
    RegisterUser = "REGISTER_USER",
    LoginUser = "LOGIN_USER",
    UserRegistered = "USER_REGISTERED",
    UserLoggedIn = "USER_LOGGED_IN",
}

export interface RegisterUser {
    readonly type: Types.RegisterUser
    readonly payload: {
        email: string,
        password: string,
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
        jwtToken: string,
        expiresIn: number,
    }
}

const RegisterUserAction = (email: string, password: string): RegisterUser => ({
    type: Types.RegisterUser,
    payload: {
        email: email,
        password: password,
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

const UserLoggedInAction = (jwtToken: string, expiresIn: number): UserLoggedIn => ({
    type: Types.UserLoggedIn,
    payload: {
        jwtToken: jwtToken,
        expiresIn: expiresIn,
    }
})



export {
    Types,
    RegisterUserAction,
    LoginUserAction,
    UserRegisteredAction,
    UserLoggedInAction
}