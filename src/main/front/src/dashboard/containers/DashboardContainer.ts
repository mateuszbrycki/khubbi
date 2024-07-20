import {connect, MapDispatchToProps, MapStateToProps} from 'react-redux'
import {ApplicationState} from "../../store/state";
import Dashboard, {DashboardActionProps, DashboardProps} from "../components/Dashboard";
import {LoadEventsAction} from "../../events/store/actions";
import {getEvents} from "../../events/store/selectors";

const mapStateToProps: MapStateToProps<DashboardProps, {}, ApplicationState> = (state: ApplicationState) => ({
    events: getEvents(state)
})

const mapDispatchToProps: MapDispatchToProps<DashboardActionProps, {}> = (dispatch) => ({
    loadEvents: () => dispatch(LoadEventsAction()),
})

const DashboardContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(Dashboard)

export default DashboardContainer