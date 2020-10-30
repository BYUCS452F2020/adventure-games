import 'dart:convert';

import 'package:flutter_app/Request/GameRequest.dart';
import 'package:flutter_app/Request/JoinGameRequest.dart';
import 'package:flutter_app/Request/LoginRequest.dart';
import 'package:flutter_app/Request/RegisterRequest.dart';
import 'package:flutter_app/Result/GameResult.dart';
import 'package:flutter_app/Result/PlayerResult.dart';
import 'package:flutter_app/Result/PlayersResult.dart';
import 'package:flutter_app/Result/UserResult.dart';
import 'package:http/http.dart' as http;

class ServerFacade {
  String serverHost;
  String serverPort;

  ServerFacade() {
    this.serverHost = "192.168.1.15";
    this.serverPort = "3000";
  }

  Future<UserResult> login(LoginRequest r) async {
    final http.Response response = await http.post(
      "http://" + serverHost + ":" + serverPort + "/user/login",
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, LoginRequest>{
        'loginRequest': r,
      }),
    );

    if (response.statusCode == 201) {
      //Return a UserResult object
      return UserResult.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to login');
    }
  }

  Future<UserResult> register(RegisterRequest r) async {
    final http.Response response = await http.post(
      "http://" + serverHost + ":" + serverPort + "/user/register",
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, RegisterRequest>{
        'registerRequest': r,
      }),
    );

    if (response.statusCode == 201) {
      return UserResult.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to login');
    }
  }

  Future<UserResult> getUser(String username) async {
    final response = await http
        .get("http://" + serverHost + ":" + "serverPort" + "/user/" + username);

    if (response.statusCode == 200) {
      return UserResult.fromJson(jsonDecode(response.body));
    } else {
      throw Exception("Failed to load user");
    }
  }

  Future<PlayerResult> getPlayer(int playerId) async {
    final response = await http.get("http://" +
        serverHost +
        ":" +
        "serverPort" +
        "/player/" +
        playerId.toString());

    if (response.statusCode == 200) {
      return PlayerResult.fromJson(jsonDecode(response.body));
    } else {
      throw Exception("Failed to load player");
    }
  }

  Future<PlayersResult> getPlayers(String userId) async {
    final response = await http.get(
        "http://" + serverHost + ":" + "serverPort" + "/players/" + userId);

    if (response.statusCode == 200) {
      return PlayersResult.fromJson(jsonDecode(response.body));
    } else {
      throw Exception("Failed to load players");
    }
  }

  Future<PlayerResult> joinGame(JoinGameRequest r) async {
    final http.Response response = await http.post(
      "http://" + serverHost + ":" + serverPort + "/join_game",
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, JoinGameRequest>{
        'joinGameRequest': r,
      }),
    );

    if (response.statusCode == 201) {
      return PlayerResult.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to join game');
    }
  }

  Future<PlayerResult> leaveGame(int playerId) async {
    final response = await http.get("http://" +
        serverHost +
        ":" +
        "serverPort" +
        "/leave_game/" +
        playerId.toString());

    if (response.statusCode == 200) {
      return PlayerResult.fromJson(jsonDecode(response.body));
    } else {
      throw Exception("Failed to leave game");
    }
  }

  Future<GameResult> startGame(int gameId) async {
    final response = await http.get("http://" +
        serverHost +
        ":" +
        "serverPort" +
        "/start_game/" +
        gameId.toString());

    if (response.statusCode == 200) {
      return GameResult.fromJson(jsonDecode(response.body));
    } else {
      throw Exception("Failed to start game");
    }
  }

  Future<PlayerResult> killTarget(int playerId) async {
    final response = await http.get("http://" +
        serverHost +
        ":" +
        "serverPort" +
        "/kill_target/" +
        playerId.toString());

    if (response.statusCode == 200) {
      return PlayerResult.fromJson(jsonDecode(response.body));
    } else {
      throw Exception("Failed to kill target. You suck");
    }
  }

  Future<PlayerResult> createGame(GameRequest r) async {
    final http.Response response = await http.post(
      "http://" + serverHost + ":" + serverPort + "/game",
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, GameRequest>{
        'gameRequest': r,
      }),
    );

    if (response.statusCode == 201) {
      return PlayerResult.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to create game');
    }
  }

  Future<GameResult> getGame(int gameId) async {
    final response = await http.get("http://" +
        serverHost +
        ":" +
        "serverPort" +
        "/game/" +
        gameId.toString());

    if (response.statusCode == 200) {
      return GameResult.fromJson(jsonDecode(response.body));
    } else {
      throw Exception("Failed to load game");
    }
  }
}
