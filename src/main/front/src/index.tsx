import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import './App.css'
import App from './App';
import {Provider} from "react-redux";
import store from "./store/store";
import reportWebVitals from './reportWebVitals';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import EntryForms from "./authorization/components/EntryForms";
import AuthorizationContainer from "./authorization/containers/AuthorizationContainer";

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

const router = createBrowserRouter([
    {
        path: "/login",
        element: <App />,
        children: [
            {
                path: "/login",
                element: <AuthorizationContainer />,
            },
       ]},
    {
        path: "/",
        element: <App />,
        children: [
            {
                path: "/",
                element: <h1>Welcome</h1>,
            },
        ]
    },
    {
        path: "/dashboard",
        element: <App />,
        children: [
            {
                path: "/dashboard",
                element: <h1>Dashboard</h1>,
            },
        ]
    },
]);

root.render(
  <React.StrictMode>
    <Provider store={store}>
        <RouterProvider router={router} />
    </Provider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
