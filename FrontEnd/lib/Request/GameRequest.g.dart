// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'GameRequest.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

GameRequest _$GameRequestFromJson(Map<String, dynamic> json) {
  return GameRequest(
    json['username'] as String,
    json['location'] as String,
  );
}

Map<String, dynamic> _$GameRequestToJson(GameRequest instance) =>
    <String, dynamic>{
      'username': instance.username,
      'location': instance.location,
    };
