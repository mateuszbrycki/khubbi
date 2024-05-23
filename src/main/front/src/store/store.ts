import {applyMiddleware, combineReducers, createStore} from 'redux'
import createSageMiddleWare from 'redux-saga'
import rootSaga from './sagas'
import {rootReducer} from './reducers'
import {composeWithDevTools} from "@redux-devtools/extension"
import {createReduxHistoryContext} from "redux-first-history"
import {createBrowserHistory} from 'history'
import {persistReducer, persistStore} from 'redux-persist'
import storage from 'redux-persist/lib/storage'

const {createReduxHistory, routerMiddleware, routerReducer} = createReduxHistoryContext({
    history: createBrowserHistory(),
    //other options if needed
});

const saga = createSageMiddleWare()

const persistConfig = {
    key: 'root',
    whitelist: ['application'],
    storage: storage,
}

const presistedReducer = persistReducer(persistConfig, combineReducers({
    application: rootReducer, router: routerReducer
}));

const store = createStore(presistedReducer,
    composeWithDevTools(applyMiddleware(saga, routerMiddleware)));

const persistor = persistStore(store)

const history = createReduxHistory(store);

saga.run(rootSaga)

export {
    store,
    history,
    persistor
}