// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'GameResult.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

GameResult _$GameResultFromJson(Map<String, dynamic> json) {
  return GameResult(
    json['success'] as bool,
    json['message'] as String,
  )..game = json['game'] == null
      ? null
      : Game.fromJson(json['game'] as Map<String, dynamic>);
}

Map<String, dynamic> _$GameResultToJson(GameResult instance) =>
    <String, dynamic>{
      'success': instance.success,
      'message': instance.message,
      'game': instance.game?.toJson(),
    };
