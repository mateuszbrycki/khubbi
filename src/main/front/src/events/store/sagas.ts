import {Action} from "redux";
import {EventsHttpApi} from "../api/api";
import {call, put, takeEvery} from '@redux-saga/core/effects'
import {LoadEvents, EventsLoadedAction, Types} from "./actions";
import {List} from "immutable";
import {Event} from "../../types";

function* fetchEvents(api: EventsHttpApi): Generator<any, any, List<Event>> {
    return yield api.fetchEvents()
        .then(response => response)
        .catch(err => {
            // TODO mateusz.brycki - dispatch error and show notification
            return List.of()
        })
}

function* loadEventsSaga(api: EventsHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is LoadEvents => a.type === Types.LoadEvents, function* (a: LoadEvents) {
        const response: List<Event> = yield call(fetchEvents, api);
        yield put(EventsLoadedAction(response));
    })
}

export {
    loadEventsSaga
}