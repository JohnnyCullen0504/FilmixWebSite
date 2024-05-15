package com.hhu.filmix.config;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;

@Configuration
@Slf4j
public class MinioConfig {
    private Environment environment;
    @Bean()
    public MinioClient minioClient(){

        log.info("MinIO endpoint ---> " + environment.getProperty("app.main.minio.endpoint"));
        return MinioClient.builder()
                .endpoint(Objects.requireNonNull(environment.getProperty("app.main.minio.endpoint")))
                .credentials(
                        Objects.requireNonNull(environment.getProperty("app.main.minio.access-key")),
                        Objects.requireNonNull(environment.getProperty("app.main.minio.secret-key")))
                .build();
    }

    public MinioConfig(Environment environment) {
        this.environment = environment;
    }
}
