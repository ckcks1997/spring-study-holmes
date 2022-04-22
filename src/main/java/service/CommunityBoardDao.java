package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Community;
import util.MybatisConnection;


@Component
public class CommunityBoardDao {

	private static final String NS = "community.";
	  private Map<String, Object> map = new HashMap<>();
	
	  @Autowired
		MySqlSessionFactory sqlSessionFactory;
		SqlSession sqlSession;
		
		@PostConstruct
		public void setSqlSession() {
			this.sqlSession = sqlSessionFactory.sqlmap.openSession();
		}
		
	   
	  
	  public int comBoardCount(String boardid) {
		
			return sqlSession.selectOne(NS+"comBoardCount",boardid);
			
	  }
	  
	  public int myComCount(String nickname) {
			
			return sqlSession.selectOne(NS+"myComCount",nickname);
			
	  }
	  
	  //검색 수
	  public int comSearchCount(String boardid,String part,String searchData) {
			
				map.clear();
				map.put("part", part);
				map.put("searchData", "%"+searchData+"%");
				map.put("boardid", boardid);
				
			return sqlSession.selectOne(NS+"comSearchCount",map);
			
	  }
	  
	  
	  
	  
	  
	  
	  //최신순 나열
	  public List<Community> comBoardList(int pageInt, int limit, int boardcount, String boardid) {
		  
				map.clear();
				map.put("boardid", boardid);
				map.put("start", (pageInt-1)*limit+1);
				map.put("end", pageInt*limit);
					
			return sqlSession.selectList(NS+"comBoardList",map);
			
	  }
	  
	  
	  
	  //댓글순 나열
	  	public List<Community> comBoardReply(int pageInt, int limit, int boardcount, String boardid) {
		  
				map.clear();
				map.put("boardid", boardid);
				map.put("start", (pageInt-1)*limit+1);
				map.put("end", pageInt*limit);
					
			return sqlSession.selectList(NS+"comBoardReply",map);
			
	  }
	  
	  //조회수순 나열
		public List<Community> comBoardRead(int pageInt, int limit, int boardcount, String boardid) {
			  
					map.clear();
					map.put("boardid", boardid);
					map.put("start", (pageInt-1)*limit+1);
					map.put("end", pageInt*limit);
						
				return sqlSession.selectList(NS+"comBoardRead",map);
				
		  }
	  	
	  //내가 쓴 문의글 나열 
		public List<Community> comBoardMyAsk(int pageInt, int limit, int boardcount,String nickname) {
			  
					map.clear();
					map.put("nickname", nickname);
					map.put("start", (pageInt-1)*limit+1);
					map.put("end", pageInt*limit);
						
				return sqlSession.selectList(NS+"comBoardMyAsk",map);
				
		  }
	  
	  
	  
	  //검색리스트
	  public List<Community> comSearchList(int pageInt, int limit, int boardcount, String boardid, String part, String searchData) {
		  
				map.clear();
				map.put("boardid", boardid);
				map.put("start", (pageInt-1)*limit+1);
				map.put("end", pageInt*limit);
				map.put("part", part);
				map.put("searchData", "%"+searchData+"%");
					
			return sqlSession.selectList(NS+"comSearchList",map);
			
	  }
	  
	  
	  
	  public int comInsertBoard(Community com) {
		  
			
			try {
			return sqlSession.update(NS+"comInsertBoard", com);	
			} catch(Exception e ) {
				e.printStackTrace();
			} finally {
				sqlSession.commit();
			}
			return 0;
			}
	 
	  
	  
	  
	  public Community comBoardOne(int board_num) {
		 
				return sqlSession.selectOne(NS+"comBoardOne",board_num);
			
		  
	  }
	  
	   
	  public int comNextNum() {
		
			  return sqlSession.selectOne(NS+"comNextNum");
		  
	  }
	  
	  
	  public int comBoardUpdate(Community com) {
		  
		  
		  try {
		  return sqlSession.update(NS+"comBoardUpdate",com);
		  } catch (Exception e) {
			  e.printStackTrace();
		  } finally {
			  sqlSession.commit();
		  }
		  
		  return 0;
		  
	  }
	  
	  public int comBoardDelete (int board_num) {
		  
		 try {
		  return sqlSession.update(NS+"comBoardDelete", board_num);
		 } catch(Exception e) {
			 e.printStackTrace();
		 } finally {
			 sqlSession.commit();
		 }
		  return 0;
	  }
	  
	  /*조회수 증가*/
	  public void comReadCountUp(int board_num) {
		  
			 try {
			  sqlSession.update(NS+"comReadCountUp",board_num);
			 } catch(Exception e) {
				 e.printStackTrace();
			 } finally {
				 sqlSession.commit();
			 }
		  
	  }
	  
	  
	  public List<Community> comSearch(String part,String searchData, String boardid) {
		  
		  
		  List<Community> searchList = null;
		  
		 
			  map.clear();
			  map.put("part", part);
			  map.put("searchData", "%"+searchData+"%");
			  map.put("boardid", boardid);
			  
			  searchList = sqlSession.selectList(NS+"comSearch",map);
	
		  
		  return searchList;
		  
	  }
	  
	  
	  
	  
	  
	  
	  
	  /* 메인화면(board) 최신글 출력 관련 Dao */
	   public List<Community> comMainBoardList(String boardid) {
               
           return sqlSession.selectList(NS+"comMainBoardList",boardid);
          
     }
	   
	   
	   
	   

	   
	   
	   
	   
	   public List<Community> comBoardmyList1(int pageInt, int limit, int boardcount, String nickname) {
			
					map.clear();					
					map.put("start", (pageInt-1)*limit+1);
					map.put("end", pageInt*limit);
					map.put("nickname", nickname);
	
				return sqlSession.selectList(NS+"comBoardmyList1",map);
				
		  }
	   
}
