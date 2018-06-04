package epam.sections;

import com.epam.jdi.uitests.web.selenium.elements.common.Button;
import com.epam.jdi.uitests.web.selenium.elements.common.TextField;
import com.epam.jdi.uitests.web.selenium.elements.composite.Form;
import com.epam.jdi.uitests.web.selenium.elements.pageobjects.annotations.JFindBy;
import epam.entities.User;

public class LoginForm extends Form<User> {
    @JFindBy(id="Name") public TextField name;
    @JFindBy(id="Password") public TextField password;

    @JFindBy(css="[type=submit]") public Button enter;
}
