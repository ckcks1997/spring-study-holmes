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


//잘됬나 확인 한번 부탁드립니다.-준현-

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
			return sqlSession.selectOne(NS + "menuCount", menuid);
	}
	
	
	
	public int myStudyCount(String nickname) {
			return sqlSession.selectOne(NS + "myStudyCount", nickname);
	}
	
	
	public int onAllCount(String nickname) {
			return sqlSession.selectOne(NS + "onAllCount", nickname);
	}
	
	public int offAllCount(String nickname) {
			return sqlSession.selectOne(NS + "offAllCount", nickname);
	}
	
	public int onoffAllCount(String nickname) {
			return sqlSession.selectOne(NS + "onoffAllCount", nickname);
	}

	
	
	public List<StudyMenu> menuList(int pageInt, int limit, int menucount, String menuid) {
		try {
			map.clear();
			map.put("menuid", menuid);
			map.put("start", (pageInt - 1) * limit + 1);
			map.put("end", pageInt * limit);
			return sqlSession.selectList(NS + "menuList", map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.commit();
		}
		return null;
	}
	
	/*추가됨*/
	public List<StudyMenu> menuList2(int pageInt, int limit, int menucount, String menuid) {
		try {
			map.clear();
			map.put("menuid", menuid);
			map.put("start", (pageInt - 1) * limit + 1);
			map.put("end", pageInt * limit);
			return sqlSession.selectList(NS + "menuList2", map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.commit();
		}
		return null;
	}
	

	
	public int menuNextNum() {
			return sqlSession.selectOne(NS + "menuNextNum");
	}

	
	
	public int insertMenu(StudyMenu studymenu) {
		
			return sqlSession.update(NS + "insertMenu", studymenu);
	}
	
	
	
	public List<StudyMenu> studySearch(Search sh) {
		return sqlSession.selectList(NS + "studySearch", sh);
	
	}
	
	
	
	 public StudyMenu menuBoardOne(int board_num) {
				return sqlSession.selectOne(NS+"menuBoardOne",board_num);
		
	  }
 

	 public List<StudyMenu> list2(String list2) {

		
			return sqlSession.selectList(NS + "list2", list2);
		

		}
	  
	 
		/*-----------------------------------------------------------------------------------------------------------------		*/ 
	 public List<StudyMenu> studySearch(String part, String searchData, String menuid) {
			try {
				map.clear();
				map.put("part", part);
				map.put("searchData", "%" +searchData+ "%");
				map.put("menuid", menuid);
				return sqlSession.selectList(NS + "studySearch", map);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sqlSession.commit();			}
			return null;
		}
	 
	 
	 public int studySearchCount(String menuid, String part, String searchData) {
				map.clear();
				map.put("part", part);
				map.put("searchData", "%" +searchData+ "%");
				map.put("menuid", menuid);
				return sqlSession.selectOne(NS + "studySearchCount", map);
			
		}
	 
	 public List<StudyMenu> studySearchList(int pageInt, int limit, int menucount, String menuid, String part, String searchData) {
				map.clear();
				map.put("menuid", menuid);
				map.put("start", (pageInt - 1) * limit + 1);
				map.put("end", pageInt * limit);
				map.put("part", part);
				map.put("searchData", "%" +searchData+ "%");
				return sqlSession.selectList(NS + "studySearchList", map);	
	
		}
	 
	 /*-----------------------------------------------------------------------------------------------------------------		*/ 
	 public List<StudyMenu> onAllSearch(String part, String searchData, String menuid) {
			
				map.clear();
				map.put("part", part);
				map.put("searchData", "%" +searchData+ "%");
				map.put("menuid", menuid);
				return sqlSession.selectList(NS + "onAllSearch", map);
			
		}
	 
	 
	 public int onAllSearchCount(String menuid, String part, String searchData) {
			
				map.clear();
				map.put("part", part);
				map.put("searchData", "%" +searchData+ "%");
				map.put("menuid", menuid);
				return sqlSession.selectOne(NS + "onAllSearchCount", map);
			
		}
	 
	 public List<StudyMenu> onAllSearchList(int pageInt, int limit, int menucount, String menuid, String part, String searchData) {
			
				map.clear();
				map.put("menuid", menuid);
				map.put("start", (pageInt - 1) * limit + 1);
				map.put("end", pageInt * limit);
				map.put("part", part);
				map.put("searchData", "%" +searchData+ "%");
				return sqlSession.selectList(NS + "onAllSearchList", map);
			
		}
	 	 	 
	 /*-----------------------------------------------------------------------------------------------------------------		*/ 
	 
	 public List<StudyMenu> offAllSearch(String part, String searchData, String menuid) {
			
			
				map.clear();
				map.put("part", part);
				map.put("searchData", "%" +searchData+ "%");
				map.put("menuid", menuid);
				return sqlSession.selectList(NS + "offAllSearch", map);
			
		}
	 
	 public int offAllSearchCount(String menuid, String part, String searchData) {
			
				map.clear();
				map.put("part", part);
				map.put("searchData", "%" +searchData+ "%");
				map.put("menuid", menuid);
				return sqlSession.selectOne(NS + "offAllSearchCount", map);
			
		}
	 
	 public List<StudyMenu> offAllSearchList(int pageInt, int limit, int menucount, String menuid, String part, String searchData) {
			
				map.clear();
				map.put("menuid", menuid);
				map.put("start", (pageInt - 1) * limit + 1);
				map.put("end", pageInt * limit);
				map.put("part", part);
				map.put("searchData", "%" +searchData+ "%");
				return sqlSession.selectList(NS + "offAllSearchList", map);
			
		}
	 	 	 
	 /*-----------------------------------------------------------------------------------------------------------------		*/ 
	 
	 public List<StudyMenu> onoffAllSearch(String part, String searchData, String menuid) {
			
			
				map.clear();
				map.put("part", part);
				map.put("searchData", "%" +searchData+ "%");
				map.put("menuid", menuid);
				return sqlSession.selectList(NS + "onoffAllSearch", map);
			
		}
	 
	 
	 public int onoffAllSearchCount(String menuid, String part, String searchData) {
		
				map.clear();
				map.put("part", part);
				map.put("searchData", "%" +searchData+ "%");
				map.put("menuid", menuid);
				return sqlSession.selectOne(NS + "onoffAllSearchCount", map);
			
		}
	 
	 public List<StudyMenu> onoffAllSearchList(int pageInt, int limit, int menucount, String menuid, String part, String searchData) {
		
				map.clear();
				map.put("menuid", menuid);
				map.put("start", (pageInt - 1) * limit + 1);
				map.put("end", pageInt * limit);
				map.put("part", part);
				map.put("searchData", "%" +searchData+ "%");
				return sqlSession.selectList(NS + "onoffAllSearchList", map);
		
		}
	 	 	 
	 /*-----------------------------------------------------------------------------------------------------------------		*/ 
	 
	 public List<StudyMenu> onallList(int pageInt, int limit, int menuAllCount, String menuid) {
			
				map.clear();
				map.put("menuid", menuid);
				map.put("start", (pageInt - 1) * limit + 1);
				map.put("end", pageInt * limit);
				return sqlSession.selectList(NS + "onallList", map);
			
	 }	 
	 

	 
	 public List<StudyMenu> offallList(int pageInt, int limit, int menuAllCount, String menuid) {
		
				map.clear();
				map.put("menuid", menuid);
				map.put("start", (pageInt - 1) * limit + 1);
				map.put("end", pageInt * limit);
				return sqlSession.selectList(NS + "offallList", map);
			
	 }	 
 
	 /*추가*/
	 public List<StudyMenu> offallList2(int pageInt, int limit, int menuAllCount, String menuid) {
			
				map.clear();
				map.put("menuid", menuid);
				map.put("start", (pageInt - 1) * limit + 1);
				map.put("end", pageInt * limit);
				return sqlSession.selectList(NS + "offallList2", map);
			
	 }	
	 
	 public List<StudyMenu> onoffallList(int pageInt, int limit, int menuAllCount, String menuid) {
			
				map.clear();
				map.put("menuid", menuid);
				map.put("start", (pageInt - 1) * limit + 1);
				map.put("end", pageInt * limit);
				return sqlSession.selectList(NS + "onoffallList", map);
			
	 }	 
	 
	
	 public List<StudyMenu> mylist2(int pageInt, int limit, int menucount, String nickname) {
			
				map.clear();
				
				map.put("start", (pageInt - 1) * limit + 1);
				map.put("end", pageInt * limit);
				map.put("nickname",nickname);
				return sqlSession.selectList(NS + "mylist2", map);
			
		}
	 
	 
	 

	 

	 public int studyUpdate(StudyMenu sm) {
		
		  try {
		  return sqlSession.update(NS+"studyUpdate",sm);
		  } catch (Exception e) {
			  e.printStackTrace();
		  } finally {
			  sqlSession.commit();
		  }
		  
		  return 0;
		  
	  }
	 
	 public StudyMenu studyMenuOne(int board_num) {
		  
				return sqlSession.selectOne(NS+"studyMenuOne",board_num);
		  
	  }
	 
	 public int studyDelete (int board_num) {
		
		 try {
		  return sqlSession.update(NS+"studyDelete", board_num);
		 } catch(Exception e) {
			 e.printStackTrace();
		 } finally {
			 sqlSession.commit();
		 }
		  return 0;
	  }
	 

	 
     public List<StudyMenu> mainNewStudy3() {

      
       return sqlSession.selectList(NS + "mainNewStudy3");
     

      
}   

}

		 	