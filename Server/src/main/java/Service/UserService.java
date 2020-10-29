package Service;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.User;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.UserResult;

import java.sql.Connection;

public class UserService {

  public static UserResult getUser(String username) throws DataAccessException {
    Database db = new Database();
    try {
      Connection conn = db.openConnection();
      db.createTables();
      db.createDefaultUser();

      UserDao userDao = new UserDao(conn);
      User user = userDao.getOne(username);

      if (user == null) {
        db.closeConnection(false);
        return new UserResult(false, "no user data associated with username");
      }

      db.closeConnection(true);

      return new UserResult(user);
    } catch (Exception e) {
      e.printStackTrace();
      db.closeConnection(false);
      return new UserResult(false, "error " + e.getMessage());
    }
  }

  public static UserResult insert(RegisterRequest r) throws Exception {
    Database db = new Database();
    try {
      Connection conn = db.openConnection();
      db.createTables();

      UserDao userDao = new UserDao(conn);

      String userName = r.getUsername();
      String password = r.getPassword();
      String firstName = r.getFirstName();
      String lastName = r.getLastName();

      User user = new User(userName, password, firstName, lastName);
      if (userDao.getOne(userName) != null) {
        db.closeConnection(false);
        return new UserResult(false, "error Username already exists");
      }
      userDao.insert(user);

      db.closeConnection(true);

      return new UserResult(true, "Registration Succeeded");
    } catch (DataAccessException e) {
      e.printStackTrace();
      db.closeConnection(false);
      return new UserResult(false, "error " + e.getMessage());
    }
  }

  public static UserResult login(LoginRequest r) throws DataAccessException {
    Database db = new Database();
    try {
      Connection conn = db.openConnection();
      db.createTables();
      db.createDefaultUser();

      String username = r.getUsername();
      String password = r.getPassword();

      UserDao userDao = new UserDao(conn);
      User user = userDao.getOne(username);

      if (user == null) {
        db.closeConnection(false);
        return new UserResult(false, "No user found by username");
      }

      db.closeConnection(true);

      if (user.getPassword().equals(password)) {
        return new UserResult(user);
      } else {
        return new UserResult(false, "Password incorrect");
      }
    } catch (Exception e) {
      e.printStackTrace();
      db.closeConnection(false);
      return new UserResult(false, "error " + e.getMessage());
    }
  }

}
