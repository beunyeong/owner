package com.example.oner.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public S3Service(
            @Value("${aws.s3.bucket}") String bucketName,
            @Value("${aws.credentials.accessKey}") String accessKey,
            @Value("${aws.credentials.secretKey}") String secretKey,
            @Value("${aws.region}") String region) {

        this.bucketName = bucketName;

        // AWS Credentials 설정
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        // S3 Client 설정
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    /**
     * 파일 업로드 메서드
     *
     * @param file       업로드할 파일
     * @param folderName S3 버킷 내 폴더 이름
     * @return 업로드된 파일의 URL
     */
    public String uploadFile(MultipartFile file, String folderName) {
        String fileName = folderName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        }

        return amazonS3.getUrl(bucketName, fileName).toString();

    }

    public String uploadFile(String key, InputStream inputStream, String contentType, long contentLength) {
        try {
            // 메타데이터 생성
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentLength);
            metadata.setContentType(contentType);

            // 파일 업로드
            amazonS3.putObject(bucketName, key, inputStream, metadata);

            // 업로드된 파일 URL 반환
            return amazonS3.getUrl(bucketName, key).toString();
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        }
    }

    public void deleteFile(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);
        amazonS3.deleteObject(bucketName, key);
    }

    private String extractKeyFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            return path.startsWith("/") ? path.substring(1) : path;
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 S3 URL입니다: " + fileUrl, e);
        }
    }
}


