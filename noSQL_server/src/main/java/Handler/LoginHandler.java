package Handler;

import Request.LoginRequest;
import Result.UserResult;
import Service.UserService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class LoginHandler extends RequestHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String message;
    UserResult result = null;
    try {
      if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
        InputStream reqBody = exchange.getRequestBody();
        String reqData = readString(reqBody);
        LoginRequest loginRequest = JsonHandler.deserialize(reqData, LoginRequest.class);
        result = UserService.login(loginRequest);
      } else {
        message = "{ \"message\": \"error: bad request\" }";
        result = new UserResult(false, message);
      }
    } catch (Exception e) {
      message = "{ \"message\": \"error: " + e.getMessage() + "\" }";
      result = new UserResult(false, message);
      e.printStackTrace();
    } finally {
      message = JsonHandler.serialize(result, UserResult.class);
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
      OutputStream respBody = exchange.getResponseBody();
      writeString(message, respBody);
      respBody.close();
    }
  }
}
