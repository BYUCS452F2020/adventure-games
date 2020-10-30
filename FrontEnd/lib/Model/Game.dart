import 'package:json_annotation/json_annotation.dart';

part 'Game.g.dart';

@JsonSerializable()

class Game {

  int gameId;
  int hostId;
  String location;
  String startTime;
  int initialPlayerCount;
  int playersRemainingCount;
  String code;
  String winner;

  Game(this.gameId, this.hostId, this.location, this.startTime,
      this.initialPlayerCount, this.playersRemainingCount, this.code,
      this.winner);

  factory Game.fromJson(Map<String, dynamic> json) => _$GameFromJson(json);

  Map<String, dynamic> toJson() => _$GameToJson(this);
}
