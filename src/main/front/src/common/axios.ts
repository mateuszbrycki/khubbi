import axios from "axios";
import {store} from "../store/store";
import {getJWTToken} from "../authorization/store/selectors";
import {RefreshUserJWTAction, UserJWTTokenRefreshFailedAction} from "../authorization/store/actions";

axios.interceptors.request.use(config => {
        // TODO mateusz.brycki implement pattern-based URL matching
    const isAuthEndpoint = config.url?.includes("/auth/")
    const isLogoutEndpoint = config.url?.endsWith("/auth/logout")

        const authToken = getJWTToken(store.getState());
    if ((!isAuthEndpoint || isLogoutEndpoint) && authToken) {
            config.headers.Authorization = `Bearer ${authToken}`;
        }
        return config;
    }, (error) =>
        Promise.reject(error)
);

axios.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        // If the error status is 401 and there is no originalRequest._retry flag,
        // it means the token has expired and we need to refresh it
        if (error.response.status === 403 && !originalRequest._retry) {

            originalRequest._retry = true;
            try {
                const oldJWTToken = getJWTToken(store.getState())
                await store.dispatch(RefreshUserJWTAction());

                // await doesn't wait for saga to complete, so we have to wait like this...
                do {
                    await new Promise(f => setTimeout(f, 1000));
                } while (oldJWTToken === getJWTToken(store.getState()))

                const jwtToken = getJWTToken(store.getState());

                // Retry the original request with the new tokenx
                originalRequest.headers.Authorization = `Bearer ${jwtToken}`;

                // It might be not worth handling this logic in saga since a new instance of axios is returned
                return axios(originalRequest);
            } catch (error) {
                // Handle refresh token error or redirect to login
                store.dispatch(UserJWTTokenRefreshFailedAction())
                console.error(error)
            }
        }

        return Promise.reject(error);
    }
);

export default axios