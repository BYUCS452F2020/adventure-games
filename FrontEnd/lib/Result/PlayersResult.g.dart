// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'PlayersResult.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

PlayersResult _$PlayersResultFromJson(Map<String, dynamic> json) {
  return PlayersResult(
    json['success'] as bool,
    json['message'] as String,
  )..players = (json['players'] as List)
      ?.map(
          (e) => e == null ? null : Player.fromJson(e as Map<String, dynamic>))
      ?.toList();
}

Map<String, dynamic> _$PlayersResultToJson(PlayersResult instance) =>
    <String, dynamic>{
      'success': instance.success,
      'message': instance.message,
      'players': instance.players?.map((e) => e?.toJson())?.toList(),
    };
