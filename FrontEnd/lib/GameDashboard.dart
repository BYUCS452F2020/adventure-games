import 'package:flutter/material.dart';
import 'package:flutter_app/Result/GameResult.dart';
import 'package:flutter_app/Result/PlayerResult.dart';
import 'Model/User.dart';
import 'Model/Game.dart';
import 'Model/Player.dart';
import 'ServerFacade/ServerFacade.dart';

class GameDashboard extends StatefulWidget {
  GameDashboard({Key key, this.currPlayer}) : super(key: key);
  final Player currPlayer;
  @override
  _GameDashboardState createState() => _GameDashboardState();
}

class _GameDashboardState extends State<GameDashboard> {
  Game currGame;

  @override
  void initState(){
    super.initState();
    init();
  }

  void init() async {
    GameResult result = await new ServerFacade().getGame(widget.currPlayer.gameId);

    if (!result.success) {
      Scaffold.of(context).showSnackBar(SnackBar(
        content: Text(result.message),
      ));
      return;
    }
    currGame = result.game;
  }

  void _killTarget() async {
    PlayerResult result = await new ServerFacade().killTarget(currGame.gameId);

    if (!result.success) {
      Scaffold.of(context).showSnackBar(SnackBar(
        content: Text(result.message),
      ));
      return;
    }
    final route = MaterialPageRoute(
      builder: (context) =>
          GameDashboard(currPlayer: widget.currPlayer),
    );
    Navigator.push(context, route);
  }

  void _startGame() async {
    GameResult result = await new ServerFacade().startGame(currGame.gameId);

    if (!result.success) {
      Scaffold.of(context).showSnackBar(SnackBar(
        content: Text(result.message),
      ));
      return;
    }
    final route = MaterialPageRoute(
      builder: (context) =>
          GameDashboard(currPlayer: widget.currPlayer),
    );
    Navigator.push(context, route);
  }



  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('Game Dashboard'),
        ),
        body: Padding(
            padding: EdgeInsets.all(10),
            child: ListView(
              children: <Widget>[
                if (currGame.startTime == null && currGame.hostId == widget.currPlayer.playerId) ...[
                Container(
                    alignment: Alignment.center,
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Game Code: ${currGame.code}',
                      style: TextStyle(fontSize: 20),
                    )),
                Container(
                    alignment: Alignment.center,
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Game Not Yet Started',
                      style: TextStyle(fontSize: 20),
                    )),
                  Container(
                      height: 50,
                      padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                      child: RaisedButton(
                        textColor: Colors.white,
                        color: Colors.blue,
                        child: Text('Start Game'),
                        onPressed: () {
                          _startGame();
                        },
                      )),
                ]
                else if (currGame.startTime == null) ...[
                  Container(
                      alignment: Alignment.center,
                      padding: EdgeInsets.all(10),
                      child: Text(
                        'Game Code: ${currGame.code}',
                        style: TextStyle(fontSize: 20),
                      )),
                  Container(
                      alignment: Alignment.center,
                      padding: EdgeInsets.all(10),
                      child: Text(
                        'Game Not Yet Started',
                        style: TextStyle(fontSize: 20),
                      )),
                ]
                else if (currGame.winner != null) ...[
                    Container(
                        alignment: Alignment.center,
                        padding: EdgeInsets.all(10),
                        child: Text(
                          'Game Code: ${currGame.code}',
                          style: TextStyle(fontSize: 20),
                        )),
                    Container(
                        alignment: Alignment.center,
                        padding: EdgeInsets.all(10),
                        child: Text(
                          'Game Over',
                          style: TextStyle(fontSize: 20),
                        )),
                    Container(
                        alignment: Alignment.center,
                        padding: EdgeInsets.all(10),
                        child: Text(
                          'Winner: ${currGame.winner}',
                          style: TextStyle(fontSize: 20),
                        )),
                  ]
                  else ...[
                  Container(
                      alignment: Alignment.center,
                      padding: EdgeInsets.all(10),
                      child: Text(
                        'Game Code: ${currGame.code}',
                        style: TextStyle(fontSize: 20),
                      )),
                  Container(
                      alignment: Alignment.center,
                      padding: EdgeInsets.all(10),
                      child: Text(
                        'Players Remaining: ${currGame.playersRemainingCount}',
                        style: TextStyle(fontSize: 20),
                      )),
                  Container(
                      alignment: Alignment.center,
                      padding: EdgeInsets.all(10),
                      child: Text(
                        'Target: ${widget.currPlayer.targetId}',
                        style: TextStyle(fontSize: 20),
                      )),
                  Container(
                      height: 50,
                      padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                      child: RaisedButton(
                        textColor: Colors.white,
                        color: Colors.blue,
                        child: Text('Kill Target'),
                        onPressed: () {
                          _killTarget();
                        },
                      )),

                ]
              ],
            )));
  }
}