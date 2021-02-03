package com.zdhk.ipc.exception;

import com.zdhk.ipc.constant.BASE_JSON_CODE;
import com.zdhk.ipc.data.rsp.BaseResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;
import java.util.*;


@ControllerAdvice
public class GlobalExceptionController {
private static final Logger log = LoggerFactory.getLogger(GlobalExceptionController.class);

	/**
	 * 自定义错误，前台传入参数有误
	 * @param req
	 * @param ex
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@ExceptionHandler(ReqException.class)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public Map<String,Object> handleReqException(HttpServletRequest req,ReqException ex,HttpServletResponse response) throws UnsupportedEncodingException{
		Map<String,Object> errorMap = new HashMap<String,Object>();
		errorMap.put("code", ex.getCode());
		errorMap.put("desc",ex.getMessage());
		log.error("code:{},detail:{}",ex.getCode(),ex.getMessage());
		return errorMap;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public BaseResp handleException(HttpServletRequest req, Exception e){
		BaseResp rsp = new BaseResp();
		if (e instanceof MissingServletRequestParameterException) {
			rsp.setCode(SysExceptionEnum.BAD_REQUEST_PARAM_MISS.getCode());
			rsp.setDesc(SysExceptionEnum.BAD_REQUEST_PARAM_MISS.getMessage());
		} else if (e instanceof MethodArgumentTypeMismatchException
				|| ((e instanceof IllegalStateException) && e.getMessage().contains("parameter"))) {
			rsp.setCode(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getCode());
			rsp.setDesc(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getMessage());
		} else if (e instanceof BindException) {
			if (((BindException) e).hasErrors()) {
				StringBuilder errors = new StringBuilder();
				for (FieldError fieldError : ((BindException) e).getBindingResult().getFieldErrors()) {
					errors.append(fieldError.getDefaultMessage()).append("，");
				}
				rsp.setCode(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getCode());
				rsp.setDesc(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getMessage() + " -> " + errors.substring(0, errors.lastIndexOf("，")));
			}
		} else if (e instanceof ConstraintViolationException) {
			Set<ConstraintViolation<?>> set = ((ConstraintViolationException) e).getConstraintViolations();
			if (set != null && !set.isEmpty()) {
				Iterator<ConstraintViolation<?>> iterator = set.iterator();
				StringJoiner joiner = new StringJoiner("，", "", "");
				while (iterator.hasNext()) {
					joiner.add(iterator.next().getMessage());
				}
				rsp.setCode(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getCode());
				rsp.setDesc(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getMessage() + " -> " + joiner.toString());
			}
		}else if(e instanceof IllegalArgumentException){
			rsp.setCode(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getCode());
			rsp.setDesc(SysExceptionEnum.BAD_REQUEST_PARAM_ERROR.getMessage());
		} else {
			log.error("[{}]_{}", SysExceptionEnum.SYSTEM_ERROR.getCode(), e.getMessage(), e);
			//e.printStackTrace();
			rsp.setCode(SysExceptionEnum.SYSTEM_ERROR.getCode());
			rsp.setDesc(SysExceptionEnum.SYSTEM_ERROR.getMessage());
		}
		return rsp;
		
	}

	

}
