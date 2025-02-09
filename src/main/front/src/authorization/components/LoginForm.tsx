import React from 'react'
import {useState} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';

export interface LoginFormProps {
}

export interface LoginFormActionProps {
    readonly loginUser: (email: string, password: string) => void
}

const RegisterForm: React.FC<LoginFormProps & LoginFormActionProps> = (props) => {

    const {loginUser} = props

    const [email, setEmail] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    const resetFormValues = () => {
        setEmail("");
        setPassword("");
    }

    return <>
        <h2>Login</h2>
        <Form onSubmit={event => {
            loginUser(email, password);
            event.preventDefault();
            resetFormValues();
        }}>
            <Form.Group className="mb-3" controlId="loginFormEmail">
                <Form.Label>Email address</Form.Label>
                <Form.Control type="text" value={email} placeholder="Enter email"
                              onChange={(event) => setEmail(event.target.value)}/>
            </Form.Group>

            <Form.Group className="mb-3" controlId="loginFormPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control type="password" value={password} placeholder="Password"
                              onChange={(event) => setPassword(event.target.value)}/>
            </Form.Group>
            <Button variant="primary" type="submit">
                Submit
            </Button>
        </Form>
    </>
}

export default RegisterForm