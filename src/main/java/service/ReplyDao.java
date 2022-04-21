package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Reply;
import util.MybatisConnection;

@Component
public class ReplyDao {
	
	private static final String NS = "reply.";
	 // private Map<String, Object> map = new HashMap<>();
	
	@Autowired
	MySqlSessionFactory sqlSessionFactory;
	SqlSession sqlSession;
	
	@PostConstruct
	public void setSqlSession() {
		this.sqlSession = sqlSessionFactory.sqlmap.openSession();
	}
			
	
	
	
	  
	  /*댓글등록*/
	  public int insertReply(Reply reply) {
		 
			try {
			return sqlSession.update(NS+"insertReply", reply);	
			} catch(Exception e ) {
				e.printStackTrace();
			} finally {
				sqlSession.commit();
			}
			return 0;
			}
	  
	  
	  public int replyNextNum() {
		  
			  return sqlSession.selectOne(NS+"replyNextNum");
		  
	  }
	  
	  
	  
	  public List<Reply> replyWriteList(int board_num) {
		 
			  return sqlSession.selectList(NS+"replyWriteList", board_num);
		  
	  }
	  
	  
	  public int replyCount(int board_num) {
		  
		  	return sqlSession.selectOne(NS+"replyCount", board_num);
		  	  
		  
	  }
	  
	  public int deleteReply(int reply_num) {
		 
		  try {
			  return sqlSession.update(NS+"deleteReply",reply_num);
		  } catch(Exception e) {
				 e.printStackTrace();
		  } finally {
				sqlSession.commit();
		  }
			  return 0;
		  
	  }
	  
	  public Reply replyOne(int reply_num) {
		
		  return sqlSession.selectOne(NS+"replyOne",reply_num);
		
	  }
	  
	  public int comReplyCountUp(int board_num) {
		  
		
		  try {
			  int communityTable = sqlSession.update(NS+"comReplyCountUp", board_num);
			  int groupTable = sqlSession.update(NS+"groupReplyCountUp", board_num);
			  return communityTable+groupTable;
		  } catch(Exception e) {
			  e.printStackTrace();
		  } finally {
			  sqlSession.commit();
		  }
		  return 0;		  
		  
	  }
	  
	  
		  
}
	 

