import React from 'react'
import {useState} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';

export interface RegisterFormProps {
    readonly user: {} | null;
}

export interface RegisterFormActionProps {
    readonly registerUser: (email: string, password: string) => void
}

const RegisterForm: React.FC<RegisterFormProps & RegisterFormActionProps> = (props) => {

    const {user, registerUser} = props

    const [email, setEmail] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    return <>
        <h2>Register</h2>
        <Form onSubmit={event => {
            registerUser(email, password);
            event.preventDefault();
        }}>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Email address</Form.Label>
                <Form.Control type="text" placeholder="Enter email"
                              onChange={(event) => setEmail(event.target.value)}/>
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPassword">
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