package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Notice;
import util.MybatisConnection;

@Component
public class NoticeDao {

	private static final String NS = "notice.";
	private Map<String, Object> map = new HashMap<>();

	@Autowired
	MySqlSessionFactory sqlSessionFactory;
	SqlSession sqlSession;

	@PostConstruct
	public void setSqlSession() {
		this.sqlSession = sqlSessionFactory.sqlmap.openSession();
	}

	public int noticeNew(String id) {

		return sqlSession.selectOne(NS + "noticeNew", id);

	}

	public List<Notice> noticeGet(String id) {

		return sqlSession.selectList(NS + "noticeGet", id);

	}

	public int noticeRead(String id) {

		try {
			return sqlSession.update(NS + "noticeRead", id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.commit();
		}
		return 0;
	}

	public int noticeWrite(int boardid, String from, String to) {

		map.clear();
		map.put("boardid", boardid);
		map.put("from", from);
		map.put("to", to);

		return sqlSession.insert(NS + "noticeWrite", map);

	}

	public int noticeReportWrite(String info, String from, String to, int board_num) {

		map.clear();
		map.put("info", info);
		map.put("from", from);
		map.put("to", to);
		map.put("board_num", board_num);

		return sqlSession.insert(NS + "noticeReportWrite", map);

	}

	public Notice noticeGetByNoticeNum(int noticeNum) {

		return sqlSession.selectOne(NS + "noticeGetByNoticeNum", noticeNum);

	}

}
