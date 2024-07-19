import {Action} from "redux";
import {AuthorizationHttpApi, LoginResponse, LogoutResponse, RefreshTokenResponse, RegisterResponse} from "../api/api";
import {call, put, select, takeEvery} from '@redux-saga/core/effects'
import {
    CheckJWTExpired,
    LoginUser,
    LogoutUser,
    RefreshUserJWT,
    RefreshUserJWTAction,
    RegisterUser,
    Types,
    UserJWTTokenRefreshedAction,
    UserJWTTokenRefreshFailed,
    UserJWTTokenRefreshFailedAction,
    UserLoggedInAction,
    UserLoggedOutAction,
    UserRegisteredAction
} from './actions'
import {push} from "redux-first-history";
import {getRefreshToken, isAuthenticated, isRefreshTokenValid} from "./selectors";

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

function* callRefreshToken(api: AuthorizationHttpApi, refreshToken: string): Generator<any, any, RefreshTokenResponse> {
    return yield api.refreshToken(refreshToken)
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

    yield takeEvery((a: Action): a is RefreshUserJWT => a.type === Types.RefreshUserJWT, function* (a: RefreshUserJWT): any {

        const refreshToken = yield select(getRefreshToken)
        const response: RefreshTokenResponse = yield call(callRefreshToken, api, refreshToken);

        if (response) {
            yield put(UserJWTTokenRefreshedAction(response.jwtToken, response.expiresIn));
        } else {
            yield put(UserJWTTokenRefreshFailedAction())
        }

    })

    yield takeEvery((a: Action): a is UserJWTTokenRefreshFailed => a.type === Types.UserJWTTokenRefreshFailed, function* (a: UserJWTTokenRefreshFailed) {
        yield put(UserLoggedOutAction())
        yield put(push("/login"))
    })

    yield takeEvery((a: Action): a is CheckJWTExpired => a.type === Types.CheckJWTExpired, function* (a: CheckJWTExpired): any {
        const isUserAuthenticated = yield select(isAuthenticated)
        if (!isUserAuthenticated) {
            const isUserRefreshTokenValid = yield select(isRefreshTokenValid)

            if (isUserRefreshTokenValid) {
                yield put(RefreshUserJWTAction())
            } else {
                // TODO mateusz.brycki might logout user in the middle of browsing the page
                // TODO consider showing an error - unexpected error
                yield put(UserLoggedOutAction())
                yield put(push("/login"))
            }
        }
    })

}

export {
    onAuthorization
}