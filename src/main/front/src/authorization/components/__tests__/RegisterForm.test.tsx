import RegisterForm from "../RegisterForm";
import ShallowRenderer from "react-test-renderer/shallow";

it('renders correctly', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<RegisterForm user={null} registerUser={(email, password) => null} />)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});