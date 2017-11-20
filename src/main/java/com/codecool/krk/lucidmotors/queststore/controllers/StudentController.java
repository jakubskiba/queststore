//package com.codecool.krk.lucidmotors.queststore.controllers;
//
//import com.codecool.krk.lucidmotors.queststore.dao.AchievedQuestDao;
//import com.codecool.krk.lucidmotors.queststore.dao.ArtifactOwnersDao;
//import com.codecool.krk.lucidmotors.queststore.dao.ExperienceLevelsDao;
//import com.codecool.krk.lucidmotors.queststore.enums.StudentControllerMenuOptions;
//import com.codecool.krk.lucidmotors.queststore.exceptions.DaoException;
//import com.codecool.krk.lucidmotors.queststore.models.Student;
//
//
//public class StudentController extends AbstractUserController<Student> {
//
//    //private final StudentView studentView = null;
//
//    protected void handleUserRequest(String userChoice) throws DaoException {
//
//        StudentControllerMenuOptions chosenOption = getEnumValue(userChoice);
//
//        switch (chosenOption) {
//
//            case START_STORE_CONTROLLER:
//                startStoreController();
//                break;
//
//            case SHOW_LEVEL:
//                showLevel();
//                break;
//
//            case SHOW_WALLET:
//                showWallet();
//                break;
//
//            case EXIT:
//                break;
//
//            case DEFAULT:
//                handleNoSuchCommand();
//                break;
//        }
//    }
//
//    private StudentControllerMenuOptions getEnumValue(String userChoice) {
//        StudentControllerMenuOptions chosenOption;
//
//        try {
//            chosenOption = StudentControllerMenuOptions.values()[Integer.parseInt(userChoice)];
//        } catch (IndexOutOfBoundsException | NumberFormatException e) {
//            chosenOption = StudentControllerMenuOptions.DEFAULT;
//        }
//
//        return chosenOption;
//    }
//
//    protected void showMenu() {
//        this.studentView.printStudentMenu();
//    }

//
//    private void showLevel() throws DaoException {
//
//        Integer level = new ExperienceLevelsDao().getExperienceLevels().computeStudentLevel(this.user.getEarnedCoins());
//        this.userInterface.println(String.format("Your level: %d", level));
//        this.userInterface.pause();
//    }
//
//    private void startStoreController() throws DaoException {
//
//        new StudentStoreController().startController(this.user, this.school);
//    }
//}


package com.codecool.krk.lucidmotors.queststore.controllers;

import com.codecool.krk.lucidmotors.queststore.dao.ArtifactOwnersDao;
import com.codecool.krk.lucidmotors.queststore.dao.ContributionDao;
import com.codecool.krk.lucidmotors.queststore.dao.ShopArtifactDao;
import com.codecool.krk.lucidmotors.queststore.dao.StudentDao;
import com.codecool.krk.lucidmotors.queststore.exceptions.DaoException;
import com.codecool.krk.lucidmotors.queststore.models.*;

import java.util.*;

import static java.lang.Integer.parseInt;

public class StudentController {

    private ArtifactOwnersDao artifactOwnersDao;
    private ShopArtifactDao shopArtifactDao;
    private StudentDao studentDao;
    private ContributionDao contributionDao;

    public StudentController() throws DaoException {
        this.shopArtifactDao = new ShopArtifactDao();
        this.artifactOwnersDao = new ArtifactOwnersDao();
        this.studentDao = new StudentDao();
        this.contributionDao = new ContributionDao();
    }

    public List<BoughtArtifact> getWallet(User student) throws DaoException {
        return this.artifactOwnersDao.getArtifacts(student);
    }

    public List<ShopArtifact> getShopArtifacts() throws DaoException {
        return this.shopArtifactDao.getAllArtifacts();
    }

    public boolean buyArtifact(Map<String, String> formData, User user) throws DaoException {
        final String key = "choosen-artifact";
        Integer artifactId = parseInt(formData.get(key));

        Student student = this.studentDao.getStudent(user.getId());
        ShopArtifact shopArtifact = shopArtifactDao.getArtifact(artifactId);

        Integer artifactPrice = shopArtifact.getPrice();
        Integer studentPossesedCoins = student.getPossesedCoins();

        if (studentPossesedCoins >= artifactPrice) {
            student.setPossesedCoins(studentPossesedCoins - artifactPrice);

            new BoughtArtifact(shopArtifact).save(new ArrayList<>(Collections.singletonList(student)));
            this.studentDao.update(student);

            return true;
        }

        return false;
    }

    public List<Contribution> getAvailableContributions() throws DaoException {
        return this.contributionDao.getOpenContributions();
    }
}