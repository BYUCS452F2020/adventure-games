package Service;

import DataAccess.*;
import Handler.JsonHandler;
import Model.Game;
import Model.Player;
import Request.JoinGameRequest;
import Result.GameResult;
import Result.PlayerResult;
import Result.PlayersResult;


import java.sql.Connection;

public class PlayerService {

  /**
   * Returns player by playerId
   */
  public static PlayerResult getPlayer(int playerId) throws DataAccessException {
    Database db = new Database();
    try {
      Connection conn = db.openConnection();
      db.createTables();
      db.createDefaultUser();

      PlayerDao playerDao = new PlayerDao(conn);
      Player player = playerDao.getOne(playerId);

      if (player == null) {
        db.closeConnection(false);
        return new PlayerResult(false, "no player data associated with id");
      }

      db.closeConnection(true);

      return new PlayerResult(player);
    } catch (Exception e) {
      e.printStackTrace();
      db.closeConnection(false);
      return new PlayerResult(false, "error " + e.getMessage());
    }
  }

  /**
   * Returns all players associated with username
   */
  public static PlayersResult getPlayers(String username) throws Exception {
    Database db = new Database();
    try {
      Connection conn = db.openConnection();
      db.createTables();

      PlayerDao playerDao = new PlayerDao(conn);
      Player[] players = playerDao.getAll(username);

      db.closeConnection(true);

      return new PlayersResult(players);
    } catch (DataAccessException e) {
      e.printStackTrace();
      db.closeConnection(false);
      return new PlayersResult(false, "error: " + e.getMessage());
    }
  }

  /**
   * Gets gameId from game code
   * Generates player for that game and inserts it into the database
   */
  public static PlayerResult joinGame(JoinGameRequest r) throws Exception {
    Database db = new Database();
    try {
      Connection conn = db.openConnection();
      db.createTables();

      PlayerDao playerDao = new PlayerDao(conn);
      GameDao gameDao = new GameDao(conn);

      String userId = r.getUserId();
      String code = r.getCode();

      Game game = gameDao.getGameByCode(code);

      Player player = new Player(userId, game.getId());
      playerDao.insert(player);
      player = playerDao.getOne(userId, game.getId());

      db.closeConnection(true);

      return new PlayerResult(player);
    } catch (DataAccessException e) {
      e.printStackTrace();
      db.closeConnection(false);
      return new PlayerResult(false, "error " + e.getMessage());
    }
  }

  /**
   * If player is last one in the the game, deletes game and player
   * If game hasn't started yet, deletes player before starting game
   * If players are still in the game and player is alive, reassigns target and deletes player, adjusts game players remaining
   * If players are still in the game but player is dead, just deletes player
   */
  public static PlayerResult leaveGame(int playerId) throws Exception {
    Database db = new Database();
    try {
      Connection conn = db.openConnection();
      db.createTables();

      PlayerDao playerDao = new PlayerDao(conn);
      GameDao gameDao = new GameDao(conn);
      Player player = playerDao.getOne(playerId);
      int gameId = player.getGameId();

      if (playerDao.getPlayersRemaining(gameId) == 1) {
        gameDao.delete(gameId);
      } else if (player.getStatus()) {
        Game game = gameDao.getOne(gameId);
        if (game.getStartTime() == 0) {
          gameDao.playerLeftBeforeStart(gameId);
        } else {
          Player assassin = playerDao.getAssassin(player.getUserId(), gameId);
          playerDao.updateTarget(assassin.getPlayerId(), player.getTargetId());
          gameDao.playerLeftAfterStart(gameId);
        }
      }

      playerDao.delete(playerId);
      db.closeConnection(true);

      return new PlayerResult(true, "Player left game");
    } catch (DataAccessException e) {
      e.printStackTrace();
      db.closeConnection(false);
      return new PlayerResult(false, "error " + e.getMessage());
    }
  }

  public static PlayerResult killTarget(int playerId) throws Exception {
    Database db = new Database();
    try {
      Connection conn = db.openConnection();
      db.createTables();

      PlayerDao playerDao = new PlayerDao(conn);
      UserDao userDao = new UserDao(conn);
      GameDao gameDao = new GameDao(conn);

      Player player = playerDao.getOne(playerId);
      String userId = player.getUserId();
      int gameId = player.getGameId();
      Player target = playerDao.getTarget(player.getTargetId(), gameId);

      //sets target status to dead (false) and nullifies their targetId
      playerDao.killPlayer(target.getPlayerId());
      //updates player and user kill count
      playerDao.updateKills(playerId);
      userDao.updateKills(userId);
      //subtracts 1 from the game's players remaining
      gameDao.playerKilled(gameId);

      if (playerDao.getPlayersRemaining(gameId) == 1) {
        userDao.updateWin(userId);
        gameDao.setWinner(gameId, userId);
        return new PlayerResult(true, "Killing player successful, game over");
      }

      //sets player's new target as target's old target
      String targetUsername = target.getTargetId();
      target = playerDao.getTarget(targetUsername, gameId);
      boolean stillInGame = target.getStatus();
      //checks if new target is still alive, if not, assigns next target
      while (!stillInGame) {
        targetUsername = target.getTargetId();
        target = playerDao.getTarget(targetUsername, gameId);
        stillInGame = target.getStatus();
      }
      playerDao.updateTarget(playerId, target.getUserId());


      db.closeConnection(true);

      return new PlayerResult(true, "Killing player successful");
    } catch (DataAccessException e) {
      e.printStackTrace();
      db.closeConnection(false);
      return new PlayerResult(false, "error " + e.getMessage());
    }
  }
}
