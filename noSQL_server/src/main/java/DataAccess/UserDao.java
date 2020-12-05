package DataAccess;

import Model.User;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import java.util.ArrayList;

public class UserDao {

  private static final String TableName = "Users";

  private static final String UserNameAttr = "username";
  private static final String PasswordAttr = "password";
  private static final String FirstNameAttr = "firstName";
  private static final String LastNameAttr = "lastName";
  private static final String WinsAttr = "wins";
  private static final String KillsAttr = "kills";
  private static final String GamesAttr = "games";

  // DynamoDB client
  private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
          .standard()
          .withRegion("us-east-2")
          .build();
  private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
  private static Table table = dynamoDB.getTable(TableName);


  public User getOne(String username) throws DataAccessException {
    Item item = table.getItem(UserNameAttr, username);

    if(item != null) {
      String userName = item.getString(UserNameAttr);
      String password = item.getString(PasswordAttr);
      String firstName = item.getString(FirstNameAttr);
      String lastName = item.getString(LastNameAttr);
      int wins = item.getInt(WinsAttr);
      int kills = item.getInt(KillsAttr);
      ArrayList<Integer> games = new ArrayList<>();
      for(Object game : item.getList(GamesAttr)) {
        games.add((Integer) game);
      }

      return new User(userName, password, firstName, lastName, wins, kills, games);
    }
    return null;
  }

  public void insert(User user) throws DataAccessException {
    Table table = dynamoDB.getTable(TableName);
    String username = user.getUsername();
    String password = user.getPassword();
    String firstName = user.getFirstName();
    String lastName = user.getLastName();
    int wins = user.getWins();
    int kills = user.getKills();
    ArrayList<Integer> games = user.getGames();

    Item item = new Item()
            .withPrimaryKey(UserNameAttr, username)
            .withString(PasswordAttr, password)
            .withString(FirstNameAttr, firstName)
            .withString(LastNameAttr, lastName)
            .withInt(WinsAttr, wins)
            .withInt(KillsAttr, kills)
            .withList(GamesAttr, games);

    try {
      table.putItem(item);
    }
    catch (Exception e) {
      System.err.println("Unable to add user: " + username);
    }
  }

  public void updateWin(String username) throws DataAccessException {
    Item item = table.getItem(UserNameAttr, username);

    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey(UserNameAttr, username)
            .withUpdateExpression("set " + WinsAttr + " = :wins")
            .withValueMap(new ValueMap()
            .withInt(":wins", item.getInt(WinsAttr) + 1));

    table.updateItem(updateItemSpec);
  }

  public void updateKills(String username) throws DataAccessException {
    Item item = table.getItem(UserNameAttr, username);

    UpdateItemSpec updateItemSpec = new UpdateItemSpec()
            .withPrimaryKey(UserNameAttr, username)
            .withUpdateExpression("set " + KillsAttr + " = :kills")
            .withValueMap(new ValueMap()
                    .withInt(":kills", item.getInt(KillsAttr) + 1));

    table.updateItem(updateItemSpec);
  }
}
