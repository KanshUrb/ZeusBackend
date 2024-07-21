package com.kansh.zeus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.sql.DataSource;

@Slf4j
@SpringBootApplication
@CrossOrigin
public class ZeusApplication implements CommandLineRunner {

	private final DataSource dataSource;

	ZeusApplication(DataSource dataSource) {this.dataSource = dataSource;}

	public static void main(String[] args) {
		SpringApplication.run(ZeusApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Datasource: {}", dataSource.toString());
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.execute("select 1");
	}
}
