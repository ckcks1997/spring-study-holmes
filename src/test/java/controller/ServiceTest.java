package controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import service.CommunityBoardDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
public class ServiceTest {

	@Autowired
	CommunityBoardDao cbd;

	@Test
	public void testComBoardCount() throws Exception{
		System.out.println(cbd.comBoardCount("1"));
	}

}
