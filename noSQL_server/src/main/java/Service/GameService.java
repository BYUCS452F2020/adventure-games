package Service;

import DataAccess.DataAccessException;
import DataAccess.GameDao;
import DataAccess.PlayerDao;
import Model.Game;
import Model.Player;
import Request.GameRequest;
import Result.GameResult;
import Result.PlayerResult;

public class GameService {
  private static int gameCodeLength = 8;

  /**
   * Returns game by gameId
   */
  public static GameResult getGame(String gameId) throws DataAccessException {
    try {

      GameDao gameDao = new GameDao();
      Game game = gameDao.getOne(gameId);

      if (game == null) {
        return new GameResult(false, "no game data associated with id");
      }


      return new GameResult(game);
    } catch (Exception e) {
      e.printStackTrace();
      return new GameResult(false, "error " + e.getMessage());
    }
  }

  /**
   * Creates player for user creating game
   * Creates game based on location and start time provided
   * Game object generates random code for others to use to join
   * Creates player for user that created the game
   * Adds player that created it to game
   * Returns game object (including gameId and generated code)
   */
  public static PlayerResult setupGame(GameRequest r) throws Exception {
    try {

      GameDao gameDao = new GameDao();
      PlayerDao playerDao = new PlayerDao();

      String userId = r.getUsername();
      String location = r.getLocation();

      String code;
 
      code = getRandomCode();
      
      Game game = new Game(location, code, userId);
      gameDao.insert(game);
      Player player = new Player(userId, code);
      playerDao.insert(player);

      player = playerDao.getOne(userId, code);
      gameDao.setHostId(code, player.getPlayerId());

      return new PlayerResult(player);
    } catch (DataAccessException e) {
      e.printStackTrace();
      return new PlayerResult(false, "error " + e.getMessage());
    }
  }

  /**
   * Gets number of players that have joined the game since it's setup
   * Updates game with number of initial players and players remaining
   * Assigns each player a target
   */
  public static GameResult startGame(String gameId) throws Exception {
    try {
      GameDao gameDao = new GameDao();
      PlayerDao playerDao = new PlayerDao();

      String[] players = gameDao.getAllPlayersInGame(gameId);
      String player;
      String target;
      for (int i = 0; i < players.length - 1; i++) {
        player = players[i];
        target = players[i + 1];
        playerDao.updateTarget(player, target);
      }
      player = players[players.length - 1];
      target = players[0];
      playerDao.updateTarget(player, target);

      gameDao.startGame(gameId, players);


      return new GameResult(true, "Start game successful");
    } catch (DataAccessException e) {
      e.printStackTrace();
      return new GameResult(false, "error " + e.getMessage());
    }
  }

  private static String getRandomCode() {
    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz";

    StringBuilder sb = new StringBuilder(gameCodeLength);

    for (int i = 0; i < gameCodeLength; i++) {
      int index
              = (int)(AlphaNumericString.length()
              * Math.random());
      sb.append(AlphaNumericString
              .charAt(index));
    }

    return sb.toString();
  }
}
