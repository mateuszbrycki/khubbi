import ShallowRenderer from "react-test-renderer/shallow";
import Dashboard from "../Dashboard";
import {List} from "immutable";

it('renders empty list', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<Dashboard shelves={List.of()} loadShelves={() => null}/>)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});

it('renders list of shelves', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<Dashboard shelves={List.of({name: "shelf-1", id: "shelf-1"}, {name: "shelf-2", id: "shelf-2"})}
                               loadShelves={() => null}/>)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});