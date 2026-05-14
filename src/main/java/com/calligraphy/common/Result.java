package com.calligraphy.common;
//result泛型T，可以方便使用其他类类型数据，也可以不使用，T可用可不用
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    public Result() {
    }
//新建对象时，构造函数将传进来的参数赋给对象的三个成员变量
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
//static，可以用类来调用方法success，第一个泛型T表示占位符，告诉编译器这个方法要用T，第二个是返回值里有T类型数据，
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }
//return 直接返回新建的Result对象，code和message是指定的数据，data是形参里传进来的数据
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}