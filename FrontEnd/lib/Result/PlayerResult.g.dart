// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'PlayerResult.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

PlayerResult _$PlayerResultFromJson(Map<String, dynamic> json) {
  return PlayerResult(
    json['success'] as bool,
    json['message'] as String,
  )..player = json['player'] == null
      ? null
      : Player.fromJson(json['player'] as Map<String, dynamic>);
}

Map<String, dynamic> _$PlayerResultToJson(PlayerResult instance) =>
    <String, dynamic>{
      'success': instance.success,
      'message': instance.message,
      'player': instance.player?.toJson(),
    };
