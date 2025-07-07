package com.blog.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(MyTestcontainersConfiguration.class)
@SpringBootTest
class BlogApplicationTests {

	@Test
	void contextLoads() {
	}

}
