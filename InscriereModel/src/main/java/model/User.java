package model;

import java.io.Serializable;

public class User implements HasId<String>,Serializable{
    private String id;
    private String parola;

    public User(String id, String parola) {
        this.id = id;
        this.parola = parola;
    }

    public User() {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String s) {
        this.id =s;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public String toString() {
        return "model.User{" +
                "id='" + id + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }
}