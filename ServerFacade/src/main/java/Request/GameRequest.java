package Request;

public class GameRequest {
  private String username;
  private String location;

  public GameRequest(String username, String location) {
    this.username = username;
    this.location = location;
  }

  public String getUsername() {
    return username;
  }

  public String getLocation() {
    return location;
  }
}
