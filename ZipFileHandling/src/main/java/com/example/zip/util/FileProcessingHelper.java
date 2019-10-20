package com.example.zip.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileProcessingHelper {

	public void saveUploadedFiles(List<MultipartFile> files) throws IOException {

		for (MultipartFile file : files) {

			if (file.isEmpty()) {
				/* if file is empty then continue to next file */
				continue;
			} else if ("zip".equalsIgnoreCase(getFileExtension(file))) {
				saveFilesFromZip(file);
			} else {
				saveSingleFile(file);
			}

		}
	}

	private void saveSingleFile(MultipartFile file) throws IOException {
		/* Create a new local file in our local resource folder */
		String absoluteFilePath = "src/main/resources/files/" + file.getOriginalFilename();
		File localFile = new File(absoluteFilePath);
		localFile.createNewFile();

		/* Copy context of incoming file to our local file */
		try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(localFile)) {
			FileCopyUtils.copy(in, out);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void saveFilesFromZip(MultipartFile zipFile) {
		byte[] buffer = new byte[1024];

		try (ZipInputStream zipInputStream = new ZipInputStream(zipFile.getInputStream())) {

			/* Start extracting the ZIP entries and add them to LIST */
			ZipEntry file = zipInputStream.getNextEntry();

			while (file != null) {
				/* Create a new local file in our local resource folder */
				String absoluteFilePath = "src/main/resources/files/" + file.getName();
				File localFile = new File(absoluteFilePath);
				localFile.createNewFile();

				/* Copy context of current ZIP entry to our local file */
				try (OutputStream out = new FileOutputStream(localFile)) {
					int len;
					while ((len = zipInputStream.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}

				zipInputStream.closeEntry();

				/* Move to next entry */
				file = zipInputStream.getNextEntry();
			}

			zipInputStream.closeEntry();

		} catch (IOException ioException) {
			// do something
			if (ioException != null) {

			}
		}

	}

	public void addFilesToZip(ZipOutputStream zipOut, String file) throws IOException {

		/* Get file input stream */
		try (final InputStream inputStream = getClass().getResourceAsStream("/files/" + file)) {

			/* Create new ZIP entry */
			final ZipEntry zipEntry = new ZipEntry(file);
			zipOut.putNextEntry(zipEntry);

			/* Stream data to ZIP output stream */
			byte[] bytes = new byte[1024];
			int length;
			while ((length = inputStream.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
		}
	}

	public List<String> readAllFilesFromPath(String path) {

		List<String> files = new ArrayList<>();

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				files.add(file.getName());
			}
		}

		return files;
	}

	public static String getFileExtension(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}
}
