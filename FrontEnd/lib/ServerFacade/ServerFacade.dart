
import 'package:flutter_app/Request/GameRequest.dart';
import 'package:flutter_app/Request/JoinGameRequest.dart';
import 'package:flutter_app/Request/LoginRequest.dart';
import 'package:flutter_app/Request/RegisterRequest.dart';
import 'package:flutter_app/Result/GameResult.dart';
import 'package:flutter_app/Result/PlayerResult.dart';
import 'package:flutter_app/Result/PlayersResult.dart';
import 'package:flutter_app/Result/UserResult.dart';

class ServerFacade {
  String serverHost;
  String serverPort;

  ServerFacade() {
    this.serverHost = "192.168.1.15";
    this.serverPort = "3000";
  }

  Future<UserResult> login(LoginRequest r) {

    return null;
  }

  Future<UserResult> register(RegisterRequest r) {

    return null;
  }

  Future<UserResult> getUser(String username) {

    return null;
  }

  Future<PlayerResult> getPlayer(int playerId) {

    return null;
  }

  Future<PlayersResult> getPlayers(String userId) {

    return null;
  }

  Future<PlayerResult> joinGame(JoinGameRequest r) {

    return null;
  }

  Future<PlayerResult> leaveGame(int playerId) {

    return null;
  }

  Future<GameResult> startGame(int gameId) {

    return null;
  }

  Future<PlayerResult> killTarget(int playerId) {

    return null;
  }

  Future<PlayerResult> createGame(GameRequest r) {

    return null;
  }

  Future<GameResult> getGame(int gameId) {

    return null;
  }
}