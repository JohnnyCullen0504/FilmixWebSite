package com.hhu.filmix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement//开启数据库事务模式
public class FilmixBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmixBackendApplication.class, args);
	}

}
