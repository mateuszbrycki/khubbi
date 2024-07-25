import ShallowRenderer from "react-test-renderer/shallow";
import EventsTimeline from "../EventsTimeline";
import {List} from "immutable";
import {EventDate} from "../../../types";


it('renders timeline with events', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<EventsTimeline events={List.of({
        note: "event-1",
        id: "event-1",
        date: EventDate.ofDateAndTime("2024-07-12T20:00").atUTC()
    }, {
        note: "event-2",
        id: "event-2",
        date: EventDate.ofDateAndTime("2024-07-12T21:00").atUTC()
    })}
                                    loadEvents={() => null}
    />)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});