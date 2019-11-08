package com.eg.crudmysql;

public class Dto_Login {

    String password;
    String email;

    public Dto_Login() {
    }

    public Dto_Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
