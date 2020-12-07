package Handler;

import Result.PlayerResult;
import Service.PlayerService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class LeaveGameHandler extends RequestHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String message;
    PlayerResult result = null;
    try {
      if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
        String urlPath = exchange.getRequestURI().toString();
        String playerId = urlPath.substring(urlPath.lastIndexOf('/') + 1);
        result = PlayerService.leaveGame(playerId);
      } else {
        message = "{ \"message\": \"error: bad request\" }";
        result = new PlayerResult(false, message);
      }
    } catch (Exception e) {
      message = "{ \"message\": \"error: " + e.getMessage() + "\" }";
      result = new PlayerResult(false, message);
      e.printStackTrace();
    } finally {
      message = JsonHandler.serialize(result, PlayerResult.class);
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
      OutputStream respBody = exchange.getResponseBody();
      writeString(message, respBody);
      respBody.close();
    }
  }
}
