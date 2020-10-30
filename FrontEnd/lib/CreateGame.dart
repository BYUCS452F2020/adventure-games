import 'package:flutter/material.dart';
import 'package:flutter_app/GameDashboard.dart';
import 'UserDashboard.dart';
import 'main.dart';
import 'Model/Player.dart';
import 'Model/User.dart';
import 'Model/Game.dart';
import 'Request/GameRequest.dart';
import 'Result/PlayerResult.dart';
import 'ServerFacade/ServerFacade.dart';

class CreateGame extends StatefulWidget {
  CreateGame({Key key, this.currUser}) : super(key: key);
  final User currUser;
  @override
  _CreateGameState createState() => _CreateGameState();
}

class _CreateGameState extends State<CreateGame> {
  TextEditingController locationController = TextEditingController();
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

    text1 = locationController.text ;
    if(text1 == '')
    {
      print('Text Field is empty, Please Fill All Data');
    }else{
      setState(() => buttonEnabled = true);
      print('Not Empty, All Text Input is Filled.');
    }

  }



  void _create() async {
    print(locationController.text);

    GameRequest request = new GameRequest(widget.currUser.username, locationController.text);
    PlayerResult result = await new ServerFacade().createGame(request);

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

  _home() {
    final route = MaterialPageRoute(
      builder: (context) =>
          UserDashboard(currentUser: widget.currUser),
    );
    Navigator.push(context, route);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('Adventure Games'),
          actions: [
            IconButton(icon: Icon(Icons.home), onPressed: () => _home())
          ],
        ),
        body: Padding(
            padding: EdgeInsets.all(10),
            child: ListView(
              children: <Widget>[
                Container(
                    alignment: Alignment.center,
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Create Game',
                      style: TextStyle(
                          color: Colors.blue,
                          fontWeight: FontWeight.w500,
                          fontSize: 30),
                    )),
                Container(
                  padding: EdgeInsets.all(10),
                  child: TextField(
                    controller: locationController,
                    onChanged: (val) {
                      checkButtonEnabled();
                    },
                    decoration: InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Location',
                    ),
                  ),
                ),
                Container(
                    height: 50,
                    padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                    child: RaisedButton(
                      textColor: Colors.white,
                      color: Colors.blue,
                      child: Text('Create'),
                      onPressed: checkButtonVar() ? () => _create() : null,
                    )),
              ],
            )));
  }
}