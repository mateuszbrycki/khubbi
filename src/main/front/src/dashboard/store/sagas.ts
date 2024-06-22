import {Action} from "redux";
import {takeEvery} from '@redux-saga/core/effects'
import {LoadDashboard, Types} from './actions'
import {DashboardHttpApi} from "../api/api";


function* onDashboard(api: DashboardHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is LoadDashboard => a.type === Types.LoadDashboard, function* (a: LoadDashboard) {
    })
}

export {
    onDashboard
}