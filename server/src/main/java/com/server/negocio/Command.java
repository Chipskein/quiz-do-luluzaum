package com.server.negocio;

public enum Command {
    //Server
    DISCONNECTED,
    SEND_YOUR_NICKNAME,
    CONNECTED,
    GAME_START,
    SHOW_QUESTION,
    SHOW_SCORE,
    GAME_END,
    ASK_PLAY_AGAIN,
    NEW_GAME,
    ERROR,

    //Client
    SENT_YOUR_NICKNAME,
    SENT_ANSWER,
    SENT_PLAY_AGAIN

}
