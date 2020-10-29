package Result;

import Model.User;

public class UserResult extends Result {
  private User user;

  public UserResult(boolean success, String message) {
    super(success, message);
  }

  public UserResult(User user) {
    super(true, "success");
    this.user = user;
  }

  public User getUser() {
    return user;
  }
}
