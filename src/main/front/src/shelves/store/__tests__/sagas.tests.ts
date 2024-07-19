import {loadShelvesSaga} from "../sagas";
import {expectSaga} from "redux-saga-test-plan";
import {Types} from "../actions";
import {ShelvesApi, ShelvesHttpApi} from "../../api/api";
import {List} from "immutable";


describe('Load Shelves Saga', () => {
    it('pushes success action with details on success', async () => {

        const fetchShelvesMock = jest.fn(() => Promise.resolve(
            List.of({
                name: "shelf-1",
                id: "1"
            }, {
                name: "shelf-2",
                id: "2"
            }, {
                name: "shelf-3",
                id: "3"
            })
        ));

        const api: ShelvesHttpApi = {
            ...ShelvesApi,
            fetchShelves: fetchShelvesMock
        }

        await expectSaga(loadShelvesSaga, api)
            .put({
                type: Types.ShelvesLoaded,
                payload: {
                    shelves: List.of({
                        name: "shelf-1",
                        id: "1"
                    }, {
                        name: "shelf-2",
                        id: "2"
                    }, {
                        name: "shelf-3",
                        id: "3"
                    })
                }
            })
            .dispatch({
                type: Types.LoadShelves,

            })
            .run()
            .then(() => {
                expect(fetchShelvesMock).toHaveBeenCalled()
            })
    })
})