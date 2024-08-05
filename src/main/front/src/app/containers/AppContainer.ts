import {connect, MapDispatchToProps, MapStateToProps} from 'react-redux'
import {ApplicationState} from "../../store/state";
import App, {AppActionProps, AppProps} from "../components/App";
import {isAuthenticated} from "../../authorization/store/selectors";
import {CheckJWTExpiredAction, LogoutUserAction} from "../../authorization/store/actions";
import {AlertMessage} from "../../types";
import {getAlerts} from "../../alerts/store/selectors";
import {HideAlertAction} from "../../alerts/store/actions";

const mapStateToProps: MapStateToProps<AppProps, {}, ApplicationState> = (state: ApplicationState) => ({
    isAuthenticated: isAuthenticated(state),
    authenticationCheckInterval: 600_000,
    alerts: getAlerts(state)
})

const mapDispatchToProps: MapDispatchToProps<AppActionProps, {}> = (dispatch) => ({
    logoutUser: () => dispatch(LogoutUserAction()),
    checkIfUserIsAuthenticated: () => dispatch(CheckJWTExpiredAction()),
    hideAlert: (alertMessage: AlertMessage) => dispatch(HideAlertAction(alertMessage))
})

const AppContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(App)

export default AppContainer