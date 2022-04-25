package pl.coderslab.entity;

import pl.coderslab.bcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;

public class UserDao {

    public static final String CREATE_USER_QUERY = "INSERT INTO users(username,email,password) VALUES(?,?,?)";
    public static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    public static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?,email = ?,password = ? WHERE id = ?";
    public static final String SELECT_USER_QUERY = "SELECT id,username,email,password FROM users WHERE id = ?";

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user) {

        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(User user) {

        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_USER_QUERY);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int Userid) {
        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_USER_QUERY);
            statement.setInt(1, Userid);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public User read(int userId) {

        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_QUERY);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            int id;
            String userName, password, email;

            while (resultSet.next()) {
                id = resultSet.getInt("id");
                userName = resultSet.getString("username");
                email = resultSet.getString("email");
                password = resultSet.getString("password");
                User user = new User(id, userName, email, password);
                return user;
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private User[] addToArray(User user, User[] users) {
        users = Arrays.copyOf(users, users.length + 1);
        users[users.length - 1] = user;
        return users;


    }

    public User[] findAll() {

        User[] userArr = new User[0];

        try (Connection connection = DbUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User(resultSet.getInt("id")
                        , resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("password"));
                userArr = addToArray(user, userArr);

            }
            return userArr;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
