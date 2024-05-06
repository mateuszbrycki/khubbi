import LoginForm from "../LoginForm";
import ShallowRenderer from "react-test-renderer/shallow";

it('renders correctly', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<LoginForm user={null} loginUser={(email, password) => null}/>)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});