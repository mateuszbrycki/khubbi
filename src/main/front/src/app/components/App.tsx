import React from "react";
import {history} from "../../store/store";
import {Link, Route, Routes} from "react-router-dom";
import {HistoryRouter as Router} from "redux-first-history/rr6";
import AuthorizationContainer from "../../authorization/containers/AuthorizationContainer";
import DashboardContainer from "../../dashboard/containers/DashboardContainer";
import AuthenticationAwareGate from "../../common/AuthenticationAwareGate";
import Navbar from "react-bootstrap/Navbar";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import {AlertPanel} from "../../alerts/components/AlertPanel";
import {AlertMessage} from "../../types";
import {List} from "immutable";

export interface AppProps {
    readonly isAuthenticated: boolean
    readonly authenticationCheckInterval: number
    readonly alerts: List<AlertMessage>
}

export interface AppActionProps {
    readonly logoutUser: () => void
    readonly checkIfUserIsAuthenticated: () => void
    readonly hideAlert: (alertMessage: AlertMessage) => void
}

const App: React.FC<AppProps & AppActionProps> = (props) => {

    const {
        isAuthenticated,
        authenticationCheckInterval,
        alerts,
        logoutUser,
        checkIfUserIsAuthenticated,
        hideAlert
    } = props

    React.useEffect(() => {

        if (isAuthenticated) {
            const interval = setInterval(() => {
                checkIfUserIsAuthenticated()
            }, authenticationCheckInterval);

            return () => clearInterval(interval);
        }

    }, [isAuthenticated])

    return <>
        <Router history={history}>
            <Navbar expand="lg" className="bg-body-tertiary">
                <Container>
                    <Navbar.Brand href="#home">khubbi</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <Link to={`/`} className="nav-link">Home</Link>
                            {isAuthenticated ? <></> :
                                <Link to={`/login`} className="nav-link">Login</Link>}
                        </Nav>
                        <Nav className="ml-auto">
                            {isAuthenticated ? <Link to="/" onClick={event => {
                                event.preventDefault();
                                logoutUser();
                            }} className="nav-link">Logout</Link> : <></>}
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
            <Container>
                <Routes>
                    <Route path="/" element={
                        <AuthenticationAwareGate isAuthenticated={isAuthenticated}
                                                 redirectToIfLogged={"/dashboard"}>
                            <h1>Welcome in khubbi</h1>
                        </AuthenticationAwareGate>
                    }/>
                    <Route path="/login"
                           element={
                               <AuthenticationAwareGate isAuthenticated={isAuthenticated}
                                                        redirectToIfLogged={"/dashboard"}>
                                   <AuthorizationContainer/>
                               </AuthenticationAwareGate>
                           }/>
                    <Route path="/dashboard"
                           element={
                               <AuthenticationAwareGate isAuthenticated={isAuthenticated}
                                                        redirectToIfNotLogged={"/login"}>
                                   <DashboardContainer/>
                               </AuthenticationAwareGate>}/>
                </Routes>
            </Container>
        </Router>
        <AlertPanel alerts={alerts} hideAlert={hideAlert} alertTimeoutMs={10000}/>
    </>

}

export default App