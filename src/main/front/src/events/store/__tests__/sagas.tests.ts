import {addNoteSaga, addPhotoSaga, loadEventsSaga} from "../sagas";
import {expectSaga} from "redux-saga-test-plan";
import {Types} from "../actions";
import {EventsApi, EventsHttpApi} from "../../api/api";
import {List} from "immutable";
import {EventDate} from "../../../types";
import {Types as AlertTypes} from "../../../alerts/store/actions";


describe('Load events Saga', () => {
    it('pushes success action with details on success', async () => {

        const fetchEventsMock = jest.fn(() => Promise.resolve(
            List.of({
                note: "event-1",
                date: EventDate.ofDateAndTime("2024-07-12T20:00"),
                id: "event-1"
            }, {
                note: "event-2",
                date: EventDate.ofDateAndTime("2024-07-12T21:00"),
                id: "event-2"
            }, {
                note: "event-3",
                date: EventDate.ofDateAndTime("2024-07-12T22:00"),
                id: "event-3"
            })
        ));

        const api: EventsHttpApi = {
            ...EventsApi,
            fetchEvents: fetchEventsMock
        }

        await expectSaga(loadEventsSaga, api)
            .put({
                type: Types.EventsLoaded,
                payload: {
                    events: List.of({
                        note: "event-1",
                        date: EventDate.ofDateAndTime("2024-07-12T20:00"),
                        id: "event-1"
                    }, {
                        note: "event-2",
                        date: EventDate.ofDateAndTime("2024-07-12T21:00"),
                        id: "event-2"
                    }, {
                        note: "event-3",
                        date: EventDate.ofDateAndTime("2024-07-12T22:00"),
                        id: "event-3"
                    })
                }
            })
            .dispatch({
                type: Types.LoadEvents,

            })
            .run()
            .then(() => {
                expect(fetchEventsMock).toHaveBeenCalled()
            })
    })

    it('pushes show alert action on api call failure', async () => {
        const fetchEventsMock = jest.fn(() => Promise.reject({
            response: {
                data: {
                    description: "Something went wrong"
                }
            }
        }));

        const api: EventsHttpApi = {
            ...EventsApi,
            fetchEvents: fetchEventsMock
        }

        await expectSaga(loadEventsSaga, api)
            .put.like({
                action: {
                    type: AlertTypes.ShowAlert
                }
            })
            .dispatch({
                type: Types.LoadEvents,
            })
            .run()
            .then(() => {
                expect(fetchEventsMock).toHaveBeenCalled()
            })
    })
})

describe('Add note saga', () => {
    it('pushes success action with details on success', async () => {
        const addEventMock = jest.fn(() => Promise.resolve({}));

        const api: EventsHttpApi = {
            ...EventsApi,
            addNote: addEventMock
        }
        await expectSaga(addNoteSaga, api)
            .put({
                type: Types.LoadEvents,
            })
            .dispatch({
                type: Types.AddNote,
                payload: {
                    note: "event-1",
                    date: EventDate.ofDateAndTime("2024-07-12T20:00"),
                }
            })
            .run()
            .then(() => {
                expect(addEventMock).toHaveBeenCalled()
            })
    })

    it('pushes show alert action on api call failure', async () => {
        const addEventMock = jest.fn(() => Promise.reject({
            response: {
                data: {
                    description: "Something went wrong"
                }
            }
        }));

        const api: EventsHttpApi = {
            ...EventsApi,
            addNote: addEventMock
        }

        await expectSaga(addNoteSaga, api)
            .put.like({
                action: {
                    type: AlertTypes.ShowAlert
                }
            })
            .dispatch({
                type: Types.AddNote,
                payload: {
                    note: "event-1",
                    date: EventDate.ofDateAndTime("2024-07-12T20:00"),
                }
            })
            .run()
            .then(() => {
                expect(addEventMock).toHaveBeenCalled()
            })
    })
})

describe('Add photo saga', () => {
    it('pushes success action with details on success', async () => {
        const addPhotoMock = jest.fn(() => Promise.resolve({}));

        const api: EventsHttpApi = {
            ...EventsApi,
            addPhoto: addPhotoMock
        }
        await expectSaga(addPhotoSaga, api)
            .put({
                type: Types.LoadEvents,
            })
            .dispatch({
                type: Types.AddPhoto,
                payload: {
                    description: "description-1",
                    file: new File(["(⌐□_□)"], "image.png", {type: "image/png", lastModified: 1722023600022}),
                    date: EventDate.ofDateAndTime("2024-07-12T20:00"),
                }
            })
            .run()
            .then(() => {
                expect(addPhotoMock).toHaveBeenCalled()
            })
    })

    it('pushes show alert action on api call failure', async () => {
        const addEventMock = jest.fn(() => Promise.reject({
            response: {
                data: {
                    description: "Something went wrong"
                }
            }
        }));

        const api: EventsHttpApi = {
            ...EventsApi,
            addPhoto: addEventMock
        }

        await expectSaga(addPhotoSaga, api)
            .put.like({
                action: {
                    type: AlertTypes.ShowAlert
                }
            })
            .dispatch({
                type: Types.AddPhoto,
                payload: {
                    description: "description-1",
                    file: new File(["(⌐□_□)"], "image.png", {type: "image/png", lastModified: 1722023600022}),
                    date: EventDate.ofDateAndTime("2024-07-12T20:00"),
                }
            })
            .run()
            .then(() => {
                expect(addEventMock).toHaveBeenCalled()
            })
    })
})