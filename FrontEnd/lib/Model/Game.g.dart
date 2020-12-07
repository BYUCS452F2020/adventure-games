// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'Game.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Game _$GameFromJson(Map<String, dynamic> json) {
  return Game(
    json['id'] as String,
    json['hostId'] as String,
    json['location'] as String,
    json['startTime'] as int,
    json['initialPlayers'] as List,
    json['playersRemaining'] as List,
    json['code'] as String,
    json['winner'] as String,
  );
}

Map<String, dynamic> _$GameToJson(Game instance) => <String, dynamic>{
      'id': instance.id,
      'hostId': instance.hostId,
      'location': instance.location,
      'startTime': instance.startTime,
      'initialPlayerCount': instance.initialPlayers,
      'playersRemainingCount': instance.playersRemaining,
      'code': instance.code,
      'winner': instance.winner,
    };
