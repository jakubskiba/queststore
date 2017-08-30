package queststore.views;

import java.util.Scanner;
import java.util.List;
import queststore.dao.FileLoader;

public class UserInterface{
    private Scanner in = new Scanner(System.in);
    private FileLoader dataLoader = new FileLoader();

    public String getInput(String question){
        System.out.print(question + ": ");
        return in.nextLine().trim();
    }

    public void print(String text){
        System.out.println(text);
    }

    public void clearWindow() {
        for(int i = 0; i < 50; i ++) {
            System.out.println();
        }
    }

    public void lockActualState() {
        System.out.print("\nPress enter to continue...");
        in.nextLine();
    }

    public void printMentorMenu() {
        String fileName = "queststore/csv/mentorMenu.csv";
        List<String> loadedMenu = dataLoader.getDataFromFile(fileName);

        printMenu(loadedMenu);
    }

    public void printStudentMenu() {
        String fileName = "queststore/csv/studentMenu.csv";
        List<String> loadedMenu = dataLoader.getDataFromFile(fileName);

        printMenu(loadedMenu);
    }

    public void printStudentStoreMenu() {
        String fileName = "queststore/csv/studentStoreMenu.csv";
        List<String> loadedMenu = dataLoader.getDataFromFile(fileName);

        printMenu(loadedMenu);
    }

    public void printManagerMenu() {
        String fileName = "queststore/csv/managerMenu.csv";
        List<String> loadedMenu = dataLoader.getDataFromFile(fileName);

        printMenu(loadedMenu);
    }

    private void printMenu(List<String> data) {
        clearWindow();
        printAllRecordsInMenuStyle(data);
    }

    private void printAllRecordsInMenuStyle(List<String> data) {
        String title = data.get(0);

        System.out.println(title);

        for(int i = 1; i < data.size(); i++) {
            String index = (Integer.toString(i % (data.size() - 1)));
            String row = data.get(i);

            System.out.println("\t" + "(" + index + ") " + row);
        }

        System.out.println();
    }


}
