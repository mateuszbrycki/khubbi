import {authorizationReducer} from "../reducers";
import {LoginUserAction, Types, UserLoggedInAction} from "../actions";
import {initialAuthorizationState} from "../state";

test('should return the initial state', () => {
    expect(authorizationReducer(undefined, {type: 'unknown'})).toEqual(
        {...initialAuthorizationState}
    )
})

test('should store JWT Token and expiration period', () => {
    expect(authorizationReducer(initialAuthorizationState, UserLoggedInAction("token", 123))).toEqual(
        {...initialAuthorizationState, jwtToken: "token", expiresIn: 123}
    )
})