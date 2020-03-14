package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.taotao.common.utils.JsonUtils;
import com.taotao.utils.FastDFSClient;

@Controller
public class PictureController {

	@Value("${TAOTAO_IMAGE_SERVER_URL}")
	private String TAOTAO_IMAGE_SERVER_URL;
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String picUpload(MultipartFile uploadFile)
	{
		try {
			System.out.println(TAOTAO_IMAGE_SERVER_URL);
			System.out.println("uplodeFile..进来了....");
		//接受上传的文件
		//获取文件的名字
		String originalFilename = uploadFile.getOriginalFilename();
		//将文件的名字进行分割
		String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
		//从新命名名字
		
		//将文件放到服务器中
		
			FastDFSClient f =new FastDFSClient("classpath:resource/client.conf");
			String url = f.uploadFile(uploadFile.getBytes(),extName);
			url=TAOTAO_IMAGE_SERVER_URL+url;
			
			Map result =new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			return JsonUtils.objectToJson(result);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Map result =new HashMap<>();
			result.put("error", 1);
			result.put("message", "上传图片失败");
			return JsonUtils.objectToJson(result);
		}
		//返回上传成功,或者上传失败
	}
}
