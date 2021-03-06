import 'package:flutter/material.dart';
import 'package:flutter_app/Result/GameResult.dart';
import 'package:flutter_app/Result/PlayerResult.dart';
import 'package:flutter_app/UserDashboard.dart';
import 'LoadingPage.dart';
import 'Model/User.dart';
import 'Model/Game.dart';
import 'Model/Player.dart';
import 'ServerFacade/ServerFacade.dart';

class GameDashboard extends StatefulWidget {
  GameDashboard({Key key, this.currPlayer, this.currUser}) : super(key: key);
  final Player currPlayer;
  final User currUser;
  @override
  _GameDashboardState createState() => _GameDashboardState();
}

class _GameDashboardState extends State<GameDashboard> {
  Widget _body = LoadingPage.loading();
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
    setState(() => _body = gameDashboardPage());
  }

  void _killTarget() async {
    PlayerResult result = await new ServerFacade().killTarget(widget.currPlayer.id);

    if (!result.success) {
      Scaffold.of(context).showSnackBar(SnackBar(
        content: Text(result.message),
      ));
      return;
    }
    final route = MaterialPageRoute(
      builder: (context) =>
          GameDashboard(currPlayer: widget.currPlayer, currUser: widget.currUser),
    );
    Navigator.push(context, route);
  }

  void _startGame() async {
    GameResult result = await new ServerFacade().startGame(currGame.id);

    if (!result.success) {
      Scaffold.of(context).showSnackBar(SnackBar(
        content: Text(result.message),
      ));
      return;
    }
    final route = MaterialPageRoute(
      builder: (context) =>
          GameDashboard(currPlayer: widget.currPlayer, currUser: widget.currUser),
    );
    Navigator.push(context, route);
  }

  _home() {
    final route = MaterialPageRoute(
      builder: (context) =>
          UserDashboard(currentUser: widget.currUser),
    );
    Navigator.push(context, route);
  }


  @override
  Widget build(BuildContext context) {
    return _body;
  }

  Scaffold gameDashboardPage() {
    return Scaffold(
        appBar: AppBar(
          title: Text('Game Dashboard'),
          actions: [
            IconButton(icon: Icon(Icons.home), onPressed: () => _home())
          ],
        ),
        body: Padding(
            padding: EdgeInsets.all(10),
            child: ListView(
              children: <Widget>[
                if (currGame.startTime == 0 && currGame.hostId == widget.currPlayer.id) ...[
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
                else if (currGame.startTime == 0) ...[
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
                else if (currGame.winner != "") ...[
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
                        'Players Remaining: ${currGame.playersRemaining}',
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