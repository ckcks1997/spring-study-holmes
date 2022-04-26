package controller;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import model.Attend;
import model.Community;
import model.GroupBoard;
import model.GroupMember;
import model.Reply;
import model.ReputationEstimate;
import model.StudyMember;
import model.StudyMenu;
import model.group.GroupInList;
import service.AttendDao;
import service.CommunityBoardDao;
import service.GroupBoardDao;
import service.GroupMemberDao;
import service.ReplyDao;
import service.ReportDao;
import service.ReputationEstimateDao;
import service.StudyMemberDao;
import service.StudyMenuDao;

//group
@Controller
@RequestMapping("/group/")
public class GroupStudyController {

	HttpServletRequest request;
	Model m;
	HttpSession session;
	@Autowired
	StudyMenuDao mud;
	@Autowired
	GroupMemberDao gmd;
	@Autowired
	ReplyDao rd;
	@Autowired
	ReportDao report_d;
	@Autowired
	ReputationEstimateDao red; 
	@Autowired
	StudyMemberDao sd;
	@Autowired
	GroupBoardDao gbd;
	@ModelAttribute
	void init(HttpServletRequest request, Model m) {
		this.request = request;
		this.m = m;
		this.session = request.getSession();
	}
	
	
  @RequestMapping("studylist")
  public String studyList(Model model) {
    
    String nickname = (String) session.getAttribute("memberNickname");
    
    String msg= "로그인이 필요합니다";
    String url= "redirect:/studymember/loginForm"; //main으로 보내기, alert.jsp파일 참고
    
    if(nickname != null) {  
      List<GroupInList> list = gmd.groupInList(nickname);
      model.addAttribute("list", list);
      return "/view/group/groupStudyList";
    }
    
    model.addAttribute("msg", msg); 
     
    return url;
  }
  
  @RequestMapping("studyinfo")
  public String studyInfo(@RequestParam String boardnum, Model model) {
    
    String nickname = (String) session.getAttribute("memberNickname"); 
    
    String msg= "로그인이 필요합니다";
    String url= "redirect:/studymember/loginForm"; 
     
    if(nickname != null) {
       
      List<GroupMember> groupMemberList = gmd.groupListByBoardnum(boardnum);
      int total = groupMemberList.size();
       
      StudyMenu smenu = mud.menuBoardOne(Integer.parseInt(boardnum));
      
      model.addAttribute("groupMemberList", groupMemberList);
      model.addAttribute("total", total); 
      model.addAttribute("studyMenuInfo", smenu);
      return "/view/group/groupStudyInfo";
    }
    
    model.addAttribute("msg", msg); 
     
    return url;
  }
  
  /*그룹 나가기 페이지*/
  @RequestMapping("groupexit")
  public String groupExit(@RequestParam String boardnum, Model model) {
    
    String nickname = (String) session.getAttribute("memberNickname"); 
    
    String msg= "로그인이 필요합니다";
    String url= "redirect:/studymember/loginForm"; 
    
    if(nickname != null) {
      List<GroupMember> groupMemberList = gmd.groupListByBoardnum(boardnum);
      model.addAttribute("groupMemberList", groupMemberList);
      return "/view/group/groupStudyExit";
    }
    
    model.addAttribute("msg", msg); 
    
    return url;
  }
  
  /*평가 후 그룹 나가기*/
  @RequestMapping("groupexitpro")
  public String groupExitPro(@RequestParam String boardnum, Model model) {

	    String nickname = (String) session.getAttribute("memberNickname"); 
	    
	    String msg= "로그인이 필요합니다";
	    String url= "redirect:/studymember/loginForm"; 
	    
    if(nickname != null) { 
      System.out.println("================");
      gmd.groupDelete(Integer.parseInt(boardnum), nickname);
      msg= "스터디가 종료되었습니다.";
      url= "redirect:/board/main"; //main으로 보내기, alert.jsp파일 참고
    }
    model.addAttribute("msg", msg); 
    
    return url;
  }
  
  /*ajax 점수평가*/
  
  @ResponseBody
  @RequestMapping("score")
  public String score(@RequestParam("nickname") String nickname_to, 
		  @RequestParam String info_value,
		  @RequestParam("score_value") String score1,
		  @RequestParam String boardnum,
		  Model model) {
    
    String nickname = (String) request.getSession().getAttribute("memberNickname");
    int score = Integer.parseInt(score1); 
    
    if(nickname != null) {
      System.out.println(nickname_to);
      System.out.println(score);
      //평가정보 기록 
      ReputationEstimate re = new ReputationEstimate();
      re.setNickname_from(nickname);
      re.setNickname_to(nickname_to);
      re.setScore(score);
      re.setInfo(info_value);
      re.setGroup_num(Integer.parseInt(boardnum));
      red.insertReputation(re);
      
      //회원정보에 점수 추가 
      System.out.println(nickname_to+"==");
      StudyMember member = sd.studyMembeByNickname(nickname_to);
      int val = member.getPoint();
      val+=score;
      
      sd.changePoint(val, nickname_to); 
      return "1";
    }
    
    return "0";
  }
  
  /*그룹 게시판
   * 
   * 주의사항: boardnum과 관련하여 코드가 많이 꼬였습니다 ㅠㅠ..
   * 
   * 게시글 관련한 board_num(community, study, group board들)의 pk는 전부 같은 시퀀스(board_seq)를 공유하는데
   * group_member 자체가 Study_menu게시글의 board_num을 참고하고 있고
   * group_board의 경우 board_num시퀀스를 pk, Study_menu의 board_num을 속성으로 갖고 있어
   * 아래 코드에서도 두 board_num 속성이 혼용되고 있습니다 ㅠㅠ(whimsical을 확인해보시면 이해가 가실겁니다..)
   * 
   * 초기에는 두 board_num의 이름이 혼용되었지만
   * 이후 Study_menu(=group_member)의 board_num은 s_board_num으로 선언하여 처리하고 있으니 참고하세요..
   * 
   * */
  @RequestMapping("groupBoard")
  public String groupBoard(Model model) { 
    String nickname = (String) session.getAttribute("memberNickname");
    String boardnum = request.getParameter("boardnum"); //그룹번호(==Study_menu 게시글 번호)
    session.setAttribute("boardnum", boardnum);
    
     System.out.println(nickname + ":"+boardnum);
    String msg= "로그인이 필요합니다";
    String url= "redirect:/board/main"; //main으로 보내기, alert.jsp파일 참고
    
    if(nickname != null) { 
      int res = gmd.isMemberInGroup(boardnum, nickname); //그룹에 있는지 확인, 있다면 1
      System.out.println(res+"=12121");
      if(res > 0) { //있다면 그룹 글 조회
    	 
    	  
    	  String boardid = "";
		  int pageInt = 1;
		  int limit = 5;
		  
		  if(request.getParameter("boardid")!= null) {
			  session.setAttribute("boardid", request.getParameter("boardid"));	  
			  session.setAttribute("pageNum", "1");		  
		  }
		  
		  boardid = (String)session.getAttribute("boardid");
		  if(boardid == null) {
			  boardid ="1";
		  }
		  
		  if (request.getParameter("pageNum")!=null) {
			  session.setAttribute("pageNum", request.getParameter("pageNum"));
		  }
		  
		  String pageNum =(String)session.getAttribute("pageNum");
		  if(pageNum == null) {
			  pageNum = "1";
		  }
    	  
		  pageInt = Integer.parseInt(pageNum);
		  
		  int boardcount = gbd.groupBoardCount(boardnum, boardid);
		  System.out.println("boardcount="+boardcount);
		  
		  List<GroupBoard> list = gbd.groupBoardList(pageInt, limit, boardcount, boardid, boardnum);
		  int boardListnum = boardcount - limit * (pageInt-1);
		  int bottomLine = 3;
		  int startPage = (pageInt-1)/bottomLine * bottomLine + 1;
		  int endPage = startPage + bottomLine -1;
		  int maxPage = (boardcount/limit)+(boardcount % limit==0? 0:1);
		  if(endPage > maxPage) endPage = maxPage;
		  
		  String boardName = "질문 & 답변";
		  switch(boardid) {

		  case "1" : boardName = "질문 답변"; break;
		  case "2" : boardName = "자료공유"; break;
		
		  }
		   model.addAttribute("boardName",boardName);
		   model.addAttribute("pageInt",pageInt);
		   model.addAttribute("boardid",boardid);
		   model.addAttribute("boardcount",boardcount);
		   model.addAttribute("list",list);
		   model.addAttribute("boardListnum",boardListnum);
		   model.addAttribute("startPage",startPage);
		   model.addAttribute("bottomLine", bottomLine);
		   model.addAttribute("endPage",endPage);
		   model.addAttribute("maxPage",maxPage);
 
    	  return "/view/group/groupBoard";
      }
      
      msg= "권한이 없습니다.";
      url= "redirect:/board/main";
    }
    
    model.addAttribute("msg", msg); 
     
    return url;
  }
  
  /*게시판-페이징*/
  @RequestMapping("groupBoardList")
  public String groupBoardList(Model model) { 
    String nickname = (String) session.getAttribute("memberNickname"); 
    String boardnum = request.getParameter("boardnum"); //그룹번호(==스터디 게시글 번호) 
    if(boardnum!= null)
    	session.setAttribute("boardnum", boardnum);
    
    boardnum = (String) session.getAttribute("boardnum"); 
     System.out.println(nickname + ":"+boardnum);
    String msg= "로그인이 필요합니다";
    String url= "redirect:/board/main"; //main으로 보내기, alert.jsp파일 참고
    
    if(nickname != null) {
      int res = gmd.isMemberInGroup(boardnum, nickname); //그룹에 있는지 확인, 있다면 1
      if(res > 0) { //있다면 그룹 글 조회

    	  String boardid = "";
		  int pageInt = 1;
		  int limit = 5;
		  
		  if(request.getParameter("boardid")!= null) {
			  session.setAttribute("boardid", request.getParameter("boardid"));	  
			  session.setAttribute("pageNum", "1");		  
		  }
		  
		  boardid = (String)session.getAttribute("boardid");
		  if(boardid == null) {
			  boardid ="1";
		  }
		  
		  if (request.getParameter("pageNum")!=null) {
			  session.setAttribute("pageNum", request.getParameter("pageNum"));
		  }
		  
		  String pageNum =(String)session.getAttribute("pageNum");
		  if(pageNum == null) {
			  pageNum = "1";
		  }
    	  
		  pageInt = Integer.parseInt(pageNum);
		   
		  int boardcount = gbd.groupBoardCount(boardnum, boardid);
		  System.out.println("boardcount="+boardcount);
		  
		  List<GroupBoard> list = gbd.groupBoardList(pageInt, limit, boardcount, boardid, boardnum);
		  int boardListnum = boardcount - limit * (pageInt-1);
		  int bottomLine = 3;
		  int startPage = (pageInt-1)/bottomLine * bottomLine + 1;
		  int endPage = startPage + bottomLine -1;
		  int maxPage = (boardcount/limit)+(boardcount % limit==0? 0:1);
		  if(endPage > maxPage) endPage = maxPage;
 
		  
		  String boardName = "질문 & 답변";
		  switch(boardid) {

		  case "1" : boardName = "질문 답변"; break;
		  case "2" : boardName = "자료공유"; break;
		
		  }
		   model.addAttribute("boardName",boardName);
		   model.addAttribute("pageInt",pageInt);
		   model.addAttribute("boardid",boardid);
		   model.addAttribute("boardcount",boardcount);
		   model.addAttribute("list",list);
		   model.addAttribute("boardListnum",boardListnum);
		   model.addAttribute("startPage",startPage);
		   model.addAttribute("bottomLine", bottomLine);
		   model.addAttribute("endPage",endPage);
		   model.addAttribute("maxPage",maxPage);
 
    	  return "/view/group/groupBoard";
      }
      
      msg= "권한이 없습니다.";
      url= "redirect:/board/main";
    }
    
    model.addAttribute("msg", msg); 
    return url;
  }
  
  /*글쓰기*/
  @RequestMapping("groupBoardWriteForm")
  public String groupWriteForm(Model model) {
	   
	  String msg = "로그인이 필요합니다";
	  String url ="redirect:/board/main";
	  
	  if(session.getAttribute("memberNickname")!= null) {
		   String paramid = request.getParameter("boardid");
		   if(paramid != null)
			   session.setAttribute("boardid", paramid);
		return "/view/group/groupBoardWriteForm";
	  }
	  
	  model.addAttribute("msg", msg);
	  model.addAttribute("url", url);
	  
	  return url;
  }
 
  /*글쓰기-진행*/
  @RequestMapping("groupBoardWritePro")
  public String groupBoardWritePro(Model model) {
	  
	  HttpSession session = request.getSession();
	  String msg = "로그인이 필요합니다";
	  String url = request.getContextPath()+"/studymember/loginForm";
	  
	  if(session.getAttribute("memberNickname")!= null) {
 
		String boardid = (String) session.getAttribute("boardid"); //게시판목록
		String boardnum = (String) session.getAttribute("boardnum");//그룹번호
		String memberNickname = (String) session.getAttribute("memberNickname"); 
		String title = (String) request.getParameter("title");
		String content = (String) request.getParameter("content");
		
		GroupBoard gb = new GroupBoard();
		
		gb.setBoardid(boardid);
		gb.setS_board_num(Integer.parseInt(boardnum));
		gb.setTitle(title);
		gb.setContent(content);
		gb.setNickname(memberNickname);
		
		int res = gbd.groupInsertBoard(gb);
		System.out.println("result="+res);
		return "redirect:/group/groupBoard?boardnum="+boardnum+"&pageNum=1";
	  }
	  
	  model.addAttribute("msg", msg); 
	  
	  return url;
  }
  
  //게시글 상세보기
  @RequestMapping("groupBoardInfo")
  public String groupBoardInfo(HttpServletRequest request, HttpServletResponse response) {
	  HttpSession session = request.getSession();
	  String s_board_num = (String) session.getAttribute("boardnum");
	  String board_num = request.getParameter("board_num");
	  
	  GroupBoard gb = gbd.groupBoardOne(s_board_num, board_num);
	  gbd.groupReadCountUp(Integer.parseInt(board_num));
	  
	  //댓글보여주기

	  List<Reply> reply_list = rd.replyWriteList(Integer.parseInt(board_num));
	  int reply_count = rd.replyCount(Integer.parseInt(board_num));
	  
	  request.setAttribute("reply_list", reply_list);
	  request.setAttribute("reply_count", reply_count);
	  
	  request.setAttribute("groupBoard", gb); 
	  
	  return "/view/group/groupBoardInfo";

	  
  }

  /*이미지 업로드*/
	@RequestMapping("imageUpload")                                             
	public String imageUpload(HttpServletRequest request, HttpServletResponse response) {
 
		String path = request.getServletContext().getRealPath("/")+"upload";
		String fileName = "";
	    //폴더가 없으면 에러남, 검사 후 폴더생성
	    File file=new File(path);
	    if(!file.exists()) { 
	      file.mkdir();
	    }
	    
	    String filename = null;
	    MultipartFile multi = null;
	    /*
	    try {
	      multi = new MultipartFile(request, path, 10*1024*1024, "utf-8");
	    } catch (IOException e) { 
	      e.printStackTrace();
	    }
	     
	    filename = multi.getFilesystemName("file");
	    */
	    System.out.println("filename="+filename);
	    request.setAttribute("filename", filename);
	    return "/single/picturePro2.jsp";
	}
	
  //게시글 수정페이지
	  @RequestMapping("groupBoardUpdateForm")
	  public String comBoardUpdateForm(HttpServletRequest request,  HttpServletResponse response) {
		  
		  HttpSession session = request.getSession();
		  String msg = "로그인이 필요합니다";
		  String url = request.getContextPath()+"/studymember/loginForm";
		  
		  if(session.getAttribute("memberNickname")!= null) {
			  
			  
		 	  int board_num = Integer.parseInt(request.getParameter("board_num"));
		 	  System.out.println(board_num+"==");
		 	  GroupBoardDao gbd = new GroupBoardDao();
		 	  GroupBoard gb = gbd.groupBoardOne2(board_num);
		 	  
		 	  if(gb.getNickname().equals(session.getAttribute("memberNickname") )){
			 	  request.setAttribute("gb", gb);
			 	  return "/view/group/groupBoardUpdateForm.jsp";
		 	  }
		  }
		  
	      msg= "권한이 없습니다.";
	      url= "main";

	    request.setAttribute("msg", msg);
	    request.setAttribute("url", url);
	     
	    return "/view/alert.jsp";  
	    }
	  
	  //게시글 수정
	  @RequestMapping("groupBoardUpdatePro")
	  public String comBoardUpdatePro(HttpServletRequest request, HttpServletResponse response) {
		  
		  String path = request.getServletContext().getRealPath("/")+"/comboardupload/";
		  /*
		  int size = 10*10*1024;
		  MultipartRequest multi = null;
		  try {
			multi = new MultipartRequest(request, path, size, "utf-8");
		  } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		  GroupBoard com = new GroupBoard();
		  com.setboard_num(Integer.parseInt(multi.getParameter("board_num")));
		  com.setTitle(multi.getParameter("title"));
		  com.setContent(multi.getParameter("content"));
		  
		  GroupBoardDao cbd = new GroupBoardDao();
		  
		  String msg = "";
		  String url = "";
		  
		
		  
		  //Community newcom = cbd.comBoardOne(com.getNum());
		  if(cbd.groupBoardUpdate(com)>0) {
			   msg = "수정되었습니다";
			   url = request.getContextPath()+"/group/groupBoardInfo?board_num="+com.getboard_num();
			 
		  } else {
			  msg = "수정이 실패하였습니다";
		  }
		 
		  
		  request.setAttribute("msg", msg);
		  request.setAttribute("url", url);
		 */
		  
		  return "/view/alert.jsp";
		  
	  }
	  
}
