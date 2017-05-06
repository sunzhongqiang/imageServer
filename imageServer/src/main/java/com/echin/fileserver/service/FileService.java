package com.echin.fileserver.service;

public interface FileService {
	//图片服务器域名
	String HOST = "img.manmankuai.com";
	
	//图片服务器存放地址
	String UPLOAD_PATH = "/data/wwwroot/"+HOST;
	
	/**
	 * 文件上传方法
	 * @param dir 路径地址
	 * @param name 文件名
	 * @param file 文件内容
	 * @return 保存后的外网地址
	 */
	String upload(String dir,String name,byte[] file);
	
}