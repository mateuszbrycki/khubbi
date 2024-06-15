import React from 'react';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import {Link} from "react-router-dom";

export interface AppActionsProps {
}

export interface AppProps {
    children?: React.ReactNode,
    readonly isAuthenticated: () => boolean;
}


const App = ({children, isAuthenticated}: AppProps) => {
    return (
        <>
            <Navbar expand="lg" className="bg-body-tertiary">
                <Container>
                    <Navbar.Brand href="#home">bookkeeper</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <Nav.Link> <Link to={`/`}>Home</Link></Nav.Link>
                            {isAuthenticated() ? <></> : <Nav.Link> <Link to={`/login`}>Login</Link></Nav.Link>}
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
            <Container>
                {children}
            </Container>
        </>
    );
}

export default App;
