package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import model.Attend;
import service.AttendDao;

@Controller
@RequestMapping("/attend/")

public class AttendCheckController  {

	HttpServletRequest request;
	Model m;
	HttpSession session;
	
	@Autowired
	AttendDao ad;
	
	
	@ModelAttribute
	void init(HttpServletRequest request, Model m) {
		this.request = request;
		this.m = m ;
		this.session = request.getSession();
		}
	
  @RequestMapping("check")
  public String ckeck(HttpServletRequest request) {
    
    String id = (String) request.getSession().getAttribute("memberID");
    
    String msg= "로그인이 필요합니다";
    String url= "main"; //main으로 보내기, alert.jsp파일 참고
    
    if(id != null) {
      
      List<Attend> result = ad.attendGet(id);
      m.addAttribute("result", result);
      return "/view/check/event";
    }
    
    m.addAttribute("msg", msg);
    m.addAttribute("url", url);
     
    return "/view/alert";
  }
 
  @RequestMapping("sendGift")
  public String sendGood(HttpServletRequest request) {
    
    String id = (String) request.getSession().getAttribute("memberID");
    String gift = request.getParameter("gift");
    System.out.println(gift);
     m.addAttribute("num", gift);
    return "/single/alert_ajax";
  }
  
}
