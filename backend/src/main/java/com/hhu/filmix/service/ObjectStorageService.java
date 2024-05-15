package com.hhu.filmix.service;

import java.io.InputStream;

public interface ObjectStorageService {
    void uploadObject(String bucketName, String objectName, InputStream stream);
    String getObjectURL(String bucketName,String objectName);
}
