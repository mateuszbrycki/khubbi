import React, {useEffect} from 'react'
import {useState} from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';

export interface RegisterFormProps {
}

export interface RegisterFormActionProps {
    readonly registerUser: (email: string, password: string) => void
}

const RegisterForm: React.FC<RegisterFormProps & RegisterFormActionProps> = (props) => {

    const {registerUser} = props

    const [email, setEmail] = useState<string>("")
    const [password, setPassword] = useState<string>("")
    const [repeatPassword, setRepeatPassword] = useState<string>("")
    const [validationError, setValidationError] = useState<string>("")

    useEffect(() => {
        if (password != repeatPassword) {
            setValidationError("Passwords are not equal");
            return;
        }
        setValidationError("");
    }, [password, repeatPassword]);

    const resetFormValues = () => {
        setEmail("");
        setPassword("");
        setRepeatPassword("");
        setValidationError("");
    }

    return <>
        <h2>Register</h2>
        <Form onSubmit={event => {
            registerUser(email, password);
            event.preventDefault();
            resetFormValues();
        }}>
            <Form.Group className="mb-3" controlId="registerFormEmail">
                <Form.Label>Email address</Form.Label>
                <Form.Control type="text" value={email} placeholder="Enter email"
                              onChange={(event) => setEmail(event.target.value)}
                              required
                />
            </Form.Group>

            <Form.Group className="mb-3" controlId="registerFormPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control type="password" value={password} placeholder="Password"
                              onChange={(event) => setPassword(event.target.value)} required/>
            </Form.Group>

            <Form.Group className="mb-3" controlId="registerFormPasswordRepeat">
                <Form.Label>Repeat Password</Form.Label>
                <Form.Control type="password" value={repeatPassword} placeholder="Repeat Password"
                              onChange={(event) => setRepeatPassword(event.target.value)} required/>
                <Form.Control.Feedback type="invalid">
                    Passwords are not equal.
                </Form.Control.Feedback>
            </Form.Group>
            <Button variant="primary" type="submit">
                Submit
            </Button>
        </Form>

        {validationError}
    </>
}

export default RegisterForm