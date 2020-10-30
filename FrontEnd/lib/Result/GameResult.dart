import 'package:flutter_app/Model/Game.dart';
import 'package:flutter_app/Result/Result.dart';
import 'package:json_annotation/json_annotation.dart';



part 'GameResult.g.dart';

@JsonSerializable(explicitToJson: true)

class GameResult extends Result {
  Game game;

  GameResult(bool success, String message) : super(success, message);

  GameResult.game(this.game) : super(true, "success");

  factory GameResult.fromJson(Map<String, dynamic> json) => _$GameResultFromJson(json);

  Map<String, dynamic> toJson() => _$GameResultToJson(this);
}