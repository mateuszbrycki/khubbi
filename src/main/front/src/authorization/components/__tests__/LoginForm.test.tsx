import LoginForm from "../LoginForm";
import ShallowRenderer from "react-test-renderer/shallow";

it('renders correctly', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<LoginForm loginUser={(email, password) => null}/>)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});