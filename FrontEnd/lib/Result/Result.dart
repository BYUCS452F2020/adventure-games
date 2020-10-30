import 'package:json_annotation/json_annotation.dart';

part 'Result.g.dart';

@JsonSerializable()

class Result {
  bool success;
  String message;

  Result(this.success, this.message);

  factory Result.fromJson(Map<String, dynamic> json) => _$ResultFromJson(json);

  Map<String, dynamic> toJson() => _$ResultToJson(this);
}