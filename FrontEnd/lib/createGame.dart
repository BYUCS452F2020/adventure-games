import 'package:flutter/material.dart';
import 'package:flutter_app/gameDashboard.dart';
import 'main.dart';
import 'Classes/user.dart';
import 'Classes/game.dart';

class createGame extends StatefulWidget {
  createGame({Key key, this.currUser}) : super(key: key);
  final user currUser;
  @override
  _createGameState createState() => _createGameState();
}

class _createGameState extends State<createGame> {
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



  void _create() {
    print(locationController.text);

    GameRequest request = new GameRequest(widget.currUser.userId, locationController.text);
    PlayerResult result = ServerFacade().createGame(request);

    if (!result.isSuccess()) {
      Scaffold.of(context).showSnackBar(SnackBar(
        content: Text(result.getMessage),
      ));
      return;
    }
    game currGame = result.getPlayer();
    final route = MaterialPageRoute(
      builder: (context) =>
          gameDashboard(currPlayer: currPlayer),
    );
    Navigator.push(context, route);
  }
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