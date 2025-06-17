package net.ubn.td.exceptions;

public class ApiReturn<T> {
    private ResponseCode code;
    private String message;
    private T data;

    public ResponseCode getCode() { return code; }
    public void setCode(ResponseCode code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
