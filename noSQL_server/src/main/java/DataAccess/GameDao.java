package DataAccess;

import Model.Game;

import java.beans.Expression;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import Model.Player;
import Model.User;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

public class GameDao {

  private static final String TableName = "Games";

  private static final String IdAttr = "id";
  private static final String HostIdAttr = "hostId";
  private static final String LocationAttr = "location";
  private static final String StartTimeAttr = "startTime";
  private static final String InitialPlayerAttr = "initialPlayers";
  private static final String PlayersRemainingAttr = "playersRemaining";
  private static final String CodeAttr = "code";
  private static final String WinnerAttr = "winner";
  


  //DynamoDB client
  private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-east-2")
            .build();
  private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
  private static Table table = dynamoDB.getTable(TableName);


  public Game getOne(String gameId) throws DataAccessException {
    Item item = table.getItem(IdAttr, gameId);

    if (item != null) {
      String id = item.getString(IdAttr);
      String hostId = item.getString(HostIdAttr);
      String location = item.getString(LocationAttr);
      long startTime = item.getLong(StartTimeAttr);
      //String[] initialPlayersArray = new String[item.getList(InitialPlayerAttr).size()];
      //String[] playersRemainingArray = new String[item.getList(PlayersRemainingAttr).size()];
      List<String> initialPlayers = item.getList(InitialPlayerAttr);
      List<String> playersRemaining = item.getList(PlayersRemainingAttr);
      String code = item.getString(CodeAttr);
      String winner = item.getString(WinnerAttr);
    
      return new Game(id, hostId, location, startTime, initialPlayers.toArray(new String[initialPlayers.size()]), playersRemaining.toArray(new String[playersRemaining.size()]), code, winner);
    }
    return null;
  }

  public String getGameIdByCode(String code) throws DataAccessException {
    Item item = table.getItem(CodeAttr, code);
    if (item != null) {
      String gameId = item.getString(IdAttr);
      return gameId;
    }
    return null;
  }


  public void insert(Game game) throws DataAccessException {
    Item item = new Item()
            .withPrimaryKey(IdAttr, game.getId())
            .withString(HostIdAttr, game.getHostId())
            .withString(LocationAttr, game.getLocation())
            .withLong(StartTimeAttr, game.getStartTime())
            .withList(InitialPlayerAttr, game.getInitialPlayerCount())
            .withList(PlayersRemainingAttr, game.getPlayersRemainingCount())
            .withString(CodeAttr, game.getCode())
            .withString(WinnerAttr, game.getWinner());
    try {
      table.putItem(item);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to add game: " + game.getId());
    }
  }

  public void playerJoined(String gameId, String playerId) throws DataAccessException {
    HashMap<String, String> nameMap = new HashMap<String, String>();
    nameMap.put("#initialPlayers", InitialPlayerAttr);

    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
              .withPrimaryKey(IdAttr, gameId)
              .withUpdateExpression("set #initialPlayers = list_append(#initialPlayers, :player)")
              .withNameMap(nameMap)
              .withValueMap(new ValueMap().withList(":player", new String[] {playerId}))
              .withReturnValues(ReturnValue.UPDATED_NEW);
              
    try {
      UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to update initial player count");
      System.err.println(e.getMessage());
    }

    nameMap = new HashMap<String, String>();
    nameMap.put("#playersRemaining", PlayersRemainingAttr);

    updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey(IdAttr, gameId)
            .withUpdateExpression("set #playersRemaining = list_append(#playersRemaining, :player)")
            .withNameMap(nameMap)
            .withValueMap(new ValueMap().withList(":player", new String[] {playerId}))
            .withReturnValues(ReturnValue.UPDATED_NEW);

    try {
      UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to update initial player count");
      System.err.println(e.getMessage());
    }
  }

  public void playerLeftBeforeStart(String gameId, String playerId) throws DataAccessException {
    String updateExp = "delete " + InitialPlayerAttr + " :player";

    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
              .withPrimaryKey(IdAttr, gameId)
              .withUpdateExpression(updateExp)
              .withValueMap(new ValueMap().withString(":player", playerId))
              .withReturnValues(ReturnValue.UPDATED_NEW);
    
    try {
      UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to update initial player count");
    }
  }

  public void playerLeftAfterStart(String gameId, String playerId) throws DataAccessException {
    playerKilled(gameId, playerId);
  }

  public void setHostId(String gameId, String hostId) throws DataAccessException {
    String updateExp = "set " + HostIdAttr + " = :val";
    
    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
              .withPrimaryKey(IdAttr, gameId)
              .withUpdateExpression(updateExp)
              .withValueMap(new ValueMap().withString(":val", hostId))
              .withReturnValues(ReturnValue.UPDATED_NEW);

    try {
      UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to update host id");
    }
  }

  public String[] getAllPlayersInGame(String gameId) throws DataAccessException {
    Game game = getOne(gameId);
    return game.getInitialPlayerCount();
  }

  public void startGame(String gameId, String[] players) throws DataAccessException {
    String updateExp = "set " + StartTimeAttr + " = :startTime, "
              + PlayersRemainingAttr + " = :playersRemaining";
    
    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
              .withPrimaryKey(IdAttr, gameId)
              .withUpdateExpression(updateExp)
              .withValueMap(new ValueMap()
                .withNumber(":startTime", 1)
                .withList(":playersRemaining", players)
              )
              .withReturnValues(ReturnValue.UPDATED_NEW);
    try {
      UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to start game");
    }
  }

  public String[] getPlayersRemaining(String gameId) throws DataAccessException {
    Item item = table.getItem(IdAttr, gameId);
    
    if (item != null) {
      List<String> array = item.getList(PlayersRemainingAttr);
      return array.toArray(new String[array.size()]);
    }
    return new String[0];
  }

  public void setWinner(String gameId, String userId) throws DataAccessException {
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

  public String getWinner(String gameId) throws DataAccessException {
    Item item = table.getItem(IdAttr, gameId);
    
    if (item != null) {
      return item.getString(WinnerAttr);
    }
    return null;
  }

  public void playerKilled(String gameId, String playerId) throws DataAccessException {
    ArrayList<String> array = new ArrayList<>(Arrays.asList(getOne(gameId).getPlayersRemainingCount()));
    array.remove(playerId);

    HashMap<String, String> nameMap = new HashMap<String, String>();
    nameMap.put("#players", PlayersRemainingAttr);

    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey(IdAttr, gameId)
            .withUpdateExpression("set #players = :player")
            .withNameMap(nameMap)
            .withValueMap(new ValueMap().withList(":player", array.toArray(new String[array.size()])))
            .withReturnValues(ReturnValue.UPDATED_NEW);

    try {
      UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to update initial player count");
      System.err.println(e.getMessage());
    }
  }

  public void delete(String gameId) throws DataAccessException {
    DeleteItemSpec deleteItemSpec = new DeleteItemSpec() 
              .withPrimaryKey(IdAttr, gameId);

    try {
      table.deleteItem(deleteItemSpec);
    } catch (Exception e) {
      System.err.println("GameDAO: Unable to delete game");
    }
   }
}
