package Result;

import Model.Player;

public class PlayersResult extends Result {
  private Player[] data;

  public PlayersResult(boolean success, String message) {
    super(success, message);
  }

  public PlayersResult(Player[] data) {
    super(true, "successful");
    this.data = data;
  }

  public Player[] getData() {
    return data;
  }
}
