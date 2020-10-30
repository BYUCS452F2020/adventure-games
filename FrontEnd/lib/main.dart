import 'package:flutter/material.dart';
import 'Request/LoginRequest.dart';
import 'Result/UserResult.dart';
import 'ServerFacade/ServerFacade.dart';
import 'SignIn.dart';
import 'Model/User.dart';
import 'UserDashboard.dart';

void main() {
  runApp(MaterialApp(
    home: login(),
  ));
}

class login extends StatefulWidget {
  @override
  _loginState createState() => _loginState();
}

class _loginState extends State<login> {
  TextEditingController nameController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
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

  void _login() async {
    print(nameController.text);
    print(passwordController.text);

    LoginRequest request = new LoginRequest(nameController.text, passwordController.text);
    UserResult result = await new ServerFacade().login(request);

    if (!result.success) {
      Scaffold.of(context).showSnackBar(SnackBar(
        content: Text(result.message),
      ));
      return;
    }
    User currentUser = result.user;
    final route = MaterialPageRoute(
      builder: (context) =>
          UserDashboard(currentUser: currentUser),
    );
    Navigator.push(context, route);
  }

  void checkButtonEnabled() {
    String text1,text2;

    text1 = nameController.text ;
    text2 = passwordController.text ;
    if(text1 == '' || text2 == '' )
    {
      print('Text Field is empty, Please Fill All Data');
    }else{
      setState(() => buttonEnabled = true);
      print('Not Empty, All Text Input is Filled.');
    }

  }

  void _signIn() {
    final route = MaterialPageRoute(
      builder: (context) =>
          SignIn(),
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
                      'Adventure Games',
                      style: TextStyle(
                          color: Colors.blue,
                          fontWeight: FontWeight.w500,
                          fontSize: 30),
                    )),
                Container(
                    alignment: Alignment.center,
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Login',
                      style: TextStyle(fontSize: 20),
                    )),
                Container(
                  padding: EdgeInsets.all(10),
                  child: TextField(
                    controller: nameController,
                    onChanged: (val) {
                      checkButtonEnabled();
                    },
                    decoration: InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'User Name',
                    ),
                  ),
                ),
                Container(
                  padding: EdgeInsets.fromLTRB(10, 10, 10, 0),
                  child: TextField(
                    obscureText: true,
                    controller: passwordController,
                    onChanged: (val) {
                      checkButtonEnabled();
                    },
                    decoration: InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Password',
                    ),
                  ),
                ),
                Container(
                    height: 50,
                    padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                    child: RaisedButton(
                      textColor: Colors.white,
                      color: Colors.blue,
                      child: Text('Login'),
                      onPressed: checkButtonVar() ? () => _login() : null,
                    )),
                Container(
                    child: Row(
                      children: <Widget>[
                        Text('Do not have an account?'),
                        FlatButton(
                          textColor: Colors.blue,
                          child: Text(
                            'Sign in',
                            style: TextStyle(fontSize: 20),
                          ),
                          onPressed: () {
                            _signIn();
                            //signup screen
                          },
                        )
                      ],
                      mainAxisAlignment: MainAxisAlignment.center,
                    ))
              ],
            )));
  }
}