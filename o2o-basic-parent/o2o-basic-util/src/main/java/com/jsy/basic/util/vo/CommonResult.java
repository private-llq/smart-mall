package com.jsy.basic.util.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsy.basic.util.exception.JSYError;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> implements Serializable {

    @ApiModelProperty("接口返回状态码，0表示成功")
    private int code;

    @ApiModelProperty("接口错误返回提示信息")
    private String message;

    @ApiModelProperty("具体数据体")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private T data;

    public static <T> CommonResult<T> ok(T data) {

        return new CommonResult<>(0, null, data);
    }

    public static CommonResult<Boolean> ok() {
        return new CommonResult<>(0, "操作成功", true);
    }

    public static CommonResult<Boolean> error(int code, String message) {
        return new CommonResult<>(code, message, false);
    }

    public static CommonResult<Boolean> error(JSYError error) {
        return error(error.getCode(), error.getMessage());
    }

    public static CommonResult<Boolean> error(int code) {
        return error(code, null);
    }


    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CommonResult)) return false;
        final CommonResult<?> other = (CommonResult<?>) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getCode() != other.getCode()) return false;
        final Object this$message = this.getMessage();
        final Object other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) return false;
        final Object this$data = this.getData();
        final Object other$data = other.getData();
        if (this$data == null ? other$data != null : !this$data.equals(other$data)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CommonResult;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getCode();
        final Object $message = this.getMessage();
        result = result * PRIME + ($message == null ? 43 : $message.hashCode());
        final Object $data = this.getData();
        result = result * PRIME + ($data == null ? 43 : $data.hashCode());
        return result;
    }


}
