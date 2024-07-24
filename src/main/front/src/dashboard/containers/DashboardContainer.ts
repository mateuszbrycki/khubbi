import {connect, MapDispatchToProps, MapStateToProps} from 'react-redux'
import {ApplicationState} from "../../store/state";
import Dashboard, {DashboardActionProps, DashboardProps} from "../components/Dashboard";
import {
    AddNoteAction,
    CloseAddEventFormAction,
    LoadEventsAction,
    OpenAddEventFormAction
} from "../../events/store/actions";
import {getEvents, getOpenAddEventForm} from "../../events/store/selectors";
import {EventForms} from "../../types";

const mapStateToProps: MapStateToProps<DashboardProps, {}, ApplicationState> = (state: ApplicationState) => ({
    events: getEvents(state),
    showEventForm: getOpenAddEventForm(state)

})

const mapDispatchToProps: MapDispatchToProps<DashboardActionProps, {}> = (dispatch) => ({
    loadEvents: () => dispatch(LoadEventsAction()),
    openAddEventForm: (type: EventForms) => dispatch(OpenAddEventFormAction(type)),
    closeAddEventForm: () => dispatch(CloseAddEventFormAction()),
    addNote: (note: string, date: string) => dispatch(AddNoteAction(note, date))
})

const DashboardContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(Dashboard)

export default DashboardContainer