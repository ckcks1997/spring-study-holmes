package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.ReputationEstimate;
import model.Search;
import model.StudyMenu;
import util.MybatisConnection;

@Component
public class ReputationEstimateDao {
	private static final String NS = "reputationestimate.";
	private Map<String, Object> map = new HashMap<>();

	@Autowired
	MySqlSessionFactory sqlSessionFactory;
	SqlSession sqlSession;

	@PostConstruct
	public void setSqlSession() {
		this.sqlSession = sqlSessionFactory.sqlmap.openSession();
	}

	public int insertReputation(ReputationEstimate rep) {
		try {
			return sqlSession.insert(NS + "insertReputation", rep);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.commit();
		}
		return 0;
	}

	public List<ReputationEstimate> getReputation(String nick) {

		return sqlSession.selectList(NS + "getReputation", nick);

	}
}
