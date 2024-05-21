import {connect, MapDispatchToProps, MapStateToProps} from 'react-redux'
import {ApplicationState} from "../../store/state";
import {getUserState} from '../store/selectors'
import {LoginUserAction, RegisterUserAction} from "../store/actions";
import EntryForms, {EntryFormsActionProps, EntryFormsProps} from "../components/EntryForms";

const mapStateToProps: MapStateToProps<EntryFormsProps, {}, ApplicationState> = (state: ApplicationState) => ({
    user: getUserState(state)
})

const mapDispatchToProps: MapDispatchToProps<EntryFormsActionProps, {}> = (dispatch) => ({
    registerUser: (email: string, password: string) => dispatch(RegisterUserAction(email, password)),
    loginUser: (email: string, password: string) => dispatch(LoginUserAction(email, password))
})

const AuthorizationContainer = connect(
mapStateToProps,
    mapDispatchToProps
)(EntryForms)

export default AuthorizationContainer