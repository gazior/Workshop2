package pl.coderslab.entity;

import pl.coderslab.bcrypt.BCrypt;

public class Main {


    public static void main(String[] args) {

        String password = "password";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println(hashed);
    }
}
