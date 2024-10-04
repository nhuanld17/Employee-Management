package com.example.demo.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileUtil {
	public static MultipartFile convertBytesToMultipartFile(byte[] bytes, String fileName, String fileType) {
		return new MockMultipartFile(fileName, fileName, fileType, bytes);
	}
}
