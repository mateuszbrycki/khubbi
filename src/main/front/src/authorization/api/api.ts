import {Shelf} from "../../types";
import {List} from "immutable";
import axios from 'axios';


export interface AuthorizationHttpApi {
    readonly register: (email: string, password: string) => {}
    readonly login: () => {}
}

const Api: AuthorizationHttpApi = {
    register: (email: string, password: string) => {
        return axios.post(`http://localhost:8080/auth/signup`, { "email": email, "password": password })
              .then(res => {
                return res.data
              })

    },
    login: () => ({})
}

export {Api as AuthorizationApi}