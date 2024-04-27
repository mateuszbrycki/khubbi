import React from "react";
import RegisterForm from "./RegisterForm";
import LoginForm from "./LoginForm";
import Container from "react-bootstrap/Container";
import {Col, Row} from "react-bootstrap";


export interface EntryFormsProps {
    readonly user: {} | null;
}

export interface EntryFormsActionProps {
    readonly registerUser: (email: string, password: string) => void
    readonly loginUser: (email: string, password: string) => void
}

const EntryForms: React.FC<EntryFormsProps & EntryFormsActionProps> = (props) => {
    const {user, registerUser, loginUser} = props

    return <>
        <Container>
            <Row>
                <Col>
                    <RegisterForm user={user} registerUser={registerUser}/>
                </Col>
                <Col>
                    <LoginForm user={user} loginUser={loginUser}/>
                </Col>
            </Row>
        </Container>
    </>
}

export default EntryForms