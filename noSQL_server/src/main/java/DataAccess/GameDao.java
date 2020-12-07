package DataAccess;

import Model.Game;

import java.beans.Expression;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.User;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

public class GameDao {

  private static final String TableName = "Games";

  private static final String IdAttr = "id";
  private static final String HostIdAttr = "hostId";
  private static final String LocationAttr = "location";
  private static final String StartTimeAttr = "startTime";
  private static final String InitialPlayerAttr = "initialPlayerCount";
  private static final String PlayersRemainingAttr = "playersRemainingCount";
  private static final String CodeAttr = "code";
  private static final String WinnerAttr = "winner";
  


  //DynamoDB client
  private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-east-2")
            .build();
  private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
  private static Table table = dynamoDB.getTable(TableName);


  public Game getOne(int gameId) throws DataAccessException {
    Item item = table.getItem(IdAttr, gameId);

    if (item != null) {
      int id = item.getInt(IdAttr);
      int hostId = item.getInt(HostIdAttr);
      String location = item.getString(LocationAttr);
      long startTime = item.getLong(StartTimeAttr);
      int initialPlayerCount = item.getInt(InitialPlayerAttr);
      int playersRemainingCount = item.getInt(PlayersRemainingAttr);
      String code = item.getString(CodeAttr);
      String winner = item.getString(WinnerAttr);
    
      return new Game(id, hostId, location, startTime, initialPlayerCount, playersRemainingCount, code, winner);
    }
    return null;
  }

  public int getGameIdByCode(String code) throws DataAccessException {
    Item item = table.getItem(CodeAttr, code);
    if (item != null) {
      int gameId = item.getInt(IdAttr);
      return gameId;
    }
    return 0;
  }

  public boolean checkCodeAvailability(String code) throws DataAccessException {
    int id = getGameIdByCode(code);

    if (id == 0) {
      return true;
    }
    return false;
  }

  public void insert(Game game) throws DataAccessException {
    Item item = new Item()
            .withPrimaryKey(IdAttr, game.getId())
            .withInt(HostIdAttr, game.getHostId())
            .withString(LocationAttr, game.getLocation())
            .withLong(StartTimeAttr, game.getStartTime())
            .withInt(InitialPlayerAttr, game.getInitialPlayerCount())
            .withInt(PlayersRemainingAttr, game.getPlayersRemainingCount())
            .withString(CodeAttr, game.getCode())
            .withString(WinnerAttr, game.getWinner());
    try {
      table.putItem(item);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to add game: " + game.getId());
    }
  }

  public void playerJoined(int gameId) throws DataAccessException {
    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
              .withPrimaryKey(IdAttr, gameId)
              .withUpdateExpression("set info.initialPlayerCount = info.initialPlayerCount + :val")
              .withValueMap(new ValueMap().withNumber(":val", 1))
              .withReturnValues(ReturnValue.UPDATED_NEW);
              
    try {
      UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to update initial player count");
    }
  }

  public void playerLeftBeforeStart(int gameId) throws DataAccessException {
    String updateExp = "set " + InitialPlayerAttr + " = " + InitialPlayerAttr + " + :val";

    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
              .withPrimaryKey(IdAttr, gameId)
              .withUpdateExpression(updateExp)
              .withValueMap(new ValueMap().withNumber(":val", 1))
              .withReturnValues(ReturnValue.UPDATED_NEW);
    
    try {
      UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to update initial player count");
    }
  }

  public void playerLeftAfterStart(int gameId) throws DataAccessException {
    playerKilled(gameId);
  }

  public int setupGame(Game game) throws DataAccessException {
    insert(game);
    return getGameIdByCode(game.getCode());
  }

  public void setHostId(int gameId, int hostId) throws DataAccessException {
    String updateExp = "set " + HostIdAttr + " = :val";
    
    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
              .withPrimaryKey(IdAttr, gameId)
              .withUpdateExpression(updateExp)
              .withValueMap(new ValueMap().withNumber(":val", hostId))
              .withReturnValues(ReturnValue.UPDATED_NEW);

    try {
      UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to update host id");
    }
  }

  public void startGame(int gameId, int numPlayers) throws DataAccessException {
    String updateExp = "set " + StartTimeAttr + " = :startTime, "
              + InitialPlayerAttr + " = :initialPlayers, "
              + PlayersRemainingAttr + " = :playersRemaining";
    
    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
              .withPrimaryKey(IdAttr, gameId)
              .withUpdateExpression(updateExp)
              .withValueMap(new ValueMap()
                .withNumber(":startTime", 1)
                .withNumber(":initialPlayers", numPlayers)
                .withNumber(":playersRemaining", numPlayers)
              )
              .withReturnValues(ReturnValue.UPDATED_NEW);
    try {
      UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to start game");
    }
  }

  public int getPlayersRemaining(int gameId) throws DataAccessException {
    Item item = table.getItem(IdAttr, gameId);
    
    if (item != null) {
      return item.getInt(PlayersRemainingAttr);
    }
    return -1;
  }

  public void setWinner(int gameId, String userId) throws DataAccessException {
    String updateExp = "set " + WinnerAttr + " = :val";
    
    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
              .withPrimaryKey(IdAttr, gameId)
              .withUpdateExpression(updateExp)
              .withValueMap(new ValueMap().withString(":val", userId))
              .withReturnValues(ReturnValue.UPDATED_NEW);

    try {
      UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to update winner");
    }
  }

  public String getWinner(int gameId) throws DataAccessException {
    Item item = table.getItem(IdAttr, gameId);
    
    if (item != null) {
      return item.getString(WinnerAttr);
    }
    return null;
  }

  public void playerKilled(int gameId) throws DataAccessException {
    String updateExp = "set " + PlayersRemainingAttr + " = " + PlayersRemainingAttr + " - :val";
    
    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
              .withPrimaryKey(IdAttr, gameId)
              .withUpdateExpression(updateExp)
              .withValueMap(new ValueMap().withNumber(":val", 1))
              .withReturnValues(ReturnValue.UPDATED_NEW);

    try {
      UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to kill player");
    }
  }

  public void delete(int gameId) throws DataAccessException {
    DeleteItemSpec deleteItemSpec = new DeleteItemSpec() 
              .withPrimaryKey(IdAttr, gameId);

    try {
      table.deleteItem(deleteItemSpec);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to delete game");
    }
   }
}
