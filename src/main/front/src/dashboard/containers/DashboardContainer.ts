import {connect, MapDispatchToProps, MapStateToProps} from 'react-redux'
import {ApplicationState} from "../../store/state";
import Dashboard, {DashboardActionProps, DashboardProps} from "../components/Dashboard";
import {LoadShelvesAction} from "../store/actions";
import {getUserState} from "../../authorization/store/selectors";

const mapStateToProps: MapStateToProps<DashboardProps, {}, ApplicationState> = (state: ApplicationState) => ({
    user: getUserState(state)
})

const mapDispatchToProps: MapDispatchToProps<DashboardActionProps, {}> = (dispatch) => ({
    loadUserShelves: (email: string, password: string) => dispatch(LoadShelvesAction(email, password)),
})

const DashboardContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(Dashboard)

export default DashboardContainer