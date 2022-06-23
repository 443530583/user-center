package com.app.usercenter.common;

/**
 * 返回工具类
 * @author admin
 */
public class ResultUtils {

    /**
     * 成功
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(200,data,BaseResponse.OK);
    }

    /**
     * 失败
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode){
        return new BaseResponse<T>(errorCode);
    }

    public static <T> BaseResponse<T> error(int errorCode,String message,String description){
        return new BaseResponse<T>(errorCode,null,message,description);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode,String message,String description){
        return new BaseResponse<T>(errorCode.getCode(),null,message,description);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode,String description){
        return new BaseResponse<T>(errorCode.getCode(),null,errorCode.getMessage(),description);
    }
}
