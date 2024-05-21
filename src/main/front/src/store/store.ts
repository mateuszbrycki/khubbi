import {applyMiddleware, combineReducers, createStore} from 'redux'
import createSageMiddleWare from 'redux-saga'
import rootSaga from './sagas'
import {rootReducer} from './reducers'
import {composeWithDevTools} from "@redux-devtools/extension"
import {createReduxHistoryContext} from "redux-first-history"
import {createBrowserHistory} from 'history'

const { createReduxHistory, routerMiddleware, routerReducer } = createReduxHistoryContext({
    history: createBrowserHistory(),
    //other options if needed
});

const saga = createSageMiddleWare()

const store = createStore(combineReducers({
    application: rootReducer, router: routerReducer
}), composeWithDevTools(applyMiddleware(saga, routerMiddleware)))

saga.run(rootSaga)

const history = createReduxHistory(store);

export {
    store,
    history
}