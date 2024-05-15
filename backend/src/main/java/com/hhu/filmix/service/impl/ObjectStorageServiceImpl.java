package com.hhu.filmix.service.impl;

import com.hhu.filmix.service.ObjectStorageService;
import io.minio.*;
import io.minio.http.Method;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.namespace.QName;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ObjectStorageServiceImpl implements ObjectStorageService {
    private MinioClient minioClient;
//    private MinioClient frontendFakeMinioClient;

    @Value("${app.main.minio.endpoint}")
    private String endPointUrl;

    @Value("${app.main.minio.frontend-endpoint:unknown}")
    private String frontendEndpoint;

    public ObjectStorageServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    //文件桶检查
    @SneakyThrows
    //TODO: Handle Minio Exception
    private void checkBucket(String bucketName){
        Boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if(!found){
            //若不存在，创建新文件桶
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

    }

    @SneakyThrows
    @Override
    public void uploadObject(String bucketName, String objectName, InputStream stream) {
        checkBucket(bucketName);
        minioClient.putObject(PutObjectArgs
                .builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(stream,-1, 10485760)
                .build());
    }

    @SneakyThrows
    @Override
    public String getObjectURL(String bucketName, String objectName) {
        //Signe Url for frontend
        Map<String, String> reqParams = new HashMap<String, String>();
        reqParams.put("response-content-type", "Image/jpeg");
        String accessUrl =
                minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(bucketName)
                                .object(objectName)
                                .expiry(2, TimeUnit.HOURS)
                                .extraQueryParams(reqParams)
                                .build()
                );

        return accessUrl;
    }
}
