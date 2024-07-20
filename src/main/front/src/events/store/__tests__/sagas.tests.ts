import {loadEventsSaga} from "../sagas";
import {expectSaga} from "redux-saga-test-plan";
import {Types} from "../actions";
import {EventsApi, EventsHttpApi} from "../../api/api";
import {List} from "immutable";


describe('Load events Saga', () => {
    it('pushes success action with details on success', async () => {

        const fetchEventsMock = jest.fn(() => Promise.resolve(
            List.of({
                name: "event-1",
                id: "1"
            }, {
                name: "event-2",
                id: "2"
            }, {
                name: "event-3",
                id: "3"
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
                        name: "event-1",
                        id: "1"
                    }, {
                        name: "event-2",
                        id: "2"
                    }, {
                        name: "event-3",
                        id: "3"
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
})