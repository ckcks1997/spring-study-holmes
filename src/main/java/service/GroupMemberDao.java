package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.GroupMember;
import model.group.GroupInList;
import util.MybatisConnection;

@Component
public class GroupMemberDao {

	private static final String NS = "groupmember.";
	  private Map<String, Object> map = new HashMap<>();
	 
	  
	  
	  @Autowired
		MySqlSessionFactory sqlSessionFactory;
		SqlSession sqlSession;
		
		@PostConstruct
		public void setSqlSession() {
			this.sqlSession = sqlSessionFactory.sqlmap.openSession();
		}
	  
	  
	   public int groupCount(String id) {
	      
           return sqlSession.selectOne(NS+"groupCount", id);
     }
 
	   
	   
	    public int groupInsert(GroupMember sm, int represent) {
          
	           try {
	             map.clear();
	             map.put("board_num", sm.getBoardnum());
                 map.put("nickname", sm.getNickname());
	             map.put("represent", represent); 
	           return sqlSession.insert(NS+"groupInsert", map);
	           } catch (Exception e) {
	               e.printStackTrace();
	           } finally {
	               sqlSession.commit();
	           } 
	           return 0;
	     }
	    
	    
	    
	       public List<GroupInList> groupInList(String nickname) {
	          
             SqlSession sqlSession = MybatisConnection.getConnection();
               try {
               return sqlSession.selectList(NS+"groupInList", nickname);
               } catch (Exception e) {
                   e.printStackTrace();
               } finally {
                   MybatisConnection.close(sqlSession);
               } 
               return null;
         }
	       
	       public List<GroupMember> groupListByBoardnum(String id) {
	            
	           return sqlSession.selectList(NS+"groupListByBoardnum", id);
	     }
	       
	        public int groupDelete(int boardnum, String nickname) {
	          
	               try {
	                 map.clear();
	                 map.put("boardnum", boardnum);
	                 map.put("nickname", nickname); 
	               return sqlSession.delete(NS+"groupDelete", map);
	               } catch (Exception e) {
	                   e.printStackTrace();
	               } finally {
	                  sqlSession.commit();
	               } 
	               return 0;
	         }
	   
	        
	 	   public int isMemberInGroup(String boardid, String memid) {
		        
	 	           try {
	 	        	   map.clear();
	 	        	   map.put("boardnum", boardid);
	 	        	   map.put("nickname", memid);
	 	           return sqlSession.selectOne(NS+"isMemberInGroup", map);
	 	           } catch (Exception e) {
	 	               e.printStackTrace();
	 	           } finally {
	 	              sqlSession.commit();
	 	           } 
	 	           return 0;
	 	     }
}