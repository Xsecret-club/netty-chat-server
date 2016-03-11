namespace com.xsecret.chat

enum eType {
    PING = 0;
    LOGIN = 1;
    LOGOUT = 2;
    SEND = 3;
    ERROR = 4
}

struct MsgContent {
    1: eType msgType,
    2: string clientId,
    3: string userName,
    4: string userPassword,
    5: string toClientId,
    6: string toClientMsg,
    7: string errorMsg
}