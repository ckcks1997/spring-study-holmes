package controller;

import java.util.List;

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
	
	@ResponseBody
	@RequestMapping("writeReply")
	public String writeReply(@RequestBody Reply reply) {
		
		String nickname = (String) session.getAttribute("memberNickname");
		reply.setNickname(nickname);
		
		int reply_num = rd.replyNextNum();
		
		rd.insertReply(reply); //댓글 저장하기
		
		//원글의 replycnt에 +1하기 
		rd.comReplyCountUp(Integer.parseInt(board_num));
		System.out.println("board_num:" + board_num);		

		//reply_num은 댓글의 번호
		m.addAttribute("reply_num", reply_num);
		
		
		return "single/num";	
	}
	
	@RequestMapping("deleteReply")
	public String deleteReply(int reply_num) {
		
		System.out.println(reply_num);
		
		Reply reply =  rd.replyOne(reply_num);
		m.addAttribute("reply", reply);
		rd.deleteReply(reply_num);
	
		return "view/alert";
	}
	
	

} // end class
