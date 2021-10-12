package com.imooc.example.springdtx;

import com.imooc.example.springdtx.web.QueueProductMQ;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTransJMSApplication.class)
@WebAppConfiguration
public class SpringBootDbApplicationTests {
	@Autowired
	private QueueProductMQ queueMQ;

	@Test
	public void contextLoads() {
	}

	@Test
	public void queueMQ() throws Exception{
		queueMQ.productQueue();

	}


}
