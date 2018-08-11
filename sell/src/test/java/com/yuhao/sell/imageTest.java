package com.yuhao.sell;

import com.yuhao.sell.utils.ImageUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class imageTest {
	public static void main(String[] args){
		MultipartFile file = (MultipartFile) new ImageIcon("C:\\Users\\Administrator\\Desktop\\image.jpg");
		try {
			System.out.print(ImageUtils.uploadToCDNAndGetUrl(file.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
