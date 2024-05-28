import {Action} from "redux";
import {takeEvery} from '@redux-saga/core/effects'
import {LoadShelves, Types} from './actions'
import {DashboardHttpApi} from "../api/api";


function* onDashboard(api: DashboardHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is LoadShelves => a.type === Types.LoadShelves, function* (a: LoadShelves) {
        // const response: RegisterResponse = yield call(callRegister, api, a.payload.email, a.payload.password);
        // yield put(UserRegisteredAction(response.id, response.email, response.createdAt))
    })


}

export {
    onDashboard
}