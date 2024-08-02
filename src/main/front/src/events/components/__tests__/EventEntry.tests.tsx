import ShallowRenderer from "react-test-renderer/shallow";
import {EventDate, Note, Photo} from "../../../types";
import EventEntry from "../EventEntry";

it('renders event entry with note ', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<EventEntry event={new Note(
        "event-1-id",
        EventDate.ofDateAndTime("2024-07-12T20:00").atUTC(),
        "event-1-note")
    }
    />)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});

it('renders event entry with photo ', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<EventEntry event={new Photo(
        "event-1-id",
        EventDate.ofDateAndTime("2024-07-12T20:00").atUTC(),
        "event-1-description",
        "event-1-photo-url",
    )
    }
    />)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});

it('renders event entry with no content when unrecognized entry ', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<EventEntry event={{
        note: "event-1",
        id: "event-1",
        date: EventDate.ofDateAndTime("2024-07-12T20:00").atUTC()
    }}
    />)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});

