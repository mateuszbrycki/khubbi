import React from "react";
import RegisterForm from "./RegisterForm";
import LoginForm from "./LoginForm";
import Container from "react-bootstrap/Container";
import {Col, Row} from "react-bootstrap";


export interface EntryFormsProps {
}

export interface EntryFormsActionProps {
    readonly registerUser: (email: string, password: string) => void
    readonly loginUser: (email: string, password: string) => void
}

const EntryForms: React.FC<EntryFormsProps & EntryFormsActionProps> = (props) => {
    const {registerUser, loginUser} = props

    return <>
        <Row>
            <Col>
                <RegisterForm registerUser={registerUser}/>
            </Col>
            <Col>
                <LoginForm loginUser={loginUser}/>
            </Col>
        </Row>
    </>
}

export default EntryForms