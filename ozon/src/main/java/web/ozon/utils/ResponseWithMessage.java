package web.ozon.utils;

public class ResponseWithMessage<T> {

    private String message;
    private T object;

    public ResponseWithMessage(String msg, T obj) {
        this.message = msg;
        this.object = obj;
    }
}
