// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'Game.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

Game _$GameFromJson(Map<String, dynamic> json) {
  return Game(
    json['gameId'] as int,
    json['hostId'] as int,
    json['location'] as String,
    json['startTime'] as String,
    json['initialPlayerCount'] as int,
    json['playersRemainingCount'] as int,
    json['code'] as String,
    json['winner'] as String,
  );
}

Map<String, dynamic> _$GameToJson(Game instance) => <String, dynamic>{
      'gameId': instance.gameId,
      'hostId': instance.hostId,
      'location': instance.location,
      'startTime': instance.startTime,
      'initialPlayerCount': instance.initialPlayerCount,
      'playersRemainingCount': instance.playersRemainingCount,
      'code': instance.code,
      'winner': instance.winner,
    };