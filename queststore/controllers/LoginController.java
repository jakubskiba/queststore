package questore.controllers;

import views.View;
import models.School;
import models.User;
import models.Student;
import models.Mentor;
import models.Manager;


public class LoginController {

    private School school;
    private View view = new View();

    public LoginController(School school) {
        this.school = school;
    }

    public void start() {
        String username = view.getInput("Please provide your username: ");
        String password = view.getInput("Please provide your password: ");

        try {
            User user = login(username, password);
            runUserController(user);
        } catch (WrongPasswordException e) {
            view.print(e.getMessage());
        }

    }

    private User login(String login, String password) throws WrongPasswordException {
        ArrayList<User> users = school.getAllUsers();

        for (User user : users) {
            String correctLogin = user.getLogin();
            String correctPassword = user.getPassword();

            if (correctLogin.equals(login) && correctPassword.equals(password)) {
                return user;
            }
        }

        throw WrongPasswordException("Invalid login or password!");
    }

    private void runUserController(User user) {
        if (user instanceof Manager) {
            new ManagerController().startController();
        }
        else if (user instanceof Mentor) {
            new MentorController().startController();
        }
        else if (user instanceof Student) {
            new StudentController().startController();
        }
    }
}
