import axios from '../../common/axios';

const AUTH_BASE_URL = "http://localhost:8080/auth";

export interface LoginResponse {
    readonly jwtToken: {
        readonly token: string,
        readonly expiresIn: number
    },
    readonly refreshToken: {
        readonly token: string,
        readonly expiresIn: number
    }
}

export interface RegisterResponse {
    readonly id: string
    readonly email: string
    readonly createdAt: string
}

export interface LogoutResponse {
}
export interface RefreshTokenResponse {
    readonly jwtToken: string,
    readonly expiresIn: number
}

export interface AuthorizationHttpApi {
    readonly register: (email: string, password: string, repeatedPassword: string) => Promise<RegisterResponse>
    readonly login: (email: string, password: string) => Promise<LoginResponse>
    readonly logout: () => Promise<LogoutResponse>
    readonly refreshToken: (refreshToken: string) => Promise<RefreshTokenResponse>
}

const Api: AuthorizationHttpApi = {
    register: (email: string, password: string, repeatedPassword: string) => {
        return axios.post(`${AUTH_BASE_URL}/signup`, {
            "email": email,
            "password": password,
            "repeatedPassword": repeatedPassword
        })
            .then(res => {
                return res.data
            })

    },
    login: (email: string, password: string) => {
        return axios.post(`${AUTH_BASE_URL}/login`, {"email": email, "password": password})
            .then(res => {
                return res.data
            })
    },
    logout: () => {
        return axios.get(`${AUTH_BASE_URL}/logout`)
            .then(res => {
                return res
            })
    },
    refreshToken: (refreshToken: string) => {
        return axios.post(`${AUTH_BASE_URL}/refreshToken`, {"refreshToken": refreshToken})
            .then(res => {
                return res.data
            })
    }
}

export {Api as AuthorizationApi, AUTH_BASE_URL}