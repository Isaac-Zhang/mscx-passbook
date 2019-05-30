package com.sxzhongf.mscx.passbook.advice;

import com.sxzhongf.mscx.passbook.vo.ErrorInfoVO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * GlobleExceptionHandler for 全局异常处理
 *
 * @author <a href="mailto:magicianisaac@gmail.com">Isaac.Zhang</a>
 * @see
 * @since 2019/5/30
 */
@ControllerAdvice
public class GlobleExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ErrorInfoVO<String> errorHandler(HttpServletRequest request, Exception ex)
            throws Exception {
        ErrorInfoVO<String> errorInfoVO = new ErrorInfoVO<>();
        errorInfoVO.setCode(ErrorInfoVO.ERROR);
        errorInfoVO.setMessage(ex.getMessage());
        errorInfoVO.setData("Do Not Have Return Data");
        errorInfoVO.setUrl(request.getRequestURL().toString());
        return errorInfoVO;
    }
}
