import {connect, MapDispatchToProps, MapStateToProps} from "react-redux";
import App, {AppActionsProps, AppProps} from "./App";
import {State} from "./store/state";


const mapStateToProps: MapStateToProps<AppProps, { }, State> = (state) => ({
})

const mapDispatchToProps: MapDispatchToProps<AppActionsProps, { }> = (dispatch) => ({

})

const AppContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(App)

export default AppContainer