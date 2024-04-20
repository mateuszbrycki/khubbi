import {all} from '@redux-saga/core/effects'
import {onLoadShelves} from "../shelves/store/sagas";
import {ShelvesApi} from "../shelves/api/api";

function* rootSaga(): IterableIterator<unknown> {
    yield all([
        onLoadShelves(ShelvesApi)
    ])
}

export default rootSaga