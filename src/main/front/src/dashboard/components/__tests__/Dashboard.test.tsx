import ShallowRenderer from "react-test-renderer/shallow";
import Dashboard from "../Dashboard";
import {List} from "immutable";

it('renders empty list', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<Dashboard events={List.of()} loadEvents={() => null}/>)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});

it('renders list of events', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<Dashboard events={List.of({name: "event-1", id: "event-1"}, {name: "event-2", id: "event-2"})}
                               loadEvents={() => null}/>)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});