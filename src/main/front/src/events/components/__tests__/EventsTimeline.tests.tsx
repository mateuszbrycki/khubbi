import ShallowRenderer from "react-test-renderer/shallow";
import EventsTimeline from "../EventsTimeline";
import {List} from "immutable";


it('renders timeline with events', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<EventsTimeline events={List.of({
        note: "event-1",
        id: "event-1",
        date: "23/07/2024"
    }, {
        note: "event-2",
        id: "event-2",
        date: "23/07/2024"
    })}
                                    loadEvents={() => null}
    />)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});