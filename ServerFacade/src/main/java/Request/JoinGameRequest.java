package Request;

public class JoinGameRequest {
  private String userId;
  private String code;

  public JoinGameRequest(String userId, String code) {
    this.userId = userId;
    this.code = code;
  }

  public String getUserId() {
    return userId;
  }

  public String getCode() {
    return code;
  }
}


