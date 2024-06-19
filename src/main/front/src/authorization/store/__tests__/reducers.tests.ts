import {authorizationReducer} from "../reducers";
import {UserLoggedInAction} from "../actions";
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