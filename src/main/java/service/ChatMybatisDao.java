package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.WebChat;

@Component
public class ChatMybatisDao {

	private static final String NS = "webchat.";
	private Map<String, Object> map = new HashMap<>();

	@Autowired
	MySqlSessionFactory sqlSessionFactory;
	SqlSession sqlSession;

	@PostConstruct
	public void setSqlSession() {
		this.sqlSession = sqlSessionFactory.sqlmap.openSession();
	}

	public int nextNum() {
		return sqlSession.selectOne(NS + "nextNum", map);
	}

	public int insertWebChat(WebChat webchat) {

		try {
			return sqlSession.insert(NS + "insertWebChat", webchat);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.commit();
		}
		return 0;
	}

	public List<WebChat> listWebChat(String groupId) {
		return sqlSession.selectList(NS + "listWebChat", groupId);
	}

}
