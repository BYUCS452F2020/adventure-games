package Result;

import Model.Game;

public class GameResult extends Result {
  private Game game;

  public GameResult(boolean success, String message) {
    super(success, message);
  }

  public GameResult(Game game) {
    super(true, "success");
    this.game = game;
  }

  public Game getGame() {
    return game;
  }
}
