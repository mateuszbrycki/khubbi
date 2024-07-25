import ShallowRenderer from "react-test-renderer/shallow";
import Dashboard from "../Dashboard";
import {List} from "immutable";
import {EventDate} from "../../../types";

it('renders list of events', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<Dashboard
        events={List.of({note: "event-1", id: "event-1", date: EventDate.ofDateAndTime("2024-07-12T20:00")}, {
            note: "event-2",
            id: "event-2",
            date: EventDate.ofDateAndTime("2024-07-12T21:00")
        })}
        showEventForm={null}
        loadEvents={() => null}
        openAddEventForm={() => null}
        closeAddEventForm={() => null}
        addNote={() => null}
    />)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});