import axios from "axios";
import {store} from "../store/store";
import {getJWTToken} from "../authorization/store/selectors";

axios.interceptors.request.use(config => {
        // TODO mateusz.brycki implement pattern-based URL matching
        const isAuthEndpoint = config.url?.includes("/auth/");

        const authToken = getJWTToken(store.getState());
        if (!isAuthEndpoint && authToken) {
            config.headers.Authorization = `Bearer ${authToken}`;
        }
        return config;
    }, (error) =>
        Promise.reject(error)
);

export default axios