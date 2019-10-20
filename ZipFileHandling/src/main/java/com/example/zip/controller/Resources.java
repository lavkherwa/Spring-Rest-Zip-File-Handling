package com.example.zip.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.example.zip.util.FileProcessingHelper;

@RestController
@RequestMapping("/api")
public class Resources {

	@Autowired
	FileProcessingHelper fileProcessingHelper;

	/* Upload combination of ZIP and normal files */
	@PostMapping("/upload")
	public ResponseEntity<String> handleUpload(@RequestParam("files") MultipartFile[] uploadfiles) throws Exception {

		try {
			fileProcessingHelper.saveUploadedFiles(Arrays.asList(uploadfiles));
		} catch (IOException ioException) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>("File uploaded successfully!", HttpStatus.CREATED);
	}

	/* Download files as ZIP */
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StreamingResponseBody> handleDownload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment;filename=sample.zip");

		List<String> files = fileProcessingHelper.listAllFilesFromGivenPath("src/main/resources/files/");

		StreamingResponseBody stream = out -> {
			/* Create a ZIP output stream from response output stream */
			try (final ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
				/* Gather all files and put it in a ZIP */
				for (String file : files) {
					fileProcessingHelper.addGivenFileToZip(zipOut, file);
				}
			}

		};

		return new ResponseEntity<StreamingResponseBody>(stream, HttpStatus.OK);
	}

}
