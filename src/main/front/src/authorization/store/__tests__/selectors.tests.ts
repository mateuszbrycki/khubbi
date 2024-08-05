import {getJWTToken, getRefreshToken, isAuthenticated, isRefreshTokenValid} from "../selectors";
import {initialAuthorizationState} from "../state";
import {initialState} from "../../../store/state";

describe('isAuthenticated', () => {

    test('should confirm authentication', () => {
        const state = {
            ...initialState,
            authorizationState: {
                ...initialAuthorizationState,
                jwtToken: {
                    token: "some-token",
                    expiresIn: Date.now() + 1000000
                }
            },

        }
        const result = isAuthenticated(state)
        expect(result).toEqual(true)
    })
    test('should confirm that user is not authenticated', () => {
        const state = {
            ...initialState,
            authorizationState: {
                ...initialAuthorizationState,
                jwtToken: null
            },
        }
        const result = isAuthenticated(state)
        expect(result).toEqual(false)
    })
})

describe('getJWTToken', () => {
    test('should return null as token is empty', () => {
        const state = {
            ...initialState,
            authorizationState: {
                ...initialAuthorizationState,
                jwtToken: null
            },
        }
        const result = getJWTToken(state)
        expect(result).toEqual(undefined)
    })
    test('should return token', () => {
        const state = {
            ...initialState,
            authorizationState: {
                ...initialAuthorizationState,
                jwtToken: {
                    token: "some-token",
                    expiresIn: Date.now()
                }
            },
        }
        const result = getJWTToken(state)
        expect(result).toEqual("some-token")
    })
})

describe('getRefreshToken', () => {
    test('should return null as token is empty', () => {
        const state = {
            ...initialState,
            authorizationState: {
                ...initialAuthorizationState,
                refreshToken: null
            },
        }
        const result = getRefreshToken(state)
        expect(result).toEqual(undefined)
    })
    test('should return token', () => {
        const state = {
            ...initialState,
            authorizationState: {
                ...initialAuthorizationState,
                refreshToken: {
                    token: "some-token",
                    expiresIn: Date.now()
                }
            },
        }
        const result = getRefreshToken(state)
        expect(result).toEqual("some-token")
    })
})

describe('isRefreshTokenValid', () => {
    test('should return false as token is empty', () => {
        const state = {
            ...initialState,
            authorizationState: {
                ...initialAuthorizationState,
                refreshToken: null
            },
        }
        const result = isRefreshTokenValid(state)
        expect(result).toEqual(false)
    })
    test('should return true if token is valid', () => {
        const state = {
            ...initialState,
            authorizationState: {
                ...initialAuthorizationState,
                refreshToken: {
                    token: "some-token",
                    expiresIn: Date.now() + 1000000
                }
            },
        }
        const result = isRefreshTokenValid(state)
        expect(result).toEqual(true)
    })
    test('should return false if token expired', () => {
        const state = {
            ...initialState,
            authorizationState: {
                ...initialAuthorizationState,
                refreshToken: {
                    token: "some-token",
                    expiresIn: Date.now() - 1000000
                }
            },
        }
        const result = isRefreshTokenValid(state)
        expect(result).toEqual(false)
    })
})
