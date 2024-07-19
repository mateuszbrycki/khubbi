import {connect, MapDispatchToProps, MapStateToProps} from 'react-redux'
import {ApplicationState} from "../../store/state";
import {AppActionProps, AppProps} from "../components/App";
import App from "../components/App";
import {isAuthenticated} from "../../authorization/store/selectors";
import {CheckJWTExpiredAction, LogoutUserAction} from "../../authorization/store/actions";

const mapStateToProps: MapStateToProps<AppProps, {}, ApplicationState> = (state: ApplicationState) => ({
    isAuthenticated: isAuthenticated(state),
    authenticationCheckInterval: 600_000
})

const mapDispatchToProps: MapDispatchToProps<AppActionProps, {}> = (dispatch) => ({
    logoutUser: () => dispatch(LogoutUserAction()),
    checkIfUserIsAuthenticated: () => dispatch(CheckJWTExpiredAction())
})

const AppContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(App)

export default AppContainer