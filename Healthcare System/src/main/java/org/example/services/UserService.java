package org.example.services;

import org.example.dao.UserDAO;
import org.example.models.User;
import org.example.utils.ValidationException;
import org.example.utils.Validator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService(Connection conn) {
        this.userDAO = new UserDAO(conn);
    }

    public User authenticate(String username, String password) throws SQLException, ValidationException {
        Validator.validateNotEmpty(username, "Username");
        Validator.validateNotEmpty(password, "Password");
        
        User user = userDAO.authenticate(username, password);
        if (user == null) {
            throw new ValidationException("Invalid username or password");
        }
        return user;
    }

    public void createUser(User user) throws SQLException, ValidationException {
        Validator.validateNotEmpty(user.getUsername(), "Username");
        Validator.validateNotEmpty(user.getPassword(), "Password");
        Validator.validateNotEmpty(user.getRole(), "Role");
        Validator.validateName(user.getFirstName(), "First Name");
        Validator.validateName(user.getLastName(), "Last Name");
        Validator.validateEmail(user.getEmail());
        
        userDAO.create(user);
    }

    public User getUser(Integer id) throws SQLException {
        return userDAO.findById(id);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    public void updateUser(User user) throws SQLException, ValidationException {
        Validator.validateNotEmpty(user.getUsername(), "Username");
        Validator.validateName(user.getFirstName(), "First Name");
        Validator.validateName(user.getLastName(), "Last Name");
        Validator.validateEmail(user.getEmail());
        
        userDAO.update(user);
    }

    public void deleteUser(Integer id) throws SQLException {
        userDAO.delete(id);
    }
}
