import {connect, MapDispatchToProps, MapStateToProps} from 'react-redux'
import {State} from "../../store/state";
import {getUserState} from '../store/selectors'
import {RegisterUserAction} from "../store/actions";
import RegisterForm, {RegisterFormActionProps, RegisterFormProps} from "../components/RegisterForm";

const mapStateToProps: MapStateToProps<RegisterFormProps, {}, State> = (state: State) => ({
    user: getUserState(state)
})

const mapDispatchToProps: MapDispatchToProps<RegisterFormActionProps, {}> = (dispatch) => ({
    registerUser: (email: string, password: string) => dispatch(RegisterUserAction(email, password))
})

const AuthorizationContainer = connect(
mapStateToProps,
    mapDispatchToProps
)(RegisterForm) // TODO mateusz.brycki redesign to a general component that depends on two forms (login and registration)

export default AuthorizationContainer