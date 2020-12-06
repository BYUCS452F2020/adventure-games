package DataAccess;

import Model.Player;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.Random;

public class PlayerDao {

  private static final String TableName = "Players";

  private static final String IdAttr = "id";
  private static final String UserIdAttr = "userId";
  private static final String GameAttr = "gameId";
  private static final String KillsAttr = "kills";
  private static final String TargetIdAttr = "targetId";
  private static final String StatusAttr = "status";

  // DynamoDB client
  private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
          .standard()
          .withRegion("us-east-2")
          .build();
  private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
  private static Table table = dynamoDB.getTable(TableName);

  public Player getOne(int id) throws DataAccessException {
    Item item = table.getItem(IdAttr, id);

    if(item != null) {
      id = item.getInt(IdAttr);
      String userId = item.getString(UserIdAttr);
      int gameId = item.getInt(GameAttr);
      int kills = item.getInt(KillsAttr);
      String targetId = item.getString(TargetIdAttr);
      Boolean status = item.getBoolean(StatusAttr);

      return new Player(id, userId, gameId, kills, targetId, status);
    }
    return null;
  }

  public Player getOne(String userId, int gameId) throws DataAccessException {

        Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
        expressionAttributeValues.put(":uId", userId);
        expressionAttributeValues.put(":gId", gameId);

        ItemCollection<ScanOutcome> items = table.scan("userId = :uId",
            "gameId = :gId",
            "id, userId, gameId kills, targetId, status",
            expressionAttributeValues);

        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
          int id = iterator.getInt(IdAttr);
          userId = iterator.getString(UserIdAttr);
          gameId = iterator.getInt(GameAttr);
          int kills = iterator.getInt(KillsAttr);
          String targetId = iterator.getString(TargetIdAttr);
          Boolean status = iterator.getBoolean(StatusAttr);
    
          return new Player(id, userId, gameId, kills, targetId, status);
        }
        return null;
  }

  public Player[] getAll(String username) throws DataAccessException {
    Player[] players;
    ArrayList<Player> playerArrayList = new ArrayList<>();
    Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
        expressionAttributeValues.put(":uId", username);

        ItemCollection<ScanOutcome> items = table.scan("userId = :uId",
            "id, userId, gameId, kills, targetId, status",
            expressionAttributeValues);

        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
          int id = iterator.getInt(IdAttr);
          username = iterator.getString(UserIdAttr);
          int gameId = iterator.getInt(GameAttr);
          int kills = iterator.getInt(KillsAttr);
          String targetId = iterator.getString(TargetIdAttr);
          Boolean status = iterator.getBoolean(StatusAttr);
    
          Player player = new Player(id, username, gameId, kills, targetId, status);
          playerArrayList.add(player);
        }
        players = playerArrayList.toArray(new Player[playerArrayList.size()]);
        return players;
  }

  public void insert(Player player) throws DataAccessException {
    int id = player.getPlayerId();
    String userId = player.getUserId();
    int gameId = player.getGameId();
    int kills = player.getKills();
    String targetId = player.getTargetId();
    Boolean status = player.getStatus();

    Item item = new Item()
            .withPrimaryKey(IdAttr, id)
            .withString(UserIdAttr, userId)
            .withString(GameAttr, gameId)
            .withString(KillsAttr, kills)
            .withInt(TargetIdAttr, targetId)
            .withInt(StatusAttr, status);

    try {
      table.putItem(item);
    }
    catch (Exception e) {
      System.err.println("Unable to add Player: " + id);
    }
  }

  public Player[] getAllPlayersInGame(int gameId) throws DataAccessException {
    Player[] players;
    ArrayList<Player> playerArrayList = new ArrayList<>();
    Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
        expressionAttributeValues.put(":gId", gameId);

        ItemCollection<ScanOutcome> items = table.scan("gameId = :gId",
            "id, userId, kills, gameId, targetId, status",
            expressionAttributeValues);

        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
          int id = iterator.getInt(IdAttr);
          String userId = iterator.getString(UserIdAttr);
          gameId = iterator.getInt(GameAttr);
          int kills = iterator.getInt(KillsAttr);
          String targetId = iterator.getString(TargetIdAttr);
          Boolean status = iterator.getBoolean(StatusAttr);
    
          Player player = new Player(id, userId, gameId, kills, targetId, status);
          playerArrayList.add(player);
        }
        players = playerArrayList.toArray(new Player[playerArrayList.size()]);
        return players;
  }

  public int getNextId() throws DataAccessException {
    Random rd = new Random();
    return rd.nextInt();
  }

  public void killPlayer(int playerId) throws DataAccessException {
    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey(IdAttr, playerId)
            .withUpdateExpression("set " + StatusAttr + " = :status set" + TargetIdAttr + "=:targetId")
            .withValueMap(new ValueMap()
            .withBoolean(":status", false).withString(":targetId", null));

    table.updateItem(updateItemSpec);
  }

  public void updateKills(int playerId) throws DataAccessException {
    Item item = table.getItem(IdAttr, playerId);

    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey(IdAttr, playerId)
            .withUpdateExpression("set " + KillsAttr + " = :kills")
            .withValueMap(new ValueMap()
                    .withInt(":kills", item.getInt(KillsAttr) + 1));

    table.updateItem(updateItemSpec);
  }


  public Player getTarget(String username, int gameId) throws DataAccessException {
    Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
        expressionAttributeValues.put(":uId", username);
        expressionAttributeValues.put(":gId", gameId);

        ItemCollection<ScanOutcome> items = table.scan("userId = :uId",
            "gameId = :gId",
            "id, userId, gameId kills, targetId, status",
            expressionAttributeValues);

        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
          int id = iterator.getInt(IdAttr);
          username = iterator.getString(UserIdAttr);
          gameId = iterator.getInt(GameAttr);
          int kills = iterator.getInt(KillsAttr);
          String targetId = iterator.getString(TargetIdAttr);
          Boolean status = iterator.getBoolean(StatusAttr);
    
          return new Player(id, username, gameId, kills, targetId, status);
        }
        return null;
  }

  public void updateTarget(int playerId, String newTarget) throws DataAccessException {
    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey(IdAttr, playerId)
            .withUpdateExpression("set " + TargetIdAttr + " = :target")
            .withValueMap(new ValueMap()
                    .withInt(":target", newTarget));

    table.updateItem(updateItemSpec);
  }

  public Player getAssassin(String targetId, int gameId) throws DataAccessException {
    Map<String, Object> expressionAttributeValues = new HashMap<String, Object>();
        expressionAttributeValues.put(":tId", targetId);
        expressionAttributeValues.put(":gId", gameId);

        ItemCollection<ScanOutcome> items = table.scan("targetId = :tId",
            "gameId = :gId",
            "id, userId, gameId kills, targetId, status",
            expressionAttributeValues);

        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
          int id = iterator.getInt(IdAttr);
          String userId = iterator.getString(UserIdAttr);
          gameId = iterator.getInt(GameAttr);
          int kills = iterator.getInt(KillsAttr);
          targetId = iterator.getString(TargetIdAttr);
          Boolean status = iterator.getBoolean(StatusAttr);
    
          return new Player(id, userId, gameId, kills, targetId, status);
        }
        return null;
  }

  public void delete(int playerId) throws DataAccessException {
    try {

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey("id", playerId);

        table.deleteItem(deleteItemSpec);

    }
    catch (Exception e) {
        System.err.println("Error deleting item in " + TableName);
        System.err.println(e.getMessage());
    }
  }

  public void removeAll() throws DataAccessException {
    ItemCollection<ScanOutcome> deleteoutcome = table.scan();
    Iterator<Item> iterator = deleteoutcome.iterator();

    while (iterator.hasNext()) {
      table.deleteItem("PrimaryKey", iterator.next().get("primary key value"));
    }
  }
}
