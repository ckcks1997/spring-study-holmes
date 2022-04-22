package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Search;
import model.StudyMenu;
import util.MybatisConnection;

@Component
public class StudyMenuDao {
	private static final String NS = "studymenu.";
	private Map<String, Object> map = new HashMap<>();

	@Autowired
	MySqlSessionFactory sqlSessionFactory;
	SqlSession sqlSession;

	@PostConstruct
	public void setSqlSession() {
		this.sqlSession = sqlSessionFactory.sqlmap.openSession();
	}

	public int menuCount(String menuid) {
		try {
			return sqlSession.selectOne(NS + "menuCount", menuid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int myStudyCount(String nickname) {
		try {
			return sqlSession.selectOne(NS + "myStudyCount", nickname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int onAllCount() {
		try {
			return sqlSession.selectOne(NS + "onAllCount");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int offAllCount() {
		try {
			return sqlSession.selectOne(NS + "offAllCount");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int onoffAllCount() {
		try {
			return sqlSession.selectOne(NS + "onoffAllCount");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<StudyMenu> menuList(int pageInt, int limit, int menucount, String menuid) {

		map.clear();
		map.put("menuid", menuid);
		map.put("start", (pageInt - 1) * limit + 1);
		map.put("end", pageInt * limit);
		return sqlSession.selectList(NS + "menuList", map);

	}

	/* 추가됨 */
	public List<StudyMenu> menuList2(int pageInt, int limit, int menucount, String menuid) {

		map.clear();
		map.put("menuid", menuid);
		map.put("start", (pageInt - 1) * limit + 1);
		map.put("end", pageInt * limit);
		return sqlSession.selectList(NS + "menuList2", map);

	}

	public int menuNextNum() {

		try {
			return sqlSession.selectOne(NS + "menuNextNum");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int insertMenu(StudyMenu studymenu) {
		try {
			return sqlSession.update(NS + "insertMenu", studymenu);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.commit();
		}
		return 0;
	}

	public List<StudyMenu> studySearch(Search sh) {
		List<StudyMenu> list = sqlSession.selectList(NS + "studySearch", sh);
		return list;
	}

	public StudyMenu menuBoardOne(int board_num) {
		return sqlSession.selectOne(NS + "menuBoardOne", board_num);

	}

	public List<StudyMenu> list2(String list2) {

		List<StudyMenu> list = sqlSession.selectList(NS + "list2", list2);

		return list;

	}

	/*-----------------------------------------------------------------------------------------------------------------		*/
	public List<StudyMenu> studySearch(String part, String searchData, String menuid) {
		List<StudyMenu> searchlist = null;
			map.clear();
			map.put("part", part);
			map.put("searchData", "%" + searchData + "%");
			map.put("menuid", menuid);
			searchlist = sqlSession.selectList(NS + "studySearch", map);
		return searchlist;
	}

	public int studySearchCount(String menuid, String part, String searchData) {
		try {
			map.clear();
			map.put("part", part);
			map.put("searchData", "%" + searchData + "%");
			map.put("menuid", menuid);
			return sqlSession.selectOne(NS + "studySearchCount", map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return 0;
	}

	public List<StudyMenu> studySearchList(int pageInt, int limit, int menucount, String menuid, String part,
			String searchData) {
		try {
			map.clear();
			map.put("menuid", menuid);
			map.put("start", (pageInt - 1) * limit + 1);
			map.put("end", pageInt * limit);
			map.put("part", part);
			map.put("searchData", "%" + searchData + "%");
			return sqlSession.selectList(NS + "studySearchList", map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	/*-----------------------------------------------------------------------------------------------------------------		*/
	public List<StudyMenu> onAllSearch(String part, String searchData, String menuid) {
		List<StudyMenu> searchlist = null;
		try {
			map.clear();
			map.put("part", part);
			map.put("searchData", "%" + searchData + "%");
			map.put("menuid", menuid);
			searchlist = sqlSession.selectList(NS + "onAllSearch", map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return searchlist;
	}

	public int onAllSearchCount(String menuid, String part, String searchData) {
		try {
			map.clear();
			map.put("part", part);
			map.put("searchData", "%" + searchData + "%");
			map.put("menuid", menuid);
			return sqlSession.selectOne(NS + "onAllSearchCount", map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return 0;
	}

	public List<StudyMenu> onAllSearchList(int pageInt, int limit, int menucount, String menuid, String part,
			String searchData) {
		
		try {
			map.clear();
			map.put("menuid", menuid);
			map.put("start", (pageInt - 1) * limit + 1);
			map.put("end", pageInt * limit);
			map.put("part", part);
			map.put("searchData", "%" + searchData + "%");
			return sqlSession.selectList(NS + "onAllSearchList", map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	/*-----------------------------------------------------------------------------------------------------------------		*/

	public List<StudyMenu> offAllSearch(String part, String searchData, String menuid) {
		
		List<StudyMenu> searchlist = null;
		try {
			map.clear();
			map.put("part", part);
			map.put("searchData", "%" + searchData + "%");
			map.put("menuid", menuid);
			searchlist = sqlSession.selectList(NS + "offAllSearch", map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return searchlist;
	}

	public int offAllSearchCount(String menuid, String part, String searchData) {
		
		try {
			map.clear();
			map.put("part", part);
			map.put("searchData", "%" + searchData + "%");
			map.put("menuid", menuid);
			return sqlSession.selectOne(NS + "offAllSearchCount", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<StudyMenu> offAllSearchList(int pageInt, int limit, int menucount, String menuid, String part,
			String searchData) {
		
		try {
			map.clear();
			map.put("menuid", menuid);
			map.put("start", (pageInt - 1) * limit + 1);
			map.put("end", pageInt * limit);
			map.put("part", part);
			map.put("searchData", "%" + searchData + "%");
			return sqlSession.selectList(NS + "offAllSearchList", map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	/*-----------------------------------------------------------------------------------------------------------------		*/

	public List<StudyMenu> onoffAllSearch(String part, String searchData, String menuid) {
		
		List<StudyMenu> searchlist = null;
		try {
			map.clear();
			map.put("part", part);
			map.put("searchData", "%" + searchData + "%");
			map.put("menuid", menuid);
			searchlist = sqlSession.selectList(NS + "onoffAllSearch", map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return searchlist;
	}

	public int onoffAllSearchCount(String menuid, String part, String searchData) {
		
		try {
			map.clear();
			map.put("part", part);
			map.put("searchData", "%" + searchData + "%");
			map.put("menuid", menuid);
			return sqlSession.selectOne(NS + "onoffAllSearchCount", map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return 0;
	}

	public List<StudyMenu> onoffAllSearchList(int pageInt, int limit, int menucount, String menuid, String part,
			String searchData) {
		
		try {
			map.clear();
			map.put("menuid", menuid);
			map.put("start", (pageInt - 1) * limit + 1);
			map.put("end", pageInt * limit);
			map.put("part", part);
			map.put("searchData", "%" + searchData + "%");
			return sqlSession.selectList(NS + "onoffAllSearchList", map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	/*-----------------------------------------------------------------------------------------------------------------		*/

	public List<StudyMenu> onallList(int pageInt, int limit, int menuAllCount, String menuid) {
		
		try {
			map.clear();
			map.put("menuid", menuid);
			map.put("start", (pageInt - 1) * limit + 1);
			map.put("end", pageInt * limit);
			return sqlSession.selectList(NS + "onallList", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<StudyMenu> offallList(int pageInt, int limit, int menuAllCount, String menuid) {
		
		try {
			map.clear();
			map.put("menuid", menuid);
			map.put("start", (pageInt - 1) * limit + 1);
			map.put("end", pageInt * limit);
			return sqlSession.selectList(NS + "offallList", map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	/* 추가 */
	public List<StudyMenu> offallList2(int pageInt, int limit, int menuAllCount, String menuid) {
		
		try {
			map.clear();
			map.put("menuid", menuid);
			map.put("start", (pageInt - 1) * limit + 1);
			map.put("end", pageInt * limit);
			return sqlSession.selectList(NS + "offallList2", map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public List<StudyMenu> onoffallList(int pageInt, int limit, int menuAllCount, String menuid) {
		
		try {
			map.clear();
			map.put("menuid", menuid);
			map.put("start", (pageInt - 1) * limit + 1);
			map.put("end", pageInt * limit);
			return sqlSession.selectList(NS + "onoffallList", map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public List<StudyMenu> mylist2(int pageInt, int limit, int menucount, String nickname) {
		
		try {
			map.clear();

			map.put("start", (pageInt - 1) * limit + 1);
			map.put("end", pageInt * limit);
			map.put("nickname", nickname);
			return sqlSession.selectList(NS + "mylist2", map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public int studyUpdate(StudyMenu sm) {
		

		try {
			return sqlSession.update(NS + "studyUpdate", sm);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	    	sqlSession.commit();
		}

		return 0;

	}

	public StudyMenu studyMenuOne(int board_num) {

		
		try {
			return sqlSession.selectOne(NS + "studyMenuOne", board_num);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public int studyDelete(int board_num) {
		
		try {
			return sqlSession.update(NS + "studyDelete", board_num);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	    	sqlSession.commit();
		}
		return 0;
	}

	public List<StudyMenu> mainNewStudy3() {

		List<StudyMenu> list = null;
		try {
			list = sqlSession.selectList(NS + "mainNewStudy3");
			return list;

		} catch (Exception e) {
			e.printStackTrace();
		} 

		return null;
	}

}
