package DataAccess;

import Model.Player;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import org.w3c.dom.Attr;

import javax.management.AttributeValueExp;
import java.util.*;

public class PlayerDao {

  private static final String TableName = "Players";

  private static final String IndexName = "userId-id-index";

  private static final String IdAttr = "id";
  private static final String UserIdAttr = "userId";
  private static final String GameAttr = "gameId";
  private static final String KillsAttr = "kills";
  private static final String TargetIdAttr = "targetId";
  private static final String StatusAttr = "gameStatus";

  // DynamoDB client
  private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
          .standard()
          .withRegion("us-east-2")
          .build();
  private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
  private static Table table = dynamoDB.getTable(TableName);

  public Player getOne(String id) throws DataAccessException {
    Item item = table.getItem(IdAttr, id, UserIdAttr, id.substring(0, id.indexOf("_")));

    if(item != null) {
      String iD = item.getString(IdAttr);
      String userId = item.getString(UserIdAttr);
      String gameId = item.getString(GameAttr);
      int kills = item.getInt(KillsAttr);
      String targetId = item.getString(TargetIdAttr);
      Boolean status = item.getBoolean(StatusAttr);

      return new Player(iD, userId, gameId, kills, targetId, status);
    }
    return null;
  }

  public Player getOne(String userId, String gameId) throws DataAccessException {
        Item item = table.getItem(IdAttr, userId + "_" + gameId, UserIdAttr, userId);

        if(item != null) {
          String id = item.getString(IdAttr);
          String user_Id = item.getString(UserIdAttr);
          String game_Id = item.getString(GameAttr);
          int kills = item.getInt(KillsAttr);
          String targetId = item.getString(TargetIdAttr);
          Boolean status = item.getBoolean(StatusAttr);
    
          return new Player(id, user_Id, game_Id, kills, targetId, status);
        }
        return null;
  }

  public Player[] getAll(String username) throws DataAccessException {
    ArrayList<Player> playerArrayList = new ArrayList<>();
    HashMap<String, String> nameMap = new HashMap<String, String>();
    nameMap.put("#uId", UserIdAttr);

    HashMap<String, AttributeValue> valueMap = new HashMap<String, AttributeValue>();
    valueMap.put(":uId", new AttributeValue().withS(username));

    QueryRequest queryRequest = new QueryRequest()
            .withTableName(TableName)
            .withIndexName(IndexName)
            .withKeyConditionExpression("#uId = :uId")
            .withExpressionAttributeNames(nameMap)
            .withExpressionAttributeValues(valueMap);

    QueryResult queryResult = amazonDynamoDB.query(queryRequest);
    List<Map<String, AttributeValue>> items = queryResult.getItems();
    if (items != null) {
      for (Map<String, AttributeValue> item : items){
        Player player = new Player(item.get(IdAttr).getS(), item.get(UserIdAttr).getS(), item.get(GameAttr).getS(), Integer.parseInt(item.get(KillsAttr).getN()), item.get(TargetIdAttr).getS(), item.get(StatusAttr).getBOOL());
        playerArrayList.add(player);
      }
    }

    return playerArrayList.toArray(new Player[playerArrayList.size()]);
  }

  public void insert(Player player) throws DataAccessException {
    String id = player.getPlayerId();
    String userId = player.getUserId();
    String gameId = player.getGameId();
    int kills = player.getKills();
    String targetId = player.getTargetId();
    Boolean status = player.getStatus();

    Item item = new Item()
            .withPrimaryKey(IdAttr, id)
            .withString(UserIdAttr, userId)
            .withString(GameAttr, gameId)
            .withInt(KillsAttr, kills)
            .withString(TargetIdAttr, targetId)
            .withBoolean(StatusAttr, status);

    try {
      table.putItem(item);
    }
    catch (Exception e) {
      System.err.println("Unable to add Player: " + id);
    }
  }

  public int getNextId() throws DataAccessException {
    Random rd = new Random();
    return rd.nextInt();
  }

  public void killPlayer(String playerId) throws DataAccessException {
    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey(IdAttr, playerId, UserIdAttr, playerId.substring(0, playerId.indexOf("_")))
            .withUpdateExpression("set " + StatusAttr + " = :status")
            .withValueMap(new ValueMap()
            .withBoolean(":status", false));

    table.updateItem(updateItemSpec);
  }

  public void updateKills(String playerId) throws DataAccessException {
    Item item = table.getItem(IdAttr, playerId, UserIdAttr, playerId.substring(0, playerId.indexOf("_")));

    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey(IdAttr, playerId, UserIdAttr, playerId.substring(0, playerId.indexOf("_")))
            .withUpdateExpression("set " + KillsAttr + " = :kills")
            .withValueMap(new ValueMap()
                    .withInt(":kills", item.getInt(KillsAttr) + 1));

    table.updateItem(updateItemSpec);
  }

  public void updateTarget(String playerId, String newTarget) throws DataAccessException {
    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey(IdAttr, playerId, UserIdAttr, playerId.substring(0, playerId.indexOf("_")))
            .withUpdateExpression("set " + TargetIdAttr + " = :target")
            .withValueMap(new ValueMap()
                    .withString(":target", newTarget));

    table.updateItem(updateItemSpec);
  }

  public Player getAssassin(String targetId, String gameId) throws DataAccessException {
    Item item = table.getItem(TargetIdAttr, targetId, GameAttr, gameId);

    if(item != null) {
      String id = item.getString(IdAttr);
      String userId = item.getString(UserIdAttr);
      String game_Id = item.getString(GameAttr);
      int kills = item.getInt(KillsAttr);
      String target_Id = item.getString(TargetIdAttr);
      Boolean status = item.getBoolean(StatusAttr);

      return new Player(id, userId, game_Id, kills, target_Id, status);
    }
    return null;
  }

  public void delete(String playerId) throws DataAccessException {
    try {

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey(IdAttr, playerId, UserIdAttr, playerId.substring(0, playerId.indexOf("_")));

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
