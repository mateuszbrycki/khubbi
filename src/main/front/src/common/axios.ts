import axios from "axios";
import {store} from "../store/store";
import {getJWTToken} from "../authorization/store/selectors";

axios.interceptors.request.use(config => {
        // TODO mateusz.brycki implement pattern-based URL matching
        // TODO mateusz.brycki consider if that's a good place to refresh the token
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