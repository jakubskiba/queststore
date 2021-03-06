package com.codecool.krk.lucidmotors.queststore.models;

import java.sql.SQLException;
import com.codecool.krk.lucidmotors.queststore.dao.ClassDao;
import com.codecool.krk.lucidmotors.queststore.dao.MentorDao;
import com.codecool.krk.lucidmotors.queststore.exceptions.DaoException;
import org.json.JSONArray;
import org.json.JSONObject;

public class Mentor extends User {
    
    private SchoolClass class_;
    private final MentorDao mentorDao = MentorDao.getDao();


    public Mentor(String name, String login, String password, String email, SchoolClass class_) throws DaoException {

        super(name, login, password, email);
        this.class_ = class_;
    }


    public Mentor(String name, String login, String password, String email, SchoolClass class_, Integer id) throws DaoException {

        super(name, login, password, email, id);
        this.class_ = class_;
    }

    public SchoolClass getClas() {
        return this.class_;
    }

    public void setSchoolClass(SchoolClass class_) {
        this.class_ = class_;
    }

    public String getMentorSaveString() {
        return String.format("%d|%s|%s|%s|%s|%d", this.getId(), this.getName(), this.getLogin(), this.getPassword(), this.getEmail(), this.getClas().getId());
    }

    public String getMentorData() {

        return String.format("id: %d. %s %s class name: %s",
                this.getId(),
                this.getName(),
                this.getEmail(),
                this.getClas().getName());
    }

    public String toString() {
        return String.format("id: %d. %s", this.getId(), this.getName());
    }

    public void save() throws DaoException {
        mentorDao.save(this);
    }

    public void update() throws DaoException {
        mentorDao.update(this);
    }

    public JSONObject toJSON() {
        JSONObject mentorJson = super.toJSON();
        mentorJson.put("class_id", this.class_.getId());
        mentorJson.put("class_name", this.class_.getName());
        return mentorJson;
    }

}
