import {all} from '@redux-saga/core/effects'
import {addNoteSaga, addPhotoSaga, loadEventsSaga} from "../events/store/sagas";
import {
    checkJWTExpiredSaga,
    loginUserSaga,
    logoutUserSaga,
    refreshJWTTokenSaga,
    registerUserSaga,
    userJWTTokenRefreshFailedSaga
} from "../authorization/store/sagas";
import {EventsApi} from "../events/api/api";
import {AuthorizationApi} from "../authorization/api/api";

function* rootSaga(): IterableIterator<unknown> {
    yield all([
        loadEventsSaga(EventsApi),
        registerUserSaga(AuthorizationApi),
        loginUserSaga(AuthorizationApi),
        logoutUserSaga(AuthorizationApi),
        refreshJWTTokenSaga(AuthorizationApi),
        userJWTTokenRefreshFailedSaga(AuthorizationApi),
        checkJWTExpiredSaga(AuthorizationApi),
        addNoteSaga(EventsApi),
        addPhotoSaga(EventsApi)
    ])
}

export default rootSaga