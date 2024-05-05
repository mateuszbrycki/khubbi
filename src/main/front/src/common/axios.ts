import axios from "axios";
import {JWT_TOKEN_STORAGE_KEY} from "./variables";

axios.interceptors.request.use(config => {
        // TODO mateusz.brycki implement pattern-based URL matching
        const isAuthEndpoint = config.url?.includes("/auth/");

        const authToken = localStorage.getItem(JWT_TOKEN_STORAGE_KEY);
        if (!isAuthEndpoint && authToken) {
            config.headers.Authorization = `Bearer ${authToken}`;
        }
        return config;
    }, (error) =>
        Promise.reject(error)
);

export default axios