package controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import model.Community;
import model.Reply;
import service.ReplyDao;


@Controller
@RequestMapping("/reply/")
public class ReplyController {
	
	HttpServletRequest request;
	Model m;
	HttpSession session;
	
	@Autowired
	ReplyDao rd;

	@ModelAttribute
	void init(HttpServletRequest request, Model m) {
		this.request = request;
		this.m = m;
		this.session = request.getSession();
	}
	
	//@ResponseBody// 자바 객체를 http바디로 보냄 
	
	@RequestMapping("writeReply")
	public String writeReply(@RequestBody Map<String, String> rep, Reply reply) { //RequestBody : http요청 바디내용을 자바객체로 받음
		
		String nickname = (String) session.getAttribute("memberNickname");
		reply.setNickname(nickname); //닉네임
		//프론트에서 보낸 ajax데이터
		reply.setContent(rep.get("reply_content"));
		reply.setBoard_num(Integer.parseInt(rep.get("board_num")));
		
		int reply_num = rd.replyNextNum(); 
		reply.setReply_num(reply_num);
		
		rd.insertReply(reply); //댓글 저장하기
		//reply_num은 댓글의 번호
		m.addAttribute("reply", reply);
		
		
		return "single/num";	
	}
	
	@RequestMapping("deleteReply")
	public String deleteReply(@RequestBody Map<String, String> rep, Reply reply) {
		
		int reply_num = Integer.parseInt((rep.get("reply_num")));
		System.out.println("-----삭제 ajax값"+reply_num);
		
		reply =  rd.replyOne(reply_num);
		m.addAttribute("reply", reply);
		rd.deleteReply(reply_num);
	
		return "view/alert";
	}
	
	

} // end class
