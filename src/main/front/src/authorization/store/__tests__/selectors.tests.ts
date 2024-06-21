import {getJWTToken, isAuthenticated} from "../selectors";
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
