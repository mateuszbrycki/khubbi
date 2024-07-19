import {getJWTToken, getRefreshToken, isAuthenticated, isRefreshTokenValid} from "../selectors";
import {initialShelvesState} from "../../../shelves/store/state";
import {initialAuthorizationState} from "../state";

describe('isAuthenticated', () => {

    test('should confirm authentication', () => {
        const state = {
            application: {
                shelvesState: initialShelvesState,
                authorizationState: {
                    ...initialAuthorizationState,
                    jwtToken: {
                        token: "some-token",
                        expiresIn: Date.now() + 1000000
                    }
                },
            }
        }
        const result = isAuthenticated(state)
        expect(result).toEqual(true)
    })
    test('should confirm that user is not authenticated', () => {
        const state = {
            application: {
                shelvesState: initialShelvesState,
                authorizationState: {
                    ...initialAuthorizationState,
                    jwtToken: null
                },
            }
        }
        const result = isAuthenticated(state)
        expect(result).toEqual(false)
    })
})

describe('getJWTToken', () => {
    test('should return null as token is empty', () => {
        const state = {
            application: {
                shelvesState: initialShelvesState,
                authorizationState: {
                    ...initialAuthorizationState,
                    jwtToken: null
                },
            }
        }
        const result = getJWTToken(state)
        expect(result).toEqual(undefined)
    })
    test('should return token', () => {
        const state = {
            application: {
                shelvesState: initialShelvesState,
                authorizationState: {
                    ...initialAuthorizationState,
                    jwtToken: {
                        token: "some-token",
                        expiresIn: Date.now()
                    }
                },
            }
        }
        const result = getJWTToken(state)
        expect(result).toEqual("some-token")
    })
})

describe('getRefreshToken', () => {
    test('should return null as token is empty', () => {
        const state = {
            application: {
                shelvesState: initialShelvesState,
                authorizationState: {
                    ...initialAuthorizationState,
                    refreshToken: null
                },
            }
        }
        const result = getRefreshToken(state)
        expect(result).toEqual(undefined)
    })
    test('should return token', () => {
        const state = {
            application: {
                shelvesState: initialShelvesState,
                authorizationState: {
                    ...initialAuthorizationState,
                    refreshToken: {
                        token: "some-token",
                        expiresIn: Date.now()
                    }
                },
            }
        }
        const result = getRefreshToken(state)
        expect(result).toEqual("some-token")
    })
})

describe('isRefreshTokenValid', () => {
    test('should return false as token is empty', () => {
        const state = {
            application: {
                shelvesState: initialShelvesState,
                authorizationState: {
                    ...initialAuthorizationState,
                    refreshToken: null
                },
            }
        }
        const result = isRefreshTokenValid(state)
        expect(result).toEqual(false)
    })
    test('should return true if token is valid', () => {
        const state = {
            application: {
                shelvesState: initialShelvesState,
                authorizationState: {
                    ...initialAuthorizationState,
                    refreshToken: {
                        token: "some-token",
                        expiresIn: Date.now() + 1000000
                    }
                },
            }
        }
        const result = isRefreshTokenValid(state)
        expect(result).toEqual(true)
    })
    test('should return false if token expired', () => {
        const state = {
            application: {
                shelvesState: initialShelvesState,
                authorizationState: {
                    ...initialAuthorizationState,
                    refreshToken: {
                        token: "some-token",
                        expiresIn: Date.now() - 1000000
                    }
                },
            }
        }
        const result = isRefreshTokenValid(state)
        expect(result).toEqual(false)
    })
})
