package com.jsy.basic.util.handler;
import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.vo.CommonResult;
import com.zhsj.basecommon.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.validation.ValidationException;
import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;

@ControllerAdvice
@Slf4j
public class ControllerHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ControllerHandler() {
    }


    //只要是JSYException异常都被被拦截，封装进CommonResult 返回给前端
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public CommonResult exceptionHandler(Exception e) {
        CommonResult model;
        if (e instanceof JSYException) {
            //强转异常，封装返回
            model =CommonResult.error(((JSYException) e).getCode(), e.getMessage());
        } else if(e instanceof DuplicateKeyException) {
            model = CommonResult.error(-1,"数据不能重复输入，检查是否启用");
            this.logger.error("数据不能重复插入",e);
        } else if (!(e instanceof ValidationException) && 
                !(e instanceof HttpRequestMethodNotSupportedException) &&
                !(e instanceof HttpMessageConversionException) &&
                !(e instanceof BeansException) &&
                !(e instanceof ServletRequestBindingException) &&
                !(e instanceof MethodArgumentNotValidException) &&
                !(e instanceof IllegalArgumentException) &&
                !(e instanceof IllegalStateException) &&
                !(e instanceof BaseException))
        {
            model =CommonResult.error(JSYError.UNKNOWN_EXCEPTION.getCode(), "系统异常，请联系客服！");
            this.logger.error(e.getMessage(), e);
        } else {
            model =CommonResult.error(JSYError.PARAMETER_EXCEPTION.getCode(), e.getMessage());
            this.logger.error(e.getMessage(), e);
        }
        return model;
    }

    public String exception(Exception e) {
        return "error";
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MyDateEditor());
    }

    private class MyDateEditor extends PropertyEditorSupport {
        private MyDateEditor() {
        }
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            Date date = null;
            if (StringUtils.hasText(text)) {
                try {
                    if (text.indexOf(":") > 0) {
                        if (text.length() == 5) {
                            date = DateUtils.parseDate(text, new String[]{"HH:mm"});
                        } else if (text.length() == 19) {
                            date = DateUtils.parseDate(text, new String[]{"yyyy-MM-dd hh:mm:ss"});
                        }
                    } else if (text.length() == 10) {
                        date = DateUtils.parseDate(text, new String[]{"yyyy-MM-dd"});
                    } else if (text.length() == 11) {
                        date = DateUtils.parseDate(text, new String[]{"yyyy年MM月dd日"});
                    }
                    if (date == null) {
                        String errorMsg = "Could not parse date, date format is error " + text;
                        ControllerHandler.this.logger.error(errorMsg);
                        throw new IllegalArgumentException(errorMsg);
                    }
                } catch (ParseException var5) {
                    IllegalArgumentException iae = new IllegalArgumentException(text + "Could not parse date: " + var5.getMessage(), var5);
                    ControllerHandler.this.logger.error(iae.getMessage());
                    throw iae;
                }
            }this.setValue(date);
        }
    }
}
