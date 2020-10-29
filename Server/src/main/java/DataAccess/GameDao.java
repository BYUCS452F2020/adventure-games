package DataAccess;

import Model.Game;

import java.sql.*;

public class GameDao {
  private final Connection conn;

  public GameDao(Connection conn) {
    this.conn = conn;
  }

  public Game getOne(int gameId) throws DataAccessException {
    Game game;
    ResultSet rs = null;
    String sql = "SELECT * FROM games WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, gameId);
      rs = stmt.executeQuery();
      if (rs.next()) {
        game = new Game(rs.getInt("id"), rs.getInt("hostId"), rs.getString("location"), rs.getDate("startTime").getTime(),
                rs.getInt("initialPlayerCount"), rs.getInt("playersRemainingCount"), rs.getString("code"), rs.getString("winner"));
        return game;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding game: " + e.getMessage());
    } finally {
      if(rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public Game getGameByCode(String code) throws DataAccessException {
    Game game;
    ResultSet rs = null;
    String sql = "SELECT id FROM games WHERE code = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, code);
      rs = stmt.executeQuery();
      if (rs.next()) {
        game = new Game(rs.getInt("id"), rs.getInt("hostId"), rs.getString("location"), rs.getDate("startTime").getTime(),
                rs.getInt("initialPlayerCount"), rs.getInt("playersRemainingCount"), rs.getString("code"), rs.getString("winner"));
        return game;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding game by code: " + e.getMessage());
    } finally {
      if(rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public boolean checkCodeAvailability(String code) throws DataAccessException {
    ResultSet rs = null;
    String sql = "SELECT id FROM games WHERE code = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, code);
      rs = stmt.executeQuery();
      if (rs.next()) {
        return false;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while checking code availability: " + e.getMessage());
    } finally {
      if(rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return true;
  }

  public void insert(Game game) throws DataAccessException {
    String sql = "INSERT INTO games (hostId, location, startTime, initialPlayerCount, playersRemainingCount, code, winner) VALUES (?,?,?,?,?,?,?);";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, game.getHostId());
      stmt.setString(2, game.getLocation());
      stmt.setLong(3, game.getStartTime());
      stmt.setInt(4, game.getInitialPlayerCount());
      stmt.setInt(5, game.getPlayersRemainingCount());
      stmt.setString(6, game.getCode());
      stmt.setString(7, game.getWinner());

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while inserting game into the database: " + e.getMessage());
    }
  }

  public void playerJoined(int gameId) throws DataAccessException {
    String sql = "UPDATE games SET initialPlayerCount = initialPlayerCount + 1, playersRemainingCount = playersRemainingCount + 1 WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, gameId);

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while updating game in the database: " + e.getMessage());
    }
  }

  public void playerLeftBeforeStart(int gameId) throws DataAccessException {
    String sql = "UPDATE games SET initialPlayerCount = initialPlayerCount - 1, playersRemainingCount = playersRemainingCount - 1 WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, gameId);

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while updating game in the database: " + e.getMessage());
    }
  }

  public void playerLeftAfterStart(int gameId) throws DataAccessException {
    playerKilled(gameId);
  }

  public int getNextId() throws DataAccessException {
    ResultSet rs = null;
    String sql = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'adventure_games' AND TABLE_NAME = 'games';";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("AUTO_INCREMENT");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding next game id: " + e.getMessage());
    } finally {
      if(rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return -1;
  }

  public void startGame(int gameId, int numPlayers) throws DataAccessException {
    String sql = "UPDATE games SET startTime = ?, initialPlayerCount = ?, playersRemainingCount = ? WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setLong(1, new java.util.Date().getTime());
      stmt.setInt(2, numPlayers);
      stmt.setInt(3, numPlayers);
      stmt.setInt(4, gameId);

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while updating player in the database: " + e.getMessage());
    }
  }

  public void setWinner(int gameId, String userId) throws DataAccessException {
    String sql = "UPDATE games SET winner = ? WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, gameId);
      stmt.setString(2, userId);

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while updating game winner in the database: " + e.getMessage());
    }
  }

  public String getWinner(int gameId) throws DataAccessException {
    Game game;
    ResultSet rs = null;
    String sql = "SELECT userId FROM players WHERE gameId = ? AND status = true;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, gameId);
      rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getString("userId");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding game winner: " + e.getMessage());
    } finally {
      if(rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public void playerKilled(int gameId) throws DataAccessException {
    String sql = "UPDATE games SET playersRemainingCount = playersRemainingCount - 1 WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, gameId);

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while updating game in the database: " + e.getMessage());
    }
  }

  public void delete(int gameId) throws DataAccessException {
    String sql = "DELETE FROM games WHERE id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, gameId);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new DataAccessException("Error encountered while deleting game from database: " + e.getMessage());
    }
  }
}
