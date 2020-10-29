package Handler;

import Request.JoinGameRequest;
import Result.GameResult;
import Service.PlayerService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class JoinGameHandler extends RequestHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String message;
    GameResult result = null;
    try {
      if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
        InputStream reqBody = exchange.getRequestBody();
        String reqData = readString(reqBody);
        JoinGameRequest request = JsonHandler.deserialize(reqData, JoinGameRequest.class);
        result = PlayerService.joinGame(request);
      } else {
        message = "{ \"message\": \"error: bad request\" }";
        result = new GameResult(false, message);
      }
    } catch (Exception e) {
      message = "{ \"message\": \"error: " + e.getMessage() + "\" }";
      result = new GameResult(false, message);
      e.printStackTrace();
    } finally {
      message = JsonHandler.serialize(result, GameResult.class);
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
      OutputStream respBody = exchange.getResponseBody();
      writeString(message, respBody);
      respBody.close();
    }
  }
}
