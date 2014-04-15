package net;

/**
 * Created by tormod on 14.04.14.
 */
public class Util {

    public static int START_CLIENT_TRANSFER         = 0xFF01;
    public static int START_SERVER_TRANSFER         = 0xFF02;
    public static int START_UPDATE_MOVEABLE         = 0xFF03;
    public static int START_DROP_BOMB               = 0xFF04;
    public static int START_SERVER_ADD_MOVEABLE     = 0xFF05;
    public static int START_SERVER_ADD_POWERUP      = 0xFF06;

    public static int END_CLIENT_TRANSFER           = 0xFF10;
    public static int END_SERVER_TRANSFER           = 0xFF20;
    public static int END_UPDATE_MOVEABLE           = 0xFF30;
    public static int END_DROP_BOMB                 = 0xFF40;
    public static int END_SERVER_ADD_MOVEABLE       = 0xFF50;

    public static int ERROR_INTERNAL_ERROR          = 0xFFFD;
    public static int ERROR_MALFORMATTED_MESSAGE    = 0xFFFE;
    public static int CONTENT_LENGTH                = 0xFFFF;

}
