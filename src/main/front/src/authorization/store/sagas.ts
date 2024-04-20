import {Action} from "redux";
import {AuthorizationHttpApi} from "../api/api";
import {put, takeEvery, select} from '@redux-saga/core/effects'
import {Types, RegisterUser} from './actions'
import {List} from "immutable";

function* onAuthorization(api: AuthorizationHttpApi): IterableIterator<unknown> {
     yield takeEvery((a: Action): a is RegisterUser => a.type === Types.RegisterUser, function* (a: RegisterUser) {
        console.log("saga")
        yield api.register(a.payload.email, a.payload.password)
    })

}

export {
    onAuthorization
}