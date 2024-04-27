import React from 'react'
import {useState} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';

export interface LoginFormProps {
    readonly user: {} | null;
}

export interface LoginFormActionProps {
    readonly loginUser: (email: string, password: string) => void
}

const RegisterForm: React.FC<LoginFormProps & LoginFormActionProps> = (props) => {

    const {user, loginUser} = props

    const [email, setEmail] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    return <>
        <h2>Login</h2>
        <Form onSubmit={event => {
            loginUser(email, password);
            event.preventDefault();
        }}>
            <Form.Group className="mb-3" controlId="loginFormEmail">
                <Form.Label>Email address</Form.Label>
                <Form.Control type="text" placeholder="Enter email"
                              onChange={(event) => setEmail(event.target.value)}/>
            </Form.Group>

            <Form.Group className="mb-3" controlId="loginFormPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control type="password" placeholder="Password"
                              onChange={(event) => setPassword(event.target.value)}/>
            </Form.Group>
            <Button variant="primary" type="submit">
                Submit
            </Button>
        </Form>
    </>
}

export default RegisterForm