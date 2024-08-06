import {Action} from "redux";
import {EventsHttpApi} from "../api/api";
import {call, put, takeEvery} from '@redux-saga/core/effects'
import {AddNote, AddPhoto, EventsLoadedAction, LoadEvents, LoadEventsAction, Types} from "./actions";
import {List} from "immutable";
import {AlertMessage, Event, EventDate} from "../../types";
import {ShowAlertAction} from "../../alerts/store/actions";

function* fetchEvents(api: EventsHttpApi): Generator<any, any, List<Event>> {
    return yield api.fetchEvents()
        .then(response => ({response}))
        .catch(error => ({error}))
}

function* addNote(api: EventsHttpApi, note: string, date: EventDate): Generator<any, any, List<Event>> {
    return yield api.addNote(note, date)
        .then(response => ({response}))
        .catch(error => ({error}))
}

function* addPhoto(api: EventsHttpApi, description: string, photo: File, date: EventDate): Generator<any, any, List<Event>> {
    return yield api.addPhoto(description, photo, date)
        .then(response => ({response}))
        .catch(error => ({error}))
}

function* loadEventsSaga(api: EventsHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is LoadEvents => a.type === Types.LoadEvents, function* (a: LoadEvents) {
        const {error, response} = yield call(fetchEvents, api);
        if (response) {
            yield put(EventsLoadedAction(response));
        } else {
            yield put(ShowAlertAction(AlertMessage.error("Error when loading events: " + error.response.data.message)))
        }
    })
}

function* addNoteSaga(api: EventsHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is AddNote => a.type === Types.AddNote, function* (a: AddNote) {
        const {error, response} = yield call(addNote, api, a.payload.note, a.payload.date);
        if (response) {
            yield put(LoadEventsAction());
        } else {
            yield put(ShowAlertAction(AlertMessage.error("Error when adding note: " + error.response.data.message)))
        }
    })
}

function* addPhotoSaga(api: EventsHttpApi): IterableIterator<unknown> {
    yield takeEvery((a: Action): a is AddNote => a.type === Types.AddPhoto, function* (a: AddPhoto) {
        const {error, response} = yield call(addPhoto, api, a.payload.description, a.payload.photo, a.payload.date);
        if (response) {
            yield put(LoadEventsAction());
        } else {
            yield put(ShowAlertAction(AlertMessage.error("Error when adding photo: " + error.response.data.message)))
        }
    })
}

export {
    loadEventsSaga,
    addNoteSaga,
    addPhotoSaga
}