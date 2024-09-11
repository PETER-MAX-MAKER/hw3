package dao;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao {
    boolean addUser(String username, String password) throws SQLException;
    boolean isUsernameExists(String username) throws SQLException;
    Optional<String> getPassword(String username) throws SQLException;
    boolean validateUser(String username, String password) throws SQLException;
}
