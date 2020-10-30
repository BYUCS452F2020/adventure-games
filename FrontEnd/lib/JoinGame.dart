import 'package:flutter/material.dart';
import 'package:flutter_app/Request/JoinGameRequest.dart';
import 'package:flutter_app/Result/PlayerResult.dart';
import 'package:flutter_app/ServerFacade/ServerFacade.dart';
import 'GameDashboard.dart';
import 'Model/Player.dart';
import 'main.dart';
import 'Model/User.dart';
import 'Model/Game.dart';

class JoinGame extends StatefulWidget {
  JoinGame({Key key, this.currUser}) : super(key: key);
  final User currUser;
  @override
  _JoinGameState createState() => _JoinGameState();
}

class _JoinGameState extends State<JoinGame> {
  TextEditingController codeController = TextEditingController();
  bool buttonEnabled;

  @override
  void initState(){
    super.initState();
    init();
  }

  void init() async {
    buttonEnabled = false;
  }

  bool checkButtonVar () {
    return buttonEnabled;
  }


  void checkButtonEnabled() {
    String text1;

    text1 = codeController.text ;
    if(text1 == '')
    {
      print('Text Field is empty, Please Fill All Data');
    }else{
      setState(() => buttonEnabled = true);
      print('Not Empty, All Text Input is Filled.');
    }

  }



  void _join() async {
    print(codeController.text);

    JoinGameRequest request = new JoinGameRequest(widget.currUser.username, codeController.text);
    PlayerResult result = await new ServerFacade().joinGame(request);

    if (!result.success) {
      Scaffold.of(context).showSnackBar(SnackBar(
        content: Text(result.message),
      ));
      return;
    }
    Player currPlayer = result.player;
    final route = MaterialPageRoute(
      builder: (context) =>
          GameDashboard(currPlayer: currPlayer, currUser: widget.currUser),
    );
    Navigator.push(context, route);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('Adventure Games'),
        ),
        body: Padding(
            padding: EdgeInsets.all(10),
            child: ListView(
              children: <Widget>[
                Container(
                    alignment: Alignment.center,
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Join Game',
                      style: TextStyle(
                          color: Colors.blue,
                          fontWeight: FontWeight.w500,
                          fontSize: 30),
                    )),
                Container(
                  padding: EdgeInsets.all(10),
                  child: TextField(
                    controller: codeController,
                    onChanged: (val) {
                      checkButtonEnabled();
                    },
                    decoration: InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Code',
                    ),
                  ),
                ),
                Container(
                    height: 50,
                    padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                    child: RaisedButton(
                      textColor: Colors.white,
                      color: Colors.blue,
                      child: Text('Join'),
                      onPressed: checkButtonVar() ? () => _join() : null,
                    )),
              ],
            )));
  }
}