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

export interface AppProps {
    readonly isAuthenticated: boolean
    readonly authenticationCheckInterval: number
}

export interface AppActionProps {
    readonly logoutUser: () => void
    readonly checkIfUserIsAuthenticated: () => void
}

const App: React.FC<AppProps & AppActionProps> = (props) => {

    const {isAuthenticated, authenticationCheckInterval, logoutUser, checkIfUserIsAuthenticated} = props

    React.useEffect(() => {

        if (isAuthenticated) {
            const interval = setInterval(() => {
                checkIfUserIsAuthenticated()
            }, authenticationCheckInterval);

            return () => clearInterval(interval);
        }

    }, [isAuthenticated])

    return <> <Router history={history}>
        <Navbar expand="lg" className="bg-body-tertiary">
            <Container>
                <Navbar.Brand href="#home">bookkeeper</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link> <Link to={`/`}>Home</Link></Nav.Link>
                        {isAuthenticated ? <></> : <Nav.Link> <Link to={`/login`}>Login</Link></Nav.Link>}
                    </Nav>
                    <Nav className="ml-auto">
                        {isAuthenticated ? <Nav.Link> <Link to="/" onClick={event => {
                            event.preventDefault();
                            logoutUser();
                        }}>Logout</Link></Nav.Link> : <></>}
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
        <Container>
            <Routes>
                <Route path="/" element={
                    <AuthenticationAwareGate isAuthenticated={isAuthenticated}
                                             redirectToIfLogged={"/dashboard"}>
                        <h1>Welcome in bookkeeper</h1>
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
    </>

}

export default App