// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'Player.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Player _$PlayerFromJson(Map<String, dynamic> json) {
  return Player(
    json['id'] as int,
    json['userId'] as String,
    json['targetId'] as String,
    json['gameId'] as int,
    json['kills'] as int,
    json['status'] as bool,
  );
}

Map<String, dynamic> _$PlayerToJson(Player instance) => <String, dynamic>{
      'id': instance.id,
      'userId': instance.userId,
      'targetId': instance.targetId,
      'gameId': instance.gameId,
      'kills': instance.kills,
      'status': instance.status,
    };
