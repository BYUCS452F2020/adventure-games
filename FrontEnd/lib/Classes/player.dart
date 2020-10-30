
class player {

  String playerId;
  String userId;
  String targetId;
  int gameId;
  int kills;
  bool status;

  void setPlayer(Map input){
    playerId = input["playerId"];
    userId = input["userId"];
    targetId = input["targetId"];
    gameId = input["gameId"];
    kills = input["kills"];
    status = input["status"];
  }
}
