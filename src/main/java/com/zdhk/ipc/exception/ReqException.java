package com.zdhk.ipc.exception;

public class ReqException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    private Integer code;
    private Object result;

    public ReqException(){
        super();
    }
    
    public ReqException(String msg,Integer errorCode){
        super(msg);
        code=errorCode;
    }
    
    public ReqException(String msg,Integer errorCode,Object result){
    	super(msg);
    	code=errorCode;
    	this.result = result;
    }
    
    public Object getResult(){
    	return result;
    }
    
    public Integer getCode() {
        return code;
    }
}
