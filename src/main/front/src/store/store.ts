import {applyMiddleware, createStore} from 'redux'
import createSageMiddleWare from 'redux-saga'
import rootSaga from './sagas'
import { rootReducer } from './reducers'
import {composeWithDevTools} from "@redux-devtools/extension";

const saga = createSageMiddleWare()

const store = createStore(rootReducer, composeWithDevTools(applyMiddleware(saga)))

saga.run(rootSaga)

export default store