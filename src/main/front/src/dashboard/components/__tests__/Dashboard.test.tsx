import ShallowRenderer from "react-test-renderer/shallow";
import Dashboard from "../Dashboard";
import {List} from "immutable";
import {EventDate} from "../../../types";

it('renders list of events', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<Dashboard
        events={List.of({note: "event-1", id: "event-1", date: EventDate.ofDateAndTime("2024-07-12T20:00").atUTC()}, {
            note: "event-2",
            id: "event-2",
            date: EventDate.ofDateAndTime("2024-07-12T21:00").atUTC()
        })}
        showEventForm={null}
        loadEvents={() => null}
        openAddEventForm={() => null}
        closeAddEventForm={() => null}
        addNote={() => null}
        addPhoto={() => null}
    />)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});