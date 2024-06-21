import {connect, MapDispatchToProps, MapStateToProps} from 'react-redux'
import {ApplicationState} from "../../store/state";
import {AppActionProps, AppProps} from "../components/App";
import App from "../components/App";
import {isAuthenticated} from "../../authorization/store/selectors";
import {LogoutUserAction} from "../../authorization/store/actions";

const mapStateToProps: MapStateToProps<AppProps, {}, ApplicationState> = (state: ApplicationState) => ({
    isAuthenticated: isAuthenticated(state)
})

const mapDispatchToProps: MapDispatchToProps<AppActionProps, {}> = (dispatch) => ({
    logoutUser: () => dispatch(LogoutUserAction())
})

const AppContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(App)

export default AppContainer