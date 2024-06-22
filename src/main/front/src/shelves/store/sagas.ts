import {Action} from "redux";
import {ShelvesHttpApi} from "../api/api";
import {call, put, takeEvery} from '@redux-saga/core/effects'
import {LoadShelves, ShelvesLoadedAction, Types} from "./actions";
import {List} from "immutable";
import {Shelf} from "../../types";

function* fetchShelves(api: ShelvesHttpApi): Generator<any, any, List<Shelf>> {
    return yield api.fetchShelves()
        .then(response => response)
        .catch(err => {
            // TODO mateusz.brycki - dispatch error and show notification
            console.error(err)
        })
}

function* onLoadShelves(api: ShelvesHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is LoadShelves => a.type === Types.LoadShelves, function* (a: LoadShelves) {
        const response: List<Shelf> = yield call(fetchShelves, api);
        yield put(ShelvesLoadedAction(response))
    })
}

export {
    onLoadShelves
}