package controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import model.Community;
import model.GroupMember;
import model.MemberTag;
import model.Notice;
import model.Report;
import model.ReputationEstimate;
import model.StudyMember;
import model.StudyMenu;
import service.CommunityBoardDao;
import service.GroupMemberDao;
import service.NoticeDao;
import service.ReportDao;
import service.ReputationEstimateDao;
import service.StudyMemberDao;
import service.StudyMenuDao;


@Controller  
@RequestMapping("/studymember/")
public class StudyMemberController {

	@Autowired
	StudyMemberDao md;
	@Autowired
	StudyMenuDao mud;
	@Autowired
	CommunityBoardDao cbd;
	@Autowired
	GroupMemberDao gmd;
	@Autowired
	NoticeDao nd;
	@Autowired
	ReportDao rd;
	@Autowired
	ReputationEstimateDao red;

	HttpServletRequest request;
	Model m;
	HttpSession session;
	
	@ModelAttribute
	void init(HttpServletRequest request, Model m) {
		this.request = request;
		this.m = m;
		this.session = request.getSession();
	}
	

  /*
   * 알림 페이지
   * */
  @RequestMapping("notice")
  public String memberNotice(Model model) {

    String id = (String) session.getAttribute("memberNickname");
    String msg = "로그인이 필요합니다";
    String url = "/studymember/loginForm";
    if(id != null) {
      List<Notice> noticeList = nd.noticeGet(id); //알림 리스트 가져옴
      model.addAttribute("noticeList", noticeList);
      session.setAttribute("noticeCount", 0);
      return "/view/member/memberNotice";      
    }
    model.addAttribute("msg", msg);  
    return "redirect:"+url;
  }
  
  /*
   * 알림 상세
   * */
  @RequestMapping("noticeInfo")
  public String noticeInfo(Model model) {

	  System.out.println("================");
    String id = (String) session.getAttribute("memberNickname");
    String msg = "로그인이 필요합니다";
    String url = "/studymember/loginForm";
    
    if(id != null) { //현재 로그인 된 유저라면 
      int noticeNum = Integer.parseInt(request.getParameter("noticeNum"));
      
      //noticeNum에 해당하는 notice정보 가져오기
      Notice n = nd.noticeGetByNoticeNum(noticeNum);
      if(n.getNickname_to().equals(id)) { //알림의 수신자와 현재 닉네임이 같으면
      
    	  if(n.getInfo() == null) {//Info이 없으면 Info2 이용해서 title 채워보내기
    		   
    		  StudyMenu menu = mud.menuBoardOne(Integer.parseInt(n.getInfo2()));
    		  String title = menu.getTitle();  
    		  model.addAttribute("title", title);
         
    	  }  else { //Info가 있으면
    		  //신고사유 가져오기
    		  int board_num = Integer.parseInt(n.getInfo2());
    		  List<String> reportReason = rd.reportReason(board_num);
    		  model.addAttribute("reportReason", reportReason);
    		  
    		  //report테이블에서 원 글 정보가져오기 
    		  Report report = rd.reportOne(board_num);
    		  model.addAttribute("report", report);

    	  }
      // notice 보내기
     
    	  model.addAttribute("notice", n);  
    	  return "/view/member/memberNoticeInfo";
      }      
    }
    model.addAttribute("msg", msg);  
    return "redirect:"+url;
  }
  
