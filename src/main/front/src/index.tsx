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
import {AuthenticationAwareGate} from "./common/AuthenticationAwareGate";
import {PersistGate} from "redux-persist/integration/react";
import DashboardContainer from "./dashboard/containers/DashboardContainer";

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);


function LoadingView() {
    return <h1>Loading</h1>;
}

const authenticated:() => boolean = () => isAuthenticated(store.getState())

root.render(
    <React.StrictMode>
        <Provider store={store}>
            <PersistGate loading={<LoadingView/>} persistor={persistor}>
                <Router history={history}>
                    <Routes>
                        <Route path="/" element={
                            <AuthenticationAwareGate isAuthenticated={authenticated}
                                                     redirectToIfLogged={"/dashboard"}>
                                <App isAuthenticated={authenticated}><h1>Welcome in bookkeeper</h1></App>
                            </AuthenticationAwareGate>
                        }/>
                        <Route path="/login"
                               element={
                                   <AuthenticationAwareGate isAuthenticated={authenticated}
                                                            redirectToIfLogged={"/dashboard"}>
                                       <App isAuthenticated={authenticated}>
                                           <AuthorizationContainer/>
                                       </App>
                                   </AuthenticationAwareGate>
                               }/>
                        <Route path="/dashboard"
                               element={
                                   <AuthenticationAwareGate isAuthenticated={authenticated}
                                                            redirectToIfNotLogged={"/login"}>
                                       <App isAuthenticated={authenticated}>
                                          <DashboardContainer/>
                                       </App>
                                   </AuthenticationAwareGate>}/>
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
