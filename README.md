# chat room
Computer Networking Course Project.

Based on Client-Server Architecture.

No External Library.

# Technologies

*Java: `java 11`*

*JavaFX: `javaFX 11`*

# Modules

* Client
* Server
* GUI
* Protocol
* Message Parser

# Usage

First Run `Server.src.com.amirmjrd.ServerRunner.java` , this class runs a server on `localhost` address and listening on `21000` port

then make a class on `chat_room/src` and run `Main` from Module `GUI`.

# Protocol

* Client to Server Messages :
  * `Public Message` :

     ```bash
    Public message, length=<CharCount>
    <Message>
    ```

  * `Private Message` :

    ```bash
    Private message, length=<CharCount> to <User1>,<User2>
    <Message>
    ```

  * `HandShake Message` :

    ```bash
    Hello<User>
    ```

  * `Logout Request` :

    ```bash
    Bye.
    ```

  * `Requesting Online Users` :

    ```bash
    Please send the list of attendees.
    ```

* Server to Client Messages:

  * `Public Message` :

    ```bash
        Public message, length=<CharCount> from <User3>
        <Message>
    ```

  * `Private Message` :

    ```bash
       Private message, length=<CharCount> from <User3> to <User1>,<User2>
       <Message>
     ```

  * `User logout` :

    ```bash
       User left the chat room.
    ```
  * `Online Users` :
     ```bash
        Here is the list of attendees :
        <username1>,<username2>,...
     ```
  * `Welcome` :

    ```bash
        Hi User, welcome to the chat room.
    ```

  * `User Join Public Message` :

    ```bash
        User join the chat room.

## Screenshots

![Sign Up](https://github.com/amirmojarad/chat_room/blob/main/screenshots/sign_up_page.png)

Sing Up Page

![Main Page](https://github.com/amirmojarad/chat_room/blob/main/screenshots/main_page.png)

Main Page


