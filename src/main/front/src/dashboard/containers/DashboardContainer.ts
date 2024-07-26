import {connect, MapDispatchToProps, MapStateToProps} from 'react-redux'
import {ApplicationState} from "../../store/state";
import Dashboard, {DashboardActionProps, DashboardProps} from "../components/Dashboard";
import {
    AddNoteAction, AddPhotoAction,
    CloseAddEventFormAction,
    LoadEventsAction,
    OpenAddEventFormAction
} from "../../events/store/actions";
import {getEvents, getOpenAddEventForm} from "../../events/store/selectors";
import {EventDate, EventForms} from "../../types";

const mapStateToProps: MapStateToProps<DashboardProps, {}, ApplicationState> = (state: ApplicationState) => ({
    events: getEvents(state),
    showEventForm: getOpenAddEventForm(state)

})

const mapDispatchToProps: MapDispatchToProps<DashboardActionProps, {}> = (dispatch) => ({
    loadEvents: () => dispatch(LoadEventsAction()),
    openAddEventForm: (type: EventForms) => dispatch(OpenAddEventFormAction(type)),
    closeAddEventForm: () => dispatch(CloseAddEventFormAction()),
    addNote: (note: string, date: EventDate) => dispatch(AddNoteAction(note, date)),
    addPhoto: (description: string, photo: File, date: EventDate) => dispatch(AddPhotoAction(description, photo, date))
})

const DashboardContainer = connect(
    mapStateToProps,
    mapDispatchToProps
)(Dashboard)

export default DashboardContainer