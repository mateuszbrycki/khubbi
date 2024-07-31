import ShallowRenderer from "react-test-renderer/shallow";
import {EventDate} from "../../../types";
import EventEntry from "../EventEntry";

it('renders event entry ', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<EventEntry event={{
        note: "event-1",
        id: "event-1",
        date: EventDate.ofDateAndTime("2024-07-12T20:00").atUTC()
    }}
    />)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});