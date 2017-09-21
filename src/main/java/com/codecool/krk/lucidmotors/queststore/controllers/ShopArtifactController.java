package com.codecool.krk.lucidmotors.queststore.controllers;

import com.codecool.krk.lucidmotors.queststore.dao.ShopArtifactDao;
import com.codecool.krk.lucidmotors.queststore.views.UserInterface;

class ShopArtifactController {
    private final UserInterface userInterface = new UserInterface();

    public void showAvailableArtifacts() {

        this.userInterface.printStoreArtifacts(new ShopArtifactDao().getAllArtifacts());
        this.userInterface.lockActualState();
    }

}