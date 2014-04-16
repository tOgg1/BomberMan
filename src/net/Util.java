package net;

/**
 * Created by tormod on 14.04.14.
 */
public class Util {

    /*Game-messages*/
    public static int START_CLIENT_TRANSFER         = 0xF001;
    public static int START_SERVER_TRANSFER         = 0xF002;
    public static int START_UPDATE_MOVEABLE         = 0xF003;
    public static int START_DROP_BOMB               = 0xF004;
    public static int START_SERVER_ADD_MOVEABLE     = 0xF005;
    public static int START_SERVER_ADD_POWERUP      = 0xF006;

    public static int END_CLIENT_TRANSFER           = 0xF010;
    public static int END_SERVER_TRANSFER           = 0xF020;
    public static int END_UPDATE_MOVEABLE           = 0xF030;
    public static int END_DROP_BOMB                 = 0xF040;
    public static int END_SERVER_ADD_MOVEABLE       = 0xF050;

    public static int ERROR_INTERNAL_ERROR          = 0xFF01;
    public static int ERROR_MALFORMATTED_MESSAGE    = 0xFF02;
    public static int CONTENT_LENGTH                = 0xFF03;

    /*Pre-game messages*/
    public static int INITIAL_CONNECT           = 0x10001;
    public static int START_CLIENT_PREGAMEDATA  = 0x10002;
    public static int REQUEST_GAMES             = 0x10003;
    public static int START_CONNECT_TO_GAME     = 0x10004;
    public static int START_CREATE_GAME         = 0x10005;
    public static int START_UPDATE_GAME         = 0x10006;
    public static int START_SET_MAP             = 0x10007;
    public static int KICK_PLAYER               = 0x10008;

    public static int END_CLIENT_PREGAMEDATA    = 0x10F00;
    public static int END_CONNECT_TO_GAME       = 0x10F01;

}
