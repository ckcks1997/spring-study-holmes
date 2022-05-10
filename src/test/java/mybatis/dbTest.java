package mybatis;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.sql.Connection;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;
import model.StudyMember;
import service.CommunityBoardDao;
import service.StudyMemberDao;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
@Log4j2
public class dbTest {

	@Resource
	@Autowired
	StudyMemberDao smd;

	@Test 
	@Transactional //테스트 이후 데이터 rollabck
	public void 회원데이터추가테스트() throws Exception{
		StudyMember x = new StudyMember().builder()
				.email("a111")
				.password("111")
				.nickname("test")
				.name("test")
				.tel("123")
				.picture(" ").build();

		int a = smd.insertStudyMember(x);
		log.info("결과:"+a);
		assertSame(1, a); 
	}

}
