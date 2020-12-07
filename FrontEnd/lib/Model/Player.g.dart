// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'Player.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Player _$PlayerFromJson(Map<String, dynamic> json) {
  return Player(
    json['id'] as String,
    json['userId'] as String,
    json['targetId'] as String,
    json['gameId'] as String,
    json['kills'] as int,
    json['gameStatus'] as bool,
  );
}

Map<String, dynamic> _$PlayerToJson(Player instance) => <String, dynamic>{
      'id': instance.id,
      'userId': instance.userId,
      'targetId': instance.targetId,
      'gameId': instance.gameId,
      'kills': instance.kills,
      'gameStatus': instance.gameStatus,
    };
