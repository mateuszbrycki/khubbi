import {Action} from "redux";
import {AuthorizationHttpApi} from "../api/api";
import {put, takeEvery} from '@redux-saga/core/effects'
import {LoginUser, RegisterUser, Types, UserLoggedInAction} from './actions'


function* onAuthorization(api: AuthorizationHttpApi): IterableIterator<unknown> {
     yield takeEvery((a: Action): a is RegisterUser => a.type === Types.RegisterUser, function* (a: RegisterUser) {
        console.log("Register Uer")
        yield api.register(a.payload.email, a.payload.password)
    })

    yield takeEvery((a: Action): a is LoginUser => a.type === Types.LoginUser, function* (a: LoginUser) {
        console.log("Login User")

        api.login(a.payload.email, a.payload.password)
            .then(response =>  {
                 put(UserLoggedInAction(response.token, response.expiresIn))
            })
            .catch(err => console.error(err))
    })

}

export {
    onAuthorization
}