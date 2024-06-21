import RegisterForm from "../RegisterForm";
import ShallowRenderer from "react-test-renderer/shallow";

it('renders correctly', () => {
    const renderer = ShallowRenderer.createRenderer()
    renderer.render(<RegisterForm registerUser={(email, password) => null} />)
    expect(renderer.getRenderOutput()).toMatchSnapshot()
});