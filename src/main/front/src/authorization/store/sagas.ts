import {Action} from "redux";
import {AuthorizationHttpApi, LoginResponse, LogoutResponse, RegisterResponse} from "../api/api";
import {call, put, takeEvery} from '@redux-saga/core/effects'
import {
    LoginUser,
    LogoutUser,
    RegisterUser,
    Types,
    UserJWTTokenRefreshFailed,
    UserLoggedInAction,
    UserLoggedOutAction,
    UserRegisteredAction
} from './actions'
import {push} from "redux-first-history";

function* callLogin(api: AuthorizationHttpApi, email: string, password: string): Generator<any, any, LoginResponse> {
    return yield api.login(email, password)
        .then(response => {
            return response
        })
        // TODO mateusz.brycki - dispatch error and show notification
        .catch(err => console.error(err))
}

function* callRegister(api: AuthorizationHttpApi, email: string, password: string): Generator<any, any, RegisterResponse> {
    return yield api.register(email, password)
        .then(response => {
            return response
        })
        // TODO mateusz.brycki - dispatch error and show notification
        .catch(err => console.error(err))
}

function* callLogout(api: AuthorizationHttpApi): Generator<any, any, LogoutResponse> {
    return yield api.logout()
        .then(response => {
            return response
        })
        // TODO mateusz.brycki - dispatch error and show notification
        .catch(err => console.error(err))
}

function* onAuthorization(api: AuthorizationHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is RegisterUser => a.type === Types.RegisterUser, function* (a: RegisterUser) {
        const response: RegisterResponse = yield call(callRegister, api, a.payload.email, a.payload.password);
        yield put(UserRegisteredAction(response.id, response.email, response.createdAt))
    })

    yield takeEvery((a: Action): a is LoginUser => a.type === Types.LoginUser, function* (a: LoginUser) {
        const response: LoginResponse = yield call(callLogin, api, a.payload.email, a.payload.password);
        yield put(UserLoggedInAction(response.jwtToken, response.refreshToken))
        yield put(push("/dashboard"))
    })

    yield takeEvery((a: Action): a is LogoutUser => a.type === Types.LogoutUser, function* (a: LogoutUser) {
        const response: LogoutResponse = yield call(callLogout, api);
        yield put(UserLoggedOutAction())
        yield put(push("/login"))
    })

    yield takeEvery((a: Action): a is UserJWTTokenRefreshFailed => a.type === Types.UserJWTTokenRefreshFailed, function* (a: UserJWTTokenRefreshFailed) {
        yield put(UserLoggedOutAction())
        yield put(push("/login"))
    })
}

export {
    onAuthorization
}