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
import {AlertMessage} from "../../types";
import {ShowAlertAction} from "../../alerts/store/actions";

function* callLogin(api: AuthorizationHttpApi, email: string, password: string): Generator<any, any, LoginResponse> {
    return yield api.login(email, password)
        .then(response => ({response}))
        .catch(error => ({error}))
}

function* callRegister(api: AuthorizationHttpApi, email: string, password: string, repeatedPassword: string): Generator<any, any, RegisterResponse> {
    return yield api.register(email, password, repeatedPassword)
        .then(response => ({response}))
        .catch(error => ({error}))
}

function* callLogout(api: AuthorizationHttpApi): Generator<any, any, LogoutResponse> {
    return yield api.logout()
        .then(response => ({response}))
        .catch(error => ({error}))
}

function* callRefreshToken(api: AuthorizationHttpApi, refreshToken: string): Generator<any, any, RefreshTokenResponse> {
    return yield api.refreshToken(refreshToken)
        .then(response => ({response}))
        .catch(error => ({error}))
}

function* registerUserSaga(api: AuthorizationHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is RegisterUser => a.type === Types.RegisterUser, function* (a: RegisterUser) {
        const {
            error,
            response
        } = yield call(callRegister, api, a.payload.email, a.payload.password, a.payload.repeatedPassword);

        if (response) {
            yield put(UserRegisteredAction(response.id, response.email, response.createdAt))
        } else {
            yield put(ShowAlertAction(AlertMessage.error("Error when registering user: " + error.response.data.message)))
        }
    })
}

function* loginUserSaga(api: AuthorizationHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is LoginUser => a.type === Types.LoginUser, function* (a: LoginUser) {
        const {error, response} = yield call(callLogin, api, a.payload.email, a.payload.password);
        if (response) {
            yield put(UserLoggedInAction(response.jwtToken, response.refreshToken))
            yield put(push("/dashboard"))
        } else {
            yield put(ShowAlertAction(AlertMessage.error(error.response.data.message)))
        }
    })
}

function* logoutUserSaga(api: AuthorizationHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is LogoutUser => a.type === Types.LogoutUser, function* (a: LogoutUser) {
        const {error, response} = yield call(callLogout, api);
        if (response) {
            yield put(UserLoggedOutAction())
            yield put(push("/login"))
        } else {
            yield put(ShowAlertAction(AlertMessage.error("Error when logging out: " + error.response.data.message)))
        }
    })

}

function* refreshJWTTokenSaga(api: AuthorizationHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is RefreshUserJWT => a.type === Types.RefreshUserJWT, function* (a: RefreshUserJWT): any {

        const refreshToken = yield select(getRefreshToken)
        const {error, response} = yield call(callRefreshToken, api, refreshToken);

        if (response) {
            yield put(UserJWTTokenRefreshedAction(response.jwtToken, response.expiresIn));
        } else {
            yield put(UserJWTTokenRefreshFailedAction())
        }

    })
}

function* userJWTTokenRefreshFailedSaga(api: AuthorizationHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is UserJWTTokenRefreshFailed => a.type === Types.UserJWTTokenRefreshFailed, function* (a: UserJWTTokenRefreshFailed) {
        yield put(UserLoggedOutAction())
        yield put(push("/login"))
    })
}


function* checkJWTExpiredSaga(api: AuthorizationHttpApi): IterableIterator<unknown> {
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
    registerUserSaga,
    loginUserSaga,
    logoutUserSaga,
    refreshJWTTokenSaga,
    userJWTTokenRefreshFailedSaga,
    checkJWTExpiredSaga
}