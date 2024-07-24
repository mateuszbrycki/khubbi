import ShallowRenderer from "react-test-renderer/shallow";
import Dashboard from "../Dashboard";
import {List} from "immutable";

it('renders list of events', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<Dashboard
        events={List.of({note: "event-1", id: "event-1", date: "23/07/2024"}, {
            note: "event-2",
            id: "event-2",
            date: "23/07/2024"
        })}
        showEventForm={null}
        loadEvents={() => null}
        openAddEventForm={() => null}
        closeAddEventForm={() => null}
        addNote={() => null}
    />)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});