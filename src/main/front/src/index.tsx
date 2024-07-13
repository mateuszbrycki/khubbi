import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import './App.css'
import {Provider} from "react-redux";
import {persistor, store} from "./store/store";
import reportWebVitals from './reportWebVitals';
import {isAuthenticated} from "./authorization/store/selectors";
import {PersistGate} from "redux-persist/integration/react";
import AppContainer from "./app/containers/AppContainer";

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);


function LoadingView() {
    return <h1>Loading</h1>;
}

const authenticated: () => boolean = () => isAuthenticated(store.getState())

root.render(
    <Provider store={store}>
        <PersistGate loading={<LoadingView/>} persistor={persistor}>

            <AppContainer/>
        </PersistGate>
    </Provider>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
