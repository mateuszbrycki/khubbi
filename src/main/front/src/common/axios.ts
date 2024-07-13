import axios from "axios";
import {store} from "../store/store";
import {getJWTToken, getRefreshToken} from "../authorization/store/selectors";
import {UserJWTTokenRefreshedAction} from "../authorization/store/actions";
import {AUTH_BASE_URL} from "../authorization/api/api";

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

async function refreshJWTToken() {
    const refreshToken = getRefreshToken(store.getState());
    const response = await axios.post(`${AUTH_BASE_URL}/refreshToken`, {"refreshToken": refreshToken});
    const data = response.data;
    store.dispatch(UserJWTTokenRefreshedAction(data.jwtToken, data.expiresIn));
    return data;
}

axios.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        // If the error status is 401 and there is no originalRequest._retry flag,
        // it means the token has expired and we need to refresh it
        if (error.response.status === 403 && !originalRequest._retry) {

            originalRequest._retry = true;
            try {
                const data = await refreshJWTToken();

                // Retry the original request with the new tokenx
                originalRequest.headers.Authorization = `Bearer ${data.jwtToken}`;

                // It might be not worth handling this logic in saga since a new instance of axios is returned
                return axios(originalRequest);
            } catch (error) {
                // Handle refresh token error or redirect to login
                console.error(error)
            }
        }

        return Promise.reject(error);
    }
);

export default axios