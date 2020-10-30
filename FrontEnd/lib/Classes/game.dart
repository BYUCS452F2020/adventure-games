
class game {

  int gameId;
  int hostId;
  String location;
  String startTime;
  int initialPlayerCount;
  int playersRemainingCount;
  String code;
  String winner;

  void setGame(Map input){
    gameId = input["gameId"];
    hostId = input["hostId"];
    location = input["location"];
    startTime = input["startTime"];
    initialPlayerCount = input["initialPlayerCount"];
    playersRemainingCount = input["playersRemainingCount"];
    code = input["code"];
    winner = input["winner"];
  }
}
