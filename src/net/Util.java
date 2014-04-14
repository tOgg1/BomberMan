package net;

/**
 * Created by tormod on 14.04.14.
 */
public class Util {

    public static int START_CLIENT_TRANSFER         = 0x01;
    public static int START_SERVER_TRANSFER         = 0x02;
    public static int START_CLIENT_UPDATE_MOVEABLE  = 0x03;
    public static int START_CLIENT_DROP_BOMB        = 0x04;
    public static int START_SERVER_ADD_MOVEABLE     = 0x05;

    public static int END_CLIENT_TRANSFER           = 0x10;
    public static int END_SERVER_TRANSFER           = 0x20;
    public static int END_CLIENT_UPDATE_MOVEABLE    = 0x30;
    public static int END_CLIENT_DROP_BOMB          = 0x40;
    public static int END_SERVER_ADD_MOVEABLE       = 0x50;

    public static int ERROR_INTERNAL_ERROR          = 0xFD;
    public static int ERROR_MALFORMATTED_MESSAGE    = 0xFE;
    public static int CONTENT_LENGTH                = 0xFF;

}
