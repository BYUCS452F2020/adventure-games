// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'JoinGameRequest.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

JoinGameRequest _$JoinGameRequestFromJson(Map<String, dynamic> json) {
  return JoinGameRequest(
    json['userId'] as String,
    json['code'] as String,
  );
}

Map<String, dynamic> _$JoinGameRequestToJson(JoinGameRequest instance) =>
    <String, dynamic>{
      'userId': instance.userId,
      'code': instance.code,
    };