  /*
   * 알림-그룹초대 수락
   * */
  @RequestMapping("groupAccept")
  public String groupAccept(Model model) {

    String id = (String) session.getAttribute("memberNickname");
 
   
    String msg = "로그인이 필요합니다";
    String url = "/studymember/loginForm";
    if(id != null) { 
      
      int noticeNum = Integer.parseInt(request.getParameter("notice_num")); //알림번호 가져옴
      Notice n = nd.noticeGetByNoticeNum(noticeNum); //알림정보 조회
      StudyMenu menu = mud.menuBoardOne(Integer.parseInt(n.getInfo2())); //알림정보에 있는 스터디 보드번호로 보드조회
 
      if(n.getNickname_to().equals(id)) { //세션과 알림 받은사람 비교, 본인확인
        //group에 초대하는 과정
        GroupMember gm = new GroupMember();
        gm.setBoardnum(menu.getBoard_num());
        gm.setNickname(n.getNickname_from()); 
        gmd.groupInsert(gm, 0);
        
        
        /*TODO: 스터디에 참가한 사람에게 알림 전송*/
        
        
        msg = "등록되었습니다";
        url = "/studymember/notice";
         
      }      
    }

    model.addAttribute("msg", msg);  
    return "redirect:"+url;
  }
  /*
   * 로그인 페이지
   * */
  @RequestMapping("loginForm")
  public String memberloginForm() {

    return "view/member/login";
  }
  
  /*
   * 비밀번호 찾기
   * */
  @RequestMapping("findPassword")
  public String memberfindPassword(Model model) {
    
    String msg = "기능개발진행중..";
    String url = "/studymember/loginForm";
    
    model.addAttribute("msg", msg);  
    return "redirect:"+url;
  }

  /*
   * 로그인 과정
   * 
   * 로그인 성공시에는 memberID(이메일)와 memberNickname(닉네임, 이름x!!),memberPicture 3가지 세션이 생성됩니다.
   * */
  
  @PostMapping("loginPro")
  public String memberloginPro(Model model) {

    String id = request.getParameter("id"); // email
    String pass = request.getParameter("password"); 
     
    StudyMember mem = md.studyMemberOne(id);
    String msg = "아이디를 확인하세요";
    String url = "/studymember/loginForm";
    if (mem != null) {
      if (pass.equals(mem.getPassword())) {
        request.getSession().setAttribute("memberID", mem.getEmail());
        request.getSession().setAttribute("memberNickname", mem.getNickname());
        request.getSession().setAttribute("memberPicture", mem.getPicture());
        msg = "";
        url = "/board/main";
      } else {
        System.out.println(mem);
        msg = "비밀번호 확인하세요";
      }
    } 
    model.addAttribute("msg", msg);  
    return "redirect:"+url;
  }
  
  /*
   * 카카오 로그인
   * */
  @RequestMapping("kakaologin")
  public String kakaoLogin(Model model) {
     
    String kakaoemail = request.getParameter("kakaoemail"); // email
    System.out.println(kakaoemail+"=="); 
    
    StudyMember member = md.studyMemberOne(kakaoemail);
    System.out.println(member+"===");
    if(member == null) {
      request.setAttribute("kakaoemail", kakaoemail);
      return "/view/member/join";
    }
    
    request.getSession().setAttribute("memberID", member.getEmail());
    request.getSession().setAttribute("memberNickname", member.getNickname());
    request.getSession().setAttribute("memberPicture", member.getPicture());
     
    String msg = "";
    String url =  "/board/main"; 
    model.addAttribute("msg", msg);  
    return "redirect:"+url;
  }
  
  /*
   * 로그아웃-세션 삭제
   * */
  @RequestMapping("logout")
  public String logout() {
	  
    HttpSession session = request.getSession();
    String login = (String) session.getAttribute("memberNickname");
    session.invalidate();
    request.setAttribute("msg", "");
    String url = "/board/main";
    
    return "redirect:"+url;
  }
  
  
    /*회원가입*/
  
  /*
   * 회원가입 페이지
   * */
  @RequestMapping("join")
  public String memberJoin() {

    if(request.getSession().getAttribute("memerNickname") == null) {      
      return "/view/member/join";
    }
     
    String url ="/board/main"; 

    return "redirect:"+url;
  }

  /*
   * 회원가입 진행
   * 
   * */
  @RequestMapping("joinPro")
  public String memberJoinPro(Model model) {

    int result = md.insertStudyMember(request);
    
    String msg = "가입 실패";
    String url = "/studymember/loginForm";
    
    if(result == 1) msg="가입 성공";
    
    model.addAttribute("msg", msg); 
    return "redirect:"+url;
  }
  /*
   * id중복체크
   * */
  @ResponseBody
  @RequestMapping("idexist")
  public int idExist() {

    String id = request.getParameter("id");  
    System.out.println("id="+id); 
    int mem = md.studyMemberIdExist(id); 
    System.out.println("result="+mem);
    request.setAttribute("chk", mem); 
    return mem;
  }
  
  /*
   * 닉네임 중복확인
   * */
  @ResponseBody
  @RequestMapping(value="nicknameExist") //@RequestParam: GET방식에서 파라미터 값 가져옴.
  										//@RequestBody: POST방식에서 가져옴
  public String nicknameExist(@RequestBody String nickname) {  
    int mem = md.studyMemberNicknameExist(nickname); 
    System.out.println("result="+mem); 
      
    return Integer.toString(mem);
  }
  /*
   * 회원가입 내 사진등록 창
   * */
  @RequestMapping("pictureForm")
  public String pictureForm() {

    return "/single/pictureForm.jsp";
  }
  
  /*
   * 회원가입 내 사진등록 진행
   * */
  @RequestMapping("picturePro")
  public String picturePro(HttpServletRequest request, HttpServletResponse response) {
    
    String path = request.getServletContext().getRealPath("/")+"upload";
    
    //폴더가 없으면 에러남, 검사 후 폴더생성
    File file=new File(path);
    if(!file.exists()) { 
      file.mkdir();
    }
    
    String filename = null;
    MultipartRequest multi = null;
    try {
      multi = new MultipartRequest(request, path, 10*1024*1024, "utf-8");
      System.out.println("asdasd===1");
    } catch (IOException e) { 
      e.printStackTrace();
    }
    filename = multi.getFilesystemName("picture");
    request.setAttribute("filename", filename);
    return "/single/picturePro.jsp";
  }
  
  
 
  
  
  
  
  
  
  /*
   * 마이페이지
   * */
  @RequestMapping("mypage")
  public String mypage(HttpServletRequest request, HttpServletResponse response) {
    
    HttpSession session = request.getSession();
    String msg="로그인이 필요합니다";
    String url="studymember/loginForm";
    
    if(session.getAttribute("memberID") != null) {
      String memberID = (String) session.getAttribute("memberID"); 
      StudyMember mem = md.studyMemberOne(memberID);
      request.setAttribute("memberInfo", mem);
    //유저 평판
      List<ReputationEstimate> repList = red.getReputation(mem.getNickname());
      request.setAttribute("repList", repList);
    }

    request.setAttribute("msg", msg);
    request.setAttribute("url", url);
    return "/view/member/mypage.jsp";
  }
  
    /*내  프로필 정보*/
  
  /*
   * 내 프로필 정보
   * */
  @RequestMapping("myprofile")
  public String myprofile(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession();
    String msg="로그인이 필요합니다";
    String url="studymember/loginForm";
    
    if(session.getAttribute("memberID") != null) {
      String memberID = (String) session.getAttribute("memberID"); 
      
      
      StudyMember mem = md.studyMemberOne(memberID);
     
      request.setAttribute("memberInfo", mem);
      
      return "/view/member/myprofile.jsp";
    }
    
    request.setAttribute("msg", msg);
    request.setAttribute("url", url);
     
    return "/view";
  }
  
  /*
   * 내 프로필 정보-자기소개 수정칸
   * */
  @RequestMapping("myprofileEdit1")
  public String myprofileEdit1(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession();
    String msg="로그인이 필요합니다";
    String url="studymember/loginForm";
    if(session.getAttribute("memberID") != null) {
      String s_id = (String)request.getSession().getAttribute("memberID");
      String profile_intro = (String) request.getParameter("profile_intro");
 
      int result = md.studyMemberIntroUpdate(s_id, profile_intro);

      if(result == 1) {
        msg="수정되었습니다";
        url="studymember/myprofile";
      }
    }
    request.setAttribute("msg", msg);
    request.setAttribute("url", url);
     
    return "/view";
  }
  
 
  
  
  /*
   * 비밀번호 변경
   * */
  @RequestMapping("passwordChange")
  public String passwordChange(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(); 
    String msg="로그인이 필요합니다";
    String url="studymember/loginForm";
    
    if(session.getAttribute("memberID") != null) {
      return "/view/member/passwordChange.jsp"; 
    }
    
    request.setAttribute("msg", msg);
    request.setAttribute("url", url);
    return "/view";

  }
  
  /*
   * 비밀번호 변경 진행
   * */
  @RequestMapping("passwordChangePro")
  public String passwordChangePro(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(); 
    
    String newPass = request.getParameter("password");
    
    String msg="오류가 발생했습니다.";
    String url="studymember/loginForm";
    
    if(session.getAttribute("memberID") != null && !newPass.isEmpty()) {
      String s_id = (String)request.getSession().getAttribute("memberID");
       
      int result = md.changePassword(s_id, newPass); 
      msg="비밀번호가 변경 되었습니다.";
      url="studymember/myprofile";
    }  
    
    request.setAttribute("msg", msg);
    request.setAttribute("url", url);
    return "/view";

  }
  
  /*
   * 회원탈퇴
   * */
  @RequestMapping("goodbye")
  public String goodBye(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(); 
    String msg="로그인이 필요합니다";
    String url="studymember/loginForm";
    
    if(session.getAttribute("memberID") != null) {
      String memberID = (String) session.getAttribute("memberID");
       
      StudyMember mem = md.studyMemberOne(memberID);
       
      request.setAttribute("memberInfo", mem);
       
      return "/view/member/goodbye.jsp";
    }
    
    request.setAttribute("msg", msg);
    request.setAttribute("url", url);
 
    return "/view"; 
  }
  
  /*
   * 회원탈퇴 진행
   * */
  @RequestMapping("goodbyePro")
  public String goodbyePro(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(); 
    String msg="로그인이 필요합니다";
    String url="studymember/loginForm";
    
    if(session.getAttribute("memberID") != null) {
      String memberID = (String) session.getAttribute("memberID");
      String pw = request.getParameter("password");
       
      StudyMember mem = md.studyMemberOne(memberID);
      
      if(mem.getPassword().equals(pw)) {
        int deleted = md.studyMemberDelete(memberID);
        
        if(deleted == 1) {
          session.invalidate();
          msg="회원탈퇴가 완료되었습니다.";
          url="main";
        } else {
          
          msg="알 수 없는 오류";
          url="main";
        }
      }else {
        
        msg="비밀번호가 다릅니다";
        url="studymember/goodbye";
      }
        
    }
    
    request.setAttribute("msg", msg);
    request.setAttribute("url", url);
 
    return "/view"; 
  }
  
  @RequestMapping("mywrite_study")
  public String mywrite_study(HttpServletRequest request, HttpServletResponse response) {
 
 
    return "/view/member/mywrite_study.jsp"; 
  }
  
  /*
   * 마이페이지
   * */
  @RequestMapping("userinfo")
  public String userinfo(HttpServletRequest request, HttpServletResponse response) {
    
    HttpSession session = request.getSession();
    
    //유저정보
      String usernick = request.getParameter("usernick");
       
      StudyMember mem = md.studyMemberOneByNick(usernick);
      request.setAttribute("memberInfo", mem);
      //유저 평판
      List<ReputationEstimate> repList = red.getReputation(usernick);
      request.setAttribute("repList", repList);
      
    return "/view/member/userinfo.jsp";
  }
  
}


