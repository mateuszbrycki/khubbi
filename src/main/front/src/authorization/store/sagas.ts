import {Action} from "redux";
import {AuthorizationHttpApi} from "../api/api";
import {takeEvery} from '@redux-saga/core/effects'
import {LoginUser, RegisterUser, Types} from './actions'

function* onAuthorization(api: AuthorizationHttpApi): IterableIterator<unknown> {
     yield takeEvery((a: Action): a is RegisterUser => a.type === Types.RegisterUser, function* (a: RegisterUser) {
        console.log("Register Uer")
        yield api.register(a.payload.email, a.payload.password)
    })

    yield takeEvery((a: Action): a is LoginUser => a.type === Types.LoginUser, function* (a: LoginUser) {
        console.log("Login User")
        // TODO mateusz.brycki handle JWT
        yield api.login(a.payload.email, a.payload.password)
    })

}

export {
    onAuthorization
}