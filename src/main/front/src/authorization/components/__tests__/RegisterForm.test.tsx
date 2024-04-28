import renderer from "react-test-renderer";
import RegisterForm from "../RegisterForm";

it('renders correctly', () => {
    const tree = renderer
        .create(<RegisterForm user={null} registerUser={(email, password) => null} />)
        .toJSON();
    expect(tree).toMatchSnapshot();
});