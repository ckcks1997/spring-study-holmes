package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Community;
import model.GroupBoard;
import util.MybatisConnection;

@Component
public class GroupBoardDao {

	private static final String NS = "groupBoard.";
	  private Map<String, Object> map = new HashMap<>();
	
	  
	  @Autowired
	  MySqlSessionFactory sqlSessionFactory;
	  SqlSession sqlSession;
	  
	  @PostConstruct
	  public void setSqlSession() {
		  this.sqlSession = sqlSessionFactory.sqlmap.openSession();
	  }
	  
	  public int groupBoardCount(String boardnum, String boardid) {	
			try {
				map.clear();
				map.put("boardnum", boardnum);
				map.put("boardid", boardid);  
			return sqlSession.selectOne(NS+"groupBoardCount", map);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			} 
			return 0;
	  }
	  
	  public List<GroupBoard> groupBoardList(int pageInt, int limit, int boardcount, String boardid, String s_board_num) {
		  

				map.clear();
				map.put("boardid", boardid);
				map.put("s_board_num", s_board_num);
				map.put("start", (pageInt-1)*limit+1);
				map.put("end", pageInt*limit);
					
			return sqlSession.selectList(NS+"groupBoardList",map);
			
	  }
	  
	  public int groupInsertBoard(GroupBoard gbd) {
			
			try {
			return sqlSession.insert(NS+"groupInsertBoard", gbd);	
			} catch(Exception e ) {
				e.printStackTrace();
			} finally {
				sqlSession.commit();
			}
			return 0;
			}
	 
 
	  public GroupBoard groupBoardOne(String s_board_num, String boardid) {

			map.clear(); 
			map.put("boardid", boardid);
			map.put("s_board_num", s_board_num); 
					
			return sqlSession.selectOne(NS+"groupBoardOne",map);
		
	  }
	  
	  public GroupBoard groupBoardOne2(int board_num) {

			return sqlSession.selectOne(NS+"groupBoardOne2",board_num);
			
	  } 
	  /*조회수 증가*/
	  public void groupReadCountUp(int board_num) {
		  SqlSession sqlSession = MybatisConnection.getConnection();
			 try {
			  sqlSession.update(NS+"groupReadCountUp",board_num);
			 } catch(Exception e) {
				 e.printStackTrace();
			 } finally {
				 MybatisConnection.close(sqlSession);
			 }
		  
	  }
	  
	  
	  public int groupBoardUpdate(GroupBoard gb) {
		 
		  try {
		  return sqlSession.update(NS+"groupBoardUpdate", gb);
		  } catch (Exception e) {
			  e.printStackTrace();
		  } finally {
			  sqlSession.commit();
		  }
		  
		  return 0;
		  
	  }
}
 