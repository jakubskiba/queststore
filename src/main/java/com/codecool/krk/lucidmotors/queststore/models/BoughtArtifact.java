package com.codecool.krk.lucidmotors.queststore.models;

import com.codecool.krk.lucidmotors.queststore.dao.BoughtArtifactDao;
import com.codecool.krk.lucidmotors.queststore.exceptions.DaoException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BoughtArtifact extends AbstractArtifact {

    private Date date;
    private boolean isUsed;
    private BoughtArtifactDao boughtArtifactDao = new BoughtArtifactDao();

    public BoughtArtifact(ShopArtifact shopArtifact) throws DaoException {

        super(shopArtifact.getName(), shopArtifact.getPrice(), shopArtifact.getArtifactCategory(),
                shopArtifact.getDescription());
        this.date = new Date();
        this.isUsed = false;
    }

    public BoughtArtifact(String name, Integer price, ArtifactCategory artifactCategory, String description,
                          Integer id, Date date, boolean isUsed) throws DaoException {

        super(name, price, artifactCategory, description, id);
        this.date = date;
        this.isUsed = isUsed;
    }

    public void markAsUsed() {
        this.isUsed = true;
    }

    public boolean isUsed() {
        return this.isUsed;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        String dateString = this.convertDateToString(this.getDate());
        if (this.isUsed) {
            return String.format("id: %s. name: %s,  date: %s, %s", this.getId(), this.getName(),
                    dateString, "is used");
            
        } else {
            return String.format("id: %s. name: %s,  date: %s, %s", this.getId(), this.getName(),
                    dateString, "isn't used");
        }
    }

    public void save(ArrayList<Student> owners) throws DaoException {
        boughtArtifactDao.save(this, owners);
    }

    public void update() throws DaoException {
        boughtArtifactDao.updateArtifact(this);
    }

    private String convertDateToString(Date purchaseDate) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String purchaseDateString = dateFormatter.format(purchaseDate);

        return purchaseDateString;
    }

}
