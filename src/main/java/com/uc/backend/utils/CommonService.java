package com.uc.backend.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FileUtils;



import static com.example.universityclass.utils.CustomConstants.*;

public class CommonService {

	public void getObj(AmazonS3 s3client)  {
	        String bucketName = BUCKET_NAME;
	        String objectName = BUCKET_FILE_PATH ;

	        try {
	        S3Object s3object = s3client.getObject(bucketName, objectName);
	        S3ObjectInputStream inputStream = s3object.getObjectContent();
	        FileUtils.copyInputStreamToFile(inputStream, new File(LOCAL_DOWNLOAD_PATH));
	        
	        
	        System.out.println("file copied to destination.");
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
	    }
	
	public static void createFolder(String bucketName, String folderName, AmazonS3 client,String SUFFIX) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);

		// create empty content+
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + SUFFIX, emptyContent,
				metadata);

		// send request to S3 to create folder
		client.putObject(putObjectRequest);
	}

	/**
	 * This method first deletes all the files in given folder and than the folder
	 * itself
	 */

	public static void deleteFolder(String bucketName, String folderName, AmazonS3 client) {
		List fileList = client.listObjects(bucketName, folderName).getObjectSummaries();
		for (Object object : fileList) {
			S3ObjectSummary file = (S3ObjectSummary) object;
			client.deleteObject(bucketName, file.getKey());
		}
		client.deleteObject(bucketName, folderName);
	}
	        
}
	

