package com.blog.blog;

import org.springframework.boot.SpringApplication;

public class TestBlogApplication {

	public static void main(String[] args) {
		SpringApplication.from(BlogApplication::main).with(MyTestcontainersConfiguration.class).run(args);
	}

}
