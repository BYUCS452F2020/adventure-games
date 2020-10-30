import 'package:flutter_app/Model/Player.dart';
import 'package:flutter_app/Result/Result.dart';
import 'package:json_annotation/json_annotation.dart';

part 'PlayersResult.g.dart';

@JsonSerializable(explicitToJson: true)

class PlayersResult extends Result {
  List<Player> players;

  PlayersResult(bool success, String message) : super(success, message);

  PlayersResult.players(this.players) : super(true, "success");

  factory PlayersResult.fromJson(Map<String, dynamic> json) => _$PlayersResultFromJson(json);

  Map<String, dynamic> toJson() => _$PlayersResultToJson(this);
}