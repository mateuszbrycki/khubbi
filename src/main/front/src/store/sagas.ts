import {all} from '@redux-saga/core/effects'
import {loadShelvesSaga} from "../shelves/store/sagas";
import {
    loginUserSaga,
    logoutUserSaga,
    checkJWTExpiredSaga,
    refreshJWTTokenSaga,
    registerUserSaga, userJWTTokenRefreshFailedSaga
} from "../authorization/store/sagas";
import {ShelvesApi} from "../shelves/api/api";
import {AuthorizationApi} from "../authorization/api/api";

function* rootSaga(): IterableIterator<unknown> {
    yield all([
        loadShelvesSaga(ShelvesApi),
        registerUserSaga(AuthorizationApi),
        loginUserSaga(AuthorizationApi),
        logoutUserSaga(AuthorizationApi),
        refreshJWTTokenSaga(AuthorizationApi),
        userJWTTokenRefreshFailedSaga(AuthorizationApi),
        checkJWTExpiredSaga(AuthorizationApi)
    ])
}

export default rootSaga