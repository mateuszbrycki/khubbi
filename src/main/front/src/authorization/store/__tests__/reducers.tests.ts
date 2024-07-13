import {authorizationReducer} from "../reducers";
import {UserJWTTokenRefreshedAction, UserLoggedInAction, UserLoggedOutAction} from "../actions";
import {initialAuthorizationState} from "../state";

test('should return the initial state', () => {
    expect(authorizationReducer(undefined, {type: 'unknown'})).toEqual(
        {...initialAuthorizationState}
    )
})

test('should store JWT Token and expiration period', () => {
    expect(authorizationReducer(initialAuthorizationState, UserLoggedInAction(
        {token: "jwt-token", expiresIn: 123},
        {token: "refresh-token", expiresIn: 456}))).toEqual(
        {
            ...initialAuthorizationState,
            jwtToken: {token: "jwt-token", expiresIn: 123},
            refreshToken: {token: "refresh-token", expiresIn: 456}
        }
    )
})

test('should store new JWT Token and expiration period', () => {
    expect(authorizationReducer(initialAuthorizationState, UserJWTTokenRefreshedAction(
        "jwt-token", 123))).toEqual(
        {
            ...initialAuthorizationState,
            jwtToken: {token: "jwt-token", expiresIn: 123},
        }
    )
})

test('should remove JWT Token and Refresh Token on loggout', () => {
    expect(authorizationReducer(initialAuthorizationState, UserLoggedOutAction())).toEqual(
        {
            ...initialAuthorizationState,
            jwtToken: null,
            refreshToken: null
        }
    )
})