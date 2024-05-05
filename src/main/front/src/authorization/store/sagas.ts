import {Action} from "redux";
import {AuthorizationHttpApi, LoginResponse, RegisterResponse} from "../api/api";
import {call, put, takeEvery} from '@redux-saga/core/effects'
import {LoginUser, RegisterUser, Types, UserLoggedInAction, UserRegisteredAction} from './actions'
import {JWT_TOKEN_STORAGE_KEY} from "../../common/variables";

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

function* onAuthorization(api: AuthorizationHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is RegisterUser => a.type === Types.RegisterUser, function* (a: RegisterUser) {
        const response: RegisterResponse = yield call(callRegister, api, a.payload.email, a.payload.password);
        yield put(UserRegisteredAction(response.id, response.email, response.createdAt))
    })

    yield takeEvery((a: Action): a is LoginUser => a.type === Types.LoginUser, function* (a: LoginUser) {
        const response: LoginResponse = yield call(callLogin, api, a.payload.email, a.payload.password);
        yield put(UserLoggedInAction(response.token, response.expiresIn))
        localStorage.setItem(JWT_TOKEN_STORAGE_KEY, response.token)
    })

}

export {
    onAuthorization
}