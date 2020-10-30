package Result;

public class Result {

  private String message;
  private boolean success;

  public Result(boolean success, String message) {
    this.success = success;
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null)
      return false;
    if (o instanceof Result) {
      Result result = (Result) o;
      return result.getMessage().equals(getMessage()) &&
              result.isSuccess() == (isSuccess());
    } else {
      return false;
    }
  }
}
