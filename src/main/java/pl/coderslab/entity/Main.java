package pl.coderslab.entity;

import java.util.Arrays;

public class Main {


    public static void main(String[] args) {


        User newUser;

        UserDao userDao = new UserDao();
        newUser = userDao.read(4);
        User[] userArr = userDao.findAll();
        System.out.println(Arrays.toString(userArr));


    }
}
