package com.javatab.aws.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class AmazonS3ServiceImpl implements AmazonS3Service {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${awsS3.bucketName}")
    private String bucketName;

    private AmazonS3 amazonS3Client;

    public AmazonS3ServiceImpl(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public String uploadFileToBucket(MultipartFile multipartFile) {
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            uploadFileTos3bucket(fileName, file);
            logger.info("File uploaded successfully");
            file.delete();
        } catch (AmazonServiceException ase) {
            logger.error("Caught an AmazonServiceException from PUT requests, rejected reasons:");
            logger.error("Error Message:    " + ase.getMessage());
            logger.error("HTTP Status Code: " + ase.getStatusCode());
            logger.error("AWS Error Code:   " + ase.getErrorCode());
            logger.error("Error Type:       " + ase.getErrorType());
            logger.error("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.error("Caught an AmazonClientException: ");
            logger.error("Error Message: " + ace.getMessage());
        } catch (IOException e) {
            logger.error("IO Exception while converting multipart to File");
        }

        return "File uploaded successfully";
    }

    @Override
    public String deleteFileFromBucket(String key) {
        this.amazonS3Client.deleteObject(new DeleteObjectRequest(this.bucketName, key));
        return "File deleted successfully";
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        this.amazonS3Client.putObject(new PutObjectRequest(this.bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }


}
