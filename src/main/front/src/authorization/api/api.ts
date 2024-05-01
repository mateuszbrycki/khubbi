import axios from 'axios';

export interface LoginResponse {
    readonly token: string
    readonly expiresIn: number
}

export interface AuthorizationHttpApi {
    readonly register: (email: string, password: string) => {}
    readonly login: (email: string, password: string) => Promise<LoginResponse>
}

const Api: AuthorizationHttpApi = {
    register: (email: string, password: string) => {
        return axios.post(`http://localhost:8080/auth/signup`, { "email": email, "password": password })
              .then(res => {
                return res.data
              })

    },
    login: (email: string, password: string) => {
        return axios.post(`http://localhost:8080/auth/login`, { "email": email, "password": password })
            .then(res => {
                return res.data
            })
    }
}

export {Api as AuthorizationApi}