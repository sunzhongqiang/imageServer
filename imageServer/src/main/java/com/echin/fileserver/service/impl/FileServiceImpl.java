package com.echin.fileserver.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.echin.fileserver.service.FileService;
@Service
public class FileServiceImpl implements FileService{

	@Override
	public String upload(String dir,String name, byte[] file) {
		try {
			Calendar instance = Calendar.getInstance();
			String year = instance.get(Calendar.YEAR)+"";
			String month = instance.get(Calendar.MONTH)+1+"";
			String day = instance.get(Calendar.DAY_OF_MONTH)+"";
			//路径
			String path = joinBy(File.separator,UPLOAD_PATH,dir, year, month, day);
			//保存文件
			saveFile(path,name,file);
			return joinBy("/", "http://"+HOST,dir,year,month,day)+name;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 将文件保存到想应的文件位置上
	 * @param savePath 保存路径
	 * @param name 文件名
	 * @param file 文件内容
	 * @return  文件路径
	 * @throws IOException 异常
	 */
	private String saveFile(String savePath, String name, byte[] file) throws IOException {
		Path p = Paths.get(savePath);
		if (!Files.exists(p)) {
			Files.createDirectories(p);
		}
		Files.write(Paths.get(savePath+File.separator+name), file,StandardOpenOption.CREATE);
		return savePath+File.separator+name;
	}
	
	/**
	 * 将路径使用文件分隔符拼接并返回
	 * @param separator  分隔符
	 * @param path 路径
	 * @return 拼接后的路径
	 */
	private String joinBy(String separator,String... path){
		StringBuilder pathBuilder = new StringBuilder();
		for (String subpath : path) {
			pathBuilder.append(subpath);
			pathBuilder.append(separator);
		}
		return pathBuilder.toString();
	}
	
	

}
