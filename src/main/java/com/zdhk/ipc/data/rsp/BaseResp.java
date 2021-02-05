package com.zdhk.ipc.data.rsp;


import com.alibaba.fastjson.JSONObject;
import com.zdhk.ipc.exception.meta.ExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value="接口返回对象", description="接口返回对象")
@Data
@Builder
public class BaseResp<T> implements Serializable {

    public int code;
    public int start;
    public int end;
    public String desc;

	@ApiModelProperty(value = "返回数据对象")
    public T result;

    public BaseResp() {
        super();
    }

    public BaseResp(int code, String desc) {
        super();
        this.code = code;
        this.desc = desc;
    }

    public BaseResp(int code, String desc, T result) {
        super();
        this.code = code;
        this.desc = desc;
        this.result = result;
    }

	public BaseResp(int code, int start, int end, String desc, T result) {
		super();
		this.code = code;
		this.start = start;
		this.end = end;
		this.desc = desc;
		this.result = result;
	}

	public BaseResp(ExceptionEnum exEnum){
    	this.code = exEnum.getCode();
    	this.desc = exEnum.getMessage();
	}


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


	@Override
	public String toString() {
    	return JSONObject.toJSONString(this);
	}

	public static BaseResp returnSuccess(int code,String desc,Object result){
		BaseResp rsp = new BaseResp();
		rsp.setCode(code);
		rsp.setDesc(desc);
		rsp.setResult(result);
		return rsp;
	}


}
