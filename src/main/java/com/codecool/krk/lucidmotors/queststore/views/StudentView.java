package com.codecool.krk.lucidmotors.queststore.views;

import com.codecool.krk.lucidmotors.queststore.controllers.StudentController;
import com.codecool.krk.lucidmotors.queststore.enums.StudentOptions;
import com.codecool.krk.lucidmotors.queststore.exceptions.DaoException;
import com.codecool.krk.lucidmotors.queststore.matchers.CustomMatchers;
import com.codecool.krk.lucidmotors.queststore.models.*;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.util.List;
import java.util.Map;

public class StudentView {
    private StudentController studentController;
    private User user;
    private Map<String, String> formData;

    public StudentView(User user, Map<String, String> formData) throws DaoException {
        this.user = user;
        this.formData = formData;
        this.studentController = new StudentController();
    }

    public Activity getActivity(StudentOptions studentOption) throws DaoException{

        String response;
        JtwigTemplate template = JtwigTemplate.classpathTemplate("/templates/main.twig");
        JtwigModel model = JtwigModel.newModel();

        try {
            insertData(studentOption, model);

        } catch (DaoException e) {
            e.printStackTrace();
        }

        Student student = this.studentController.getStudent(user.getId());

        model.with("user", student);
        model.with("role", student.getClass().getSimpleName());
        model.with("level", this.studentController.getUserLevel(student));
        model.with("title", studentOption.toString());
        model.with("menu_path", "classpath:/templates/snippets/student-menu-snippet.twig");
        model.with("json", "");

        String contentPath = "classpath:/" + studentOption.getPath();
        model.with("content_path", contentPath);

        response = template.render(model);

        return new Activity(200, response);
    }

    private void insertData(StudentOptions studentOption, JtwigModel model) throws DaoException {
        switch (studentOption) {
            case SHOW_WALLET:
                showWallet(model);
                break;

            case SHOW_AVAILABLE_ARTIFACTS:
                showAvailableArtifacts(model);
                break;

            case BUY_ARTIFACT:
                handleArtifactShopping(model);
                break;

            case AVAILABLE_CONTRIBUTIONS:
                handleJoinContribution(model);
                break;

            case CREATE_CONTRIBUTION:
                handleCreateContribution(model);
                break;

            case CLOSE_CONTRIBUTION:
                handleCloseContribution(model);
                break;
        }
    }

    private void showWallet(JtwigModel model) throws DaoException {
        List<BoughtArtifact> boughtArtifacts = studentController.getWallet(this.user);

        List<StackedBoughtArtifact> stackedBoughtArtifacts =
                StackedBoughtArtifact.getUserStackedBoughtArtifacts(boughtArtifacts);

        model.with("stacked_bought_artifacts", stackedBoughtArtifacts);
    }

    private void showAvailableArtifacts(JtwigModel model) throws DaoException {
        List<ShopArtifact> shopArtifacts = studentController.getShopArtifacts();
        model.with("shop_artifacts", shopArtifacts);
    }

    private void handleArtifactShopping(JtwigModel model) throws DaoException {
        if (formData.containsKey("choosen-artifact")) {
            boolean wasSuccesfullyBought = studentController.buyArtifact(this.formData, this.user);
            model.with("is_text_available", true);

            String message;
            if (wasSuccesfullyBought) {
                message = "Artifact succesfuly bought!";
            } else {
                message = "Sorry but you dont have enough money!";
            }

            model.with("text", message);

        }

        List<ShopArtifact> shopArtifacts1 = studentController.getShopArtifacts();
        model.with("shop_artifact", shopArtifacts1);
    }

    private void handleJoinContribution(JtwigModel model) throws DaoException {
        if (formData.containsKey("spent-coins-amount") && formData.containsKey("choosen-contribution")
                && CustomMatchers.isPositiveInteger(formData.get("spent-coins-amount"))) {

            model.with("is_text_available", true);
            boolean succesfullyPaid = studentController.takePartInContribution(formData, user);

            String message;
            if (succesfullyPaid) {
                message = "Succesfuly Paid! :)";
            } else {
                message = "Sorry but you dont have enough money!";
            }

            model.with("text", message);
        }

        List<Contribution> contributions = studentController.getAvailableContributions();
        model.with("available_contributions", contributions);
    }

    private void handleCreateContribution(JtwigModel model) throws DaoException {
        if (formData.containsKey("contribution-name") && formData.containsKey("choosen-artifact-for-contribution")) {
            String message = studentController.addNewContribution(formData, user);
            model.with("is_text_available", true);
            model.with("text", message);
        }

        List<ShopArtifact> shopArtifacts1 = studentController.getShopArtifacts();
        model.with("shop_artifact", shopArtifacts1);
    }

    private void handleCloseContribution(JtwigModel model) throws DaoException {
        boolean wasSuccesfulyClosed = studentController.closeUserContribution(formData);

        if (wasSuccesfulyClosed) {
            model.with("is_text_available", true);
            model.with("text", "Succesfully closed contribution!");
        }

        List<Contribution> userContributions = studentController.getThisUserContributions(user);
        model.with("user_contributions", userContributions);
    }
}
