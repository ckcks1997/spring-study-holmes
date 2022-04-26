package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Attend;

@Component
public class AttendDao {

	private static final String NS = "attend.";
	  private Map<String, Object> map = new HashMap<>();
	 
	  @Autowired
	  MySqlSessionFactory sqlSessionFactory;
	  SqlSession sqlSession;
	  
	  @PostConstruct
	  public void setSqlSession() {
		  this.sqlSession = sqlSessionFactory.sqlmap.openSession();
	  }
	  
	 
	  
	   public List<Attend> attendGet(String id) {

           return sqlSession.selectList(NS+"attendGet", id);
        
     }
	  
	   
	  public int attendInsert(Attend attend) {
	

			try {
		return sqlSession.insert(NS+"attendInsert",attend);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

    	sqlSession.commit();

			} 
			return 0;
	  }
	  
	   
	  

}

