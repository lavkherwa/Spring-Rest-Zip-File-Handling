# Spring-Rest-Zip-File-Handling

Zip file upload/download sample


This program will do following

- Upload files to folder: **src/main/resources/files/**
  - Normal files will be created as is
  - ZIP file if provided will be unzipped and files inside it will be created
  
- Download files from folder: **src/main/resources/files/**
  - All files will be zipped and downloaded as an attachment
  
**NOTE:** For handling zip download, async is required. [configuration code](ZipFileHandling/src/main/java/com/example/zip/config/AsyncConfig.java)
