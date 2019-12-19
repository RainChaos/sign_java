package cn.gljp.sign.admin.controller;

import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandler {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e){
        log.error(toString(e.getStackTrace()) + "——异常：" + e);
        e.printStackTrace();

        String errorMsg = "";
        if(e.getMessage() != null && !e.getMessage().equals("") && e.getMessage().contains("错误：")){

            errorMsg = e.getMessage().toString().replace("错误：", "");
            if(errorMsg.equals("50008"))
                return new Result(50008, false, "", "");
        }else{
            errorMsg = "请求失败";
        }
        return new Result(StatusCode.ERROR, false, errorMsg, "");
    }

    public String toString(StackTraceElement[] stackTraceElements)
    {
        StringBuilder sb = new StringBuilder();
        for(StackTraceElement stackTraceElement : stackTraceElements)
        {
            sb.append(stackTraceElement.toString()+"\n");
        }
        return sb.toString();
    }
}
