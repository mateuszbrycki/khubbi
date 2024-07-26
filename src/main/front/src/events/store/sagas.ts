import {Action} from "redux";
import {EventsHttpApi} from "../api/api";
import {call, put, takeEvery} from '@redux-saga/core/effects'
import {AddNote, AddPhoto, EventsLoadedAction, LoadEvents, LoadEventsAction, Types} from "./actions";
import {List} from "immutable";
import {Event, EventDate} from "../../types";

function* fetchEvents(api: EventsHttpApi): Generator<any, any, List<Event>> {
    return yield api.fetchEvents()
        .then(response => response)
        .catch(err => {
            // TODO mateusz.brycki - dispatch error and show notification
            return List.of()
        })
}

function* addNote(api: EventsHttpApi, note: string, date: EventDate): Generator<any, any, List<Event>> {
    return yield api.addEvent(note, date)
        .then(response => response)
        .catch(err => {
            // TODO mateusz.brycki - dispatch error and show notification
            return null
        })
}

function* addPhoto(api: EventsHttpApi, description: string, photo: File, date: EventDate): Generator<any, any, List<Event>> {
    return yield api.addPhoto(description, photo, date)
        .then(response => response)
        .catch(err => {
            // TODO mateusz.brycki - dispatch error and show notification
            return null
        })
}

function* loadEventsSaga(api: EventsHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is LoadEvents => a.type === Types.LoadEvents, function* (a: LoadEvents) {
        const response: List<Event> = yield call(fetchEvents, api);
        yield put(EventsLoadedAction(response));
    })
}

function* addNoteSaga(api: EventsHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is AddNote => a.type === Types.AddNote, function* (a: AddNote) {
        yield call(addNote, api, a.payload.note, a.payload.date);
        yield put(LoadEventsAction());
    })
}

function* addPhotoSaga(api: EventsHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is AddNote => a.type === Types.AddPhoto, function* (a: AddPhoto) {
        yield call(addPhoto, api, a.payload.description, a.payload.photo, a.payload.date);
        yield put(LoadEventsAction());
    })
}

export {
    loadEventsSaga,
    addNoteSaga,
    addPhotoSaga
}