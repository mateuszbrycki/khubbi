import React from 'react';
import AuthorizationContainer from './authorization/containers/AuthorizationContainer'
import './App.css';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import {Col, Row} from "react-bootstrap";

export interface AppActionsProps {
}

export interface AppProps {
}


function App() {
  return (
      <>
      <Navbar expand="lg" className="bg-body-tertiary">
        <Container>
          <Navbar.Brand href="#home">bookkeeper</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link href="#home">Home</Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
      <AuthorizationContainer/>
      </>
  );
}

export default App;
