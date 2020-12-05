package DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
  private Connection conn;

  public Connection openConnection() throws Exception {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");

      final String CONNECTION_URL = "jdbc:mysql://localhost/adventure_games?"
              + "user=adventure_games_user&password=password";
      conn = DriverManager.getConnection(CONNECTION_URL);
      conn.setAutoCommit(false);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Unable to open connection to database");
    }
    return conn;
  }

  public Connection getConnection() throws Exception {
    if (conn == null) {
      return openConnection();
    } else {
      return conn;
    }
  }

  public void closeConnection(boolean commit) throws DataAccessException {
    try {
      if (commit) {
        conn.commit();
      } else {
        conn.rollback();
      }
      conn.close();
      conn = null;
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Unable to close database connection");
    }
  }

  public void createTables() throws DataAccessException {
    //createExperienceTable();
    createUsersTable();
    createGamesTable();
    createPlayersTables();
  }

  private void createUsersTable() throws DataAccessException {
    try (Statement stmt = conn.createStatement()) {
      String sql = "Create Table if not exists users (" +
              "  username varchar (255) not null primary key," +
              "  password varchar (255) not null," +
              "  firstName varchar (255) not null," +
              "  lastName varchar (255) not null," +
//              "  xpId int not null," +
              "  wins int not null," +
              "  kills int not null" +
//              "  foreign key(xpID) references experience(id)" +
              ");";

      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new DataAccessException("SQL Error encountered while creating users table: " + e.getMessage());
    }
  }

  private void createGamesTable() throws DataAccessException {
    try (Statement stmt = conn.createStatement()) {
      String sql = "Create Table if not exists games (" +
              "  id int not null primary key auto_increment," +
              "  hostId int," +
              "  location varchar (255) not null," +
              "  startTime int," +
              "  initialPlayerCount int not null," +
              "  playersRemainingCount int not null," +
              "  code varchar (255) not null," +
              "  winner varchar (255)" +
              ");";

      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new DataAccessException("SQL Error encountered while creating games table: " + e.getMessage());
    }
  }

  private void createPlayersTables() throws DataAccessException {
    try (Statement stmt = conn.createStatement()) {
      String sql = "Create Table if not exists players (" +
              "  id int not null primary key auto_increment," +
              "  userId varchar (255) not null," +
              "  gameId int not null," +
              "  kills int not null," +
              "  targetId varchar (255)," +
              "  status boolean not null" +
//              "  foreign key(userId) references users(username)," +
//              "  foreign key(gameId) references games(id)," +
//              "  foreign key(targetId) references users(username)" +
              ");";

      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new DataAccessException("SQL Error encountered while creating players table: " + e.getMessage());
    }
  }

  public void createDefaultUser() throws DataAccessException {
    try (Statement stmt = conn.createStatement()) {
      String sql = "INSERT IGNORE INTO users (username, password, firstName, lastName, kills, wins) VALUES ('TestUser1', 'password', 'Test', 'User', 0, 0);";
      stmt.executeUpdate(sql);

    } catch (SQLException e) {
      throw new DataAccessException("SQL Error encountered while creating default user: " + e.getMessage());
    }
  }

  public void clearTables() throws DataAccessException {
    try (Statement stmt = conn.createStatement()) {
      String sql = "DELETE FROM users; DELETE FROM players; DELETE FROM games; DELETE FROM experience";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      throw new DataAccessException("SQL Error encountered while clearing tables");
    }
  }

}
