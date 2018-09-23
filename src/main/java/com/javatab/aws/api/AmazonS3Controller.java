package com.javatab.aws.api;

import com.javatab.aws.service.AmazonS3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/aws/s3/")
public class AmazonS3Controller {

    private AmazonS3Service amazonS3Service;

    public AmazonS3Controller(AmazonS3Service amazonS3Service) {
        this.amazonS3Service = amazonS3Service;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        String result = this.amazonS3Service.uploadFileToBucket(file);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/deleteFile")
    public ResponseEntity<String> deleteFile(@RequestPart(value = "key") String key) {
        String result = this.amazonS3Service.deleteFileFromBucket(key);
        return ResponseEntity.ok(result);
    }

}
