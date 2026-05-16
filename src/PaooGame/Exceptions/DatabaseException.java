package PaooGame.Exceptions;

import PaooGame.Managers.DatabaseManager;

public class DatabaseException extends RuntimeException{
    public DatabaseException(String msg) {
        super(msg);
    }

    public DatabaseException(String msg, Throwable cause){
        super(msg,cause);
    }
}
