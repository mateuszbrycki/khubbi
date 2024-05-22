import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import './App.css'
import {Provider} from "react-redux";
import {history, store} from "./store/store";
import reportWebVitals from './reportWebVitals';
import {HistoryRouter as Router} from "redux-first-history/rr6";
import {Route, Routes} from "react-router-dom";
import AuthorizationContainer from "./authorization/containers/AuthorizationContainer";
import App from "./App";

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);


root.render(
    <React.StrictMode>
        <Provider store={store}>
            <Router history={history}>
                <Routes>
                    <Route path="/" element={<App><h1>Welcome in bookkeeper</h1></App>}/>
                    <Route path="/login" element={<AuthorizationContainer/>}/>
                    <Route path="/dashboard" element={<App><h1>Dashboard</h1></App>}/>
                </Routes>
            </Router>
        </Provider>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
