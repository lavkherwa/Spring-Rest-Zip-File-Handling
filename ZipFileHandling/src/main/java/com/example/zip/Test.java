package com.example.zip;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Test {

	public static void main(String[] args) {

		try {
			FileInputStream fileInputStream = new FileInputStream("src/main/resources/files/lav.zip");
			ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
			ZipEntry file = zipInputStream.getNextEntry();
			while (file != null) {
				String fileName = file.getName();
				System.out.println(fileName);

				zipInputStream.closeEntry();
				file = zipInputStream.getNextEntry();
			}
			// close last ZipEntry
			zipInputStream.closeEntry();
			zipInputStream.close();
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
