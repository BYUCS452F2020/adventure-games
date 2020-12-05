package Service;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.User;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.UserResult;

public class UserService {

  public static UserResult getUser(String username) throws DataAccessException {
    try {
      UserDao userDao = new UserDao();
      User user = userDao.getOne(username);

      if (user == null) {
        return new UserResult(false, "no user data associated with username");
      }

      return new UserResult(user);
    } catch (Exception e) {
      e.printStackTrace();
      return new UserResult(false, "error " + e.getMessage());
    }
  }

  public static UserResult insert(RegisterRequest r) throws Exception {
    try {

      UserDao userDao = new UserDao();

      String username = r.getUsername();
      String password = r.getPassword();
      String firstName = r.getFirstName();
      String lastName = r.getLastName();

      User user = new User(username, password, firstName, lastName);
      if (userDao.getOne(username) != null) {
        return new UserResult(false, "error Username already exists");
      }
      userDao.insert(user);

      return new UserResult(user);
    } catch (DataAccessException e) {
      e.printStackTrace();
      return new UserResult(false, "error " + e.getMessage());
    }
  }

  public static UserResult login(LoginRequest r) throws DataAccessException {
    try {
      String username = r.getUsername();
      String password = r.getPassword();

      UserDao userDao = new UserDao();
      User user = userDao.getOne(username);

      if (user == null) {
        return new UserResult(false, "No user found by username");
      }

      if (user.getPassword().equals(password)) {
        return new UserResult(user);
      } else {
        return new UserResult(false, "Password incorrect");
      }
    } catch (Exception e) {
      e.printStackTrace();
      return new UserResult(false, "error " + e.getMessage());
    }
  }

}
