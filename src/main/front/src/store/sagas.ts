import {all} from '@redux-saga/core/effects'
import {onLoadShelves} from "../shelves/store/sagas";
import {onAuthorization} from "../authorization/store/sagas";
import {ShelvesApi} from "../shelves/api/api";
import {AuthorizationApi} from "../authorization/api/api";

function* rootSaga(): IterableIterator<unknown> {
    yield all([
        onLoadShelves(ShelvesApi),
        onAuthorization(AuthorizationApi)
    ])
}

export default rootSaga