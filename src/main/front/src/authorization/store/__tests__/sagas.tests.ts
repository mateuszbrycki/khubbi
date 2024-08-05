import {checkJWTExpiredSaga, loginUserSaga, logoutUserSaga, refreshJWTTokenSaga, registerUserSaga} from "../sagas";
import {expectSaga} from "redux-saga-test-plan";
import {Types} from "../actions";
import {AuthorizationApi, AuthorizationHttpApi} from "../../api/api";
import {push} from "redux-first-history";
import {initialState} from "../../../store/state";
import {initialEventsState} from "../../../events/store/state";
import {initialAuthorizationState} from "../state";

const generateValidTokenExpiration: () => number =
    () => Date.now() + 100_000
const generateInvalidTokenExpiration: () => number =
    () => Date.now() - 100_000

describe('Register User Saga', () => {
    it('pushes success action with details on success', async () => {

        const registerMock = jest.fn(() => Promise.resolve({
            id: "any-id",
            email: "email",
            createdAt: "12345",
        }));

        const api: AuthorizationHttpApi = {
            ...AuthorizationApi,
            register: registerMock
        }

        await expectSaga(registerUserSaga, api)
            .put({
                type: Types.UserRegistered,
                payload: {
                    id: "any-id",
                    email: "email",
                    createdAt: "12345"
                }
            })
            .dispatch({
                type: Types.RegisterUser,
                payload: {
                    email: "test-email",
                    password: "test-password"
                }
            })
            .run()
            .then(() => {
                expect(registerMock).toHaveBeenCalledWith("test-email", "test-password")
            })
    })
})

describe('Login User Saga', () => {
    it('pushes success action  and redirects to dashboard', async () => {

        const loginMock = jest.fn(() => Promise.resolve({
            jwtToken: {
                token: "jwt-token",
                expiresIn: 123
            },
            refreshToken: {
                token: "refresh-token",
                expiresIn: 654
            }
        }));

        const api: AuthorizationHttpApi = {
            ...AuthorizationApi,
            login: loginMock
        }

        await expectSaga(loginUserSaga, api)
            .put({
                type: Types.UserLoggedIn,
                payload: {
                    jwtToken: {
                        token: "jwt-token",
                        expiresIn: 123
                    },
                    refreshToken: {
                        token: "refresh-token",
                        expiresIn: 654
                    }
                }
            })
            .put(push("/dashboard"))
            .dispatch({
                type: Types.LoginUser,
                payload: {
                    email: "test-email",
                    password: "test-password"
                }
            })
            .run()
            .then(() => {
                expect(loginMock).toHaveBeenCalledWith("test-email", "test-password")
            })
    })
})

describe('Logout User Saga', () => {
    it('pushes success action and redirects to login', async () => {

        const logoutMock = jest.fn(() => Promise.resolve({}));

        const api: AuthorizationHttpApi = {
            ...AuthorizationApi,
            logout: logoutMock
        }

        await expectSaga(logoutUserSaga, api)
            .put({
                type: Types.UserLoggedOut
            })
            .put(push("/login"))
            .dispatch({
                type: Types.LogoutUser
            })
            .run()
            .then(() => {
                expect(logoutMock).toHaveBeenCalled()
            })
    })
})

describe('Refresh JWT Token Saga', () => {
    const JWT_TOKEN = "jwt-token"
    const REFRESH_TOKEN = "refresh-token"

    it('pushes success action with tokens details', async () => {

        const refreshTokenMock = jest.fn(() => Promise.resolve({
            jwtToken: JWT_TOKEN,
            expiresIn: 123
        }));

        const api: AuthorizationHttpApi = {
            ...AuthorizationApi,
            refreshToken: refreshTokenMock
        }

        await expectSaga(refreshJWTTokenSaga, api)
            .withState({
                ...initialState,
                eventsState: {...initialEventsState},
                authorizationState: {
                    ...initialAuthorizationState,
                    refreshToken: {
                        token: REFRESH_TOKEN,
                        expiresIn: 654
                    }

                }
            })
            .put({
                type: Types.UserJWTTokenRefreshed,
                payload: {
                    jwtToken: JWT_TOKEN,
                    expiresIn: 123
                }
            })
            .dispatch({
                type: Types.RefreshUserJWT
            })
            .run()
            .then(() => {
                expect(refreshTokenMock).toHaveBeenCalledWith(REFRESH_TOKEN)
            })
    })

    it('pushes failure action on request fail', async () => {

        const refreshTokenMock = jest.fn(() => Promise.reject());

        const api: AuthorizationHttpApi = {
            ...AuthorizationApi,
            refreshToken: refreshTokenMock
        }

        await expectSaga(refreshJWTTokenSaga, api)
            .withState({
                ...initialState,
                eventsState: {...initialEventsState},
                authorizationState: {
                    ...initialAuthorizationState,
                    refreshToken: {
                        token: REFRESH_TOKEN,
                        expiresIn: 654
                    }
                }

            })
            .put({
                type: Types.UserJWTTokenRefreshFailed
            })
            .dispatch({
                type: Types.RefreshUserJWT
            })
            .run()
            .then(() => {
                expect(refreshTokenMock).toHaveBeenCalledWith(REFRESH_TOKEN)
            })
    })
})

describe('User JWT Token Refresh Failed Saga', () => {
    it('pushes logout and redirects to login', async () => {

        const logoutMock = jest.fn(() => Promise.resolve({}));

        const api: AuthorizationHttpApi = {
            ...AuthorizationApi,
            logout: logoutMock
        }

        await expectSaga(logoutUserSaga, api)
            .put({
                type: Types.UserLoggedOut
            })
            .put(push("/login"))
            .dispatch({
                type: Types.LogoutUser
            })
            .run()
    })

    // https://github.com/jfairbank/redux-saga-test-plan/blob/master/docs/integration-testing/partial-matching.md
    it('shows notification on failed logout', async () => {

        const logoutMock = jest.fn(() => Promise.reject({
            response: {
                data: {
                    description: "Something went wrong"
                }
            }
        }));

        const api: AuthorizationHttpApi = {
            ...AuthorizationApi,
            logout: logoutMock
        }

        await expectSaga(logoutUserSaga, api)
            .put.like({
                action: {
                    type: "SHOW_ALERT"
                }
            })
            .dispatch({
                type: Types.LogoutUser
            })
            .run()
    })
})

describe('Check JWT Token Expired Saga', () => {
    const JWT_TOKEN = "jwt-token"
    const REFRESH_TOKEN = "refresh-token"

    it('JWT Token expired but Refresh Token is valid so JWT can be refreshed', async () => {

        await expectSaga(checkJWTExpiredSaga, AuthorizationApi)
            .withState({
                ...initialState,
                eventsState: {...initialEventsState},
                authorizationState: {
                    ...initialAuthorizationState,
                    jwtToken: {
                        token: JWT_TOKEN,
                        expiresIn: generateInvalidTokenExpiration()
                    },
                    refreshToken: {
                        token: REFRESH_TOKEN,
                        expiresIn: generateValidTokenExpiration()
                    }
                }

            })
            .put({
                type: Types.RefreshUserJWT
            })
            .dispatch({
                type: Types.CheckJWTExpired
            })
            .run()
    })

    it('both JWT and Refresh Tokens expired so user should be logged out', async () => {

        await expectSaga(checkJWTExpiredSaga, AuthorizationApi)
            .withState({
                ...initialState,
                eventsState: {...initialEventsState},
                authorizationState: {
                    ...initialAuthorizationState,
                    jwtToken: {
                        token: JWT_TOKEN,
                        expiresIn: generateInvalidTokenExpiration()
                    },
                    refreshToken: {
                        token: REFRESH_TOKEN,
                        expiresIn: generateInvalidTokenExpiration()
                    }

                }
            })
            .put({
                type: Types.UserLoggedOut
            })
            .put(push("/login"))
            .dispatch({
                type: Types.CheckJWTExpired
            })
            .run()
    })
})