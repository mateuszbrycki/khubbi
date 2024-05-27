import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import './App.css'
import {Provider} from "react-redux";
import {history, persistor, store} from "./store/store";
import reportWebVitals from './reportWebVitals';
import {HistoryRouter as Router} from "redux-first-history/rr6";
import {Route, Routes} from "react-router-dom";
import AuthorizationContainer from "./authorization/containers/AuthorizationContainer";
import App from "./App";
import {isAuthenticated} from "./authorization/store/selectors";
import {RequireAuth} from "./common/RequireAuth";
import {PersistGate} from "redux-persist/integration/react";

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);


function LoadingView() {
    return <h1>Loading</h1>;
}

root.render(
    <React.StrictMode>
        <Provider store={store}>
            <PersistGate loading={<LoadingView/>} persistor={persistor}>
                <Router history={history}>
                    <Routes>
                        <Route path="/" element={
                            <RequireAuth isAuthenticated={() => isAuthenticated(store.getState())}
                                         redirectToIfLogged={"/dashboard"}>
                                <App><h1>Welcome in bookkeeper</h1></App>
                            </RequireAuth>
                        }/>
                        <Route path="/login"
                               element={
                                   <RequireAuth isAuthenticated={() => isAuthenticated(store.getState())}
                                                redirectToIfLogged={"/dashboard"}>
                                       <App>
                                           <AuthorizationContainer/>
                                       </App>
                                   </RequireAuth>
                               }/>
                        <Route path="/dashboard"
                               element={
                                   <RequireAuth isAuthenticated={() => isAuthenticated(store.getState())}
                                                redirectToIfNotLogged={"/login"}>
                                       <App>
                                           <h1>Dashboard</h1>
                                       </App>
                                   </RequireAuth>}/>
                    </Routes>
                </Router>
            </PersistGate>
        </Provider>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
