import ShallowRenderer from "react-test-renderer/shallow";
import Dashboard from "../Dashboard";

it('renders correctly', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<Dashboard loadUserShelves={() => null}/>)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});