import 'package:flutter_app/Model/Game.dart';

import 'Result.dart';

class GameResult extends Result {
  Game game;

  GameResult(bool success, String message) : super(success, message);

  GameResult.game(this.game) : super(true, "success");
}