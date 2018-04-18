package model;

import java.io.Serializable;

public class User implements HasId<String>,Serializable{
    private String userId;
    private String parola;

    public User(String userId, String parola) {
        this.userId = userId;
        this.parola = parola;
    }

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public void setId(String s) {
        this.userId=s;
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
                "userId='" + userId + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }
}