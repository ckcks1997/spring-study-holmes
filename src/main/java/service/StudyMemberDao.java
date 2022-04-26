package service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.StudyMember;
import util.MybatisConnection;
 
@Component
public class StudyMemberDao {

  private static final String NS = "studymember.";
  private Map<String, Object> map = new HashMap<>();

  @Autowired
  MySqlSessionFactory sqlSessionFactory;
  SqlSession sqlSession;
  
  @PostConstruct
  public void setSqlSession() {
	  this.sqlSession = sqlSessionFactory.sqlmap.openSession();
  }

  public StudyMember studyMemberOne(String id) {

      return sqlSession.selectOne(NS + "studyMemberOne", id);

  }
   
  public StudyMember studyMemberOneByNick(String id) {
	  
      return sqlSession.selectOne(NS + "studyMemberOneByNick", id);
  }
  
  public StudyMember studyMembeByNickname(String id) {
	  
      return sqlSession.selectOne(NS + "studyMembeByNickname", id);
    
  }
  
   
  public int insertStudyMember(HttpServletRequest req) {

    try {

      StudyMember m = new StudyMember();
      m.setEmail(req.getParameter("email"));
      m.setName(req.getParameter("name"));
      m.setNickname(req.getParameter("nickname"));
      m.setPassword(req.getParameter("password"));
      m.setTel(req.getParameter("tel"));
      m.setPicture(req.getParameter("picture"));
      
      System.out.println(m);
      
      return sqlSession.insert(NS + "insertStudyMember", m );
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    	sqlSession.commit();
    }
    return 0;
    
  }
  public int studyMemberIdExist(String id) {
    
	  return sqlSession.selectOne(NS + "studyMemberIdExist", id);
    
  }
  
  public int studyMemberNicknameExist(String nickname) {
	  
      return sqlSession.selectOne(NS + "studyMemberNicknameExist", nickname);
    
  }
  
  public int studyMemberIntroUpdate(String email, String intro) {
    
    try {
      map.clear();
      map.put("email", email);
      map.put("intro", intro);
      return sqlSession.update(NS + "studyMemberIntroUpdate", map);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    	sqlSession.commit();
    }

    return 0;
  }
  
  public int studyMemberDelete(String email) {
    
    try {
      return sqlSession.update(NS + "studyMemberDelete", email);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    	sqlSession.commit();
    }

    return 0;
  }
  
  public int changePassword(String email, String newPass) {
    
    try {
      map.clear();
      map.put("email", email);
      map.put("newPass", newPass);
      return sqlSession.update(NS + "changePassword", map);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    	sqlSession.commit();
    }

    return 0;
  }
  
  
  public int changePoint(int point, String nickname) {
    
    try { 
      map.clear();
      map.put("point", point);
      map.put("nickname", nickname);
      return sqlSession.update(NS + "changePoint", map);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    	sqlSession.commit();
    }

    return 0;
  }
  
 public StudyMember getPoint(String nickname) {
     
    try {  
      return sqlSession.selectOne(NS + "getPoint", nickname); 
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    	sqlSession.commit();
    }

    return null;
  }
}