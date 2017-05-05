package com.echin.fileserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.echin.fileserver.service.FileService;

@RestController
public class FileServer {

	@Resource
	private FileService fileService;
	
	/**
	 * api调用统一入口
	 * @param method 调用的方法
	 * @param timestamp 调用时间戳 时间误差默认10  分组
	 * @param appKey 调用者的appid
	 * @param sign  对以上参数进行签名
	 * @param params 方法参数使用base64进行解密 
	 * @return
	 * @throws IOException 
	 */
	
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> apiOrderList(MultipartFile file,String dir,String name ,String timestamp,String appKey,String sign) throws IOException{
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			//判断时间戳，如果超过时间误差直接返货错误提示
			if(isTimeout(timestamp)){  
				result.put("success", false);
				result.put("code", 408);
				result.put("message", "时间超时，请重新发送");
				return result;
			}
			
			if(!valid(appKey, timestamp, sign)){
				result.put("success", false);
				result.put("code", 420);
				result.put("message", "签名错误，无法进行上传");
				return result;
			}
		} catch(IllegalArgumentException e){
			result.put("success", false);
			result.put("code", 500);
			result.put("message", "参数出错");
		} 
		catch (Exception e) {
			result.put("success", false);
			result.put("code", 500);
			result.put("message", "应用接口出错");
			return result;
		}
		name = UUID.randomUUID().toString().replaceAll("-", "")+name.substring(name.indexOf("."));
		String url = fileService.upload(dir,name, file.getBytes());
		result.put("success", true);
		result.put("url", url);
		return result;
	}
	 
	/**
	 * 验证签名
	 * @param appKey 应用签名
	 * @param timestamp 时间戳
	 * @param sign 签名
	 * @return
	 */
	private boolean valid(String appKey,String timestamp, String sign) {
		
		return true;
	}

	/**
	 * 时间是否超时
	 * @param timestamp
	 * @return 是否超时 
	 */
	private boolean isTimeout(String timestamp) throws IllegalArgumentException{
		DateTime dateTime = DateTime.parse(timestamp, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDateTime();
		return (dateTime.plusMinutes(10).isBeforeNow()||dateTime.plusMinutes(-10).isAfterNow());
	}
	
	
	
}
