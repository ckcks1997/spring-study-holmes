package controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import model.GroupMember;
import model.Notice;
import model.Report;
import model.ReputationEstimate;
import model.StudyMember;
import model.StudyMenu;
import service.CommunityBoardDao;
import service.GroupMemberDao;
import service.KakaoService;
import service.NoticeDao;
import service.ReportDao;
import service.ReputationEstimateDao;
import service.StudyMemberDao;
import service.StudyMenuDao;


@Controller  
@RequestMapping("/studymember/") 
public class StudyMemberController {

	StudyMemberDao md;
	StudyMenuDao mud;
	CommunityBoardDao cbd;
	GroupMemberDao gmd;
	NoticeDao nd;
	ReportDao rd;
	ReputationEstimateDao red;
	
    KakaoService kakaoService;
    PasswordEncoder passwordEncoder;

	public StudyMemberController(StudyMemberDao md, StudyMenuDao mud, CommunityBoardDao cbd, GroupMemberDao gmd,
			NoticeDao nd, ReportDao rd, ReputationEstimateDao red, KakaoService kakaoService,
			PasswordEncoder passwordEncoder) {
		this.md = md;
		this.mud = mud;
		this.cbd = cbd;
		this.gmd = gmd;
		this.nd = nd;
		this.rd = rd;
		this.red = red;
		this.kakaoService = kakaoService;
		this.passwordEncoder = passwordEncoder;
	}

	
	HttpServletRequest request;
	HttpSession session;
	
	@ModelAttribute
	void init(HttpServletRequest request) {
		this.request = request;
		this.session = request.getSession();
	}
	

  /*
   * ?????? ?????????
   * */
  @RequestMapping("notice")
  public String memberNotice(Model model) {

	  String id = (String) session.getAttribute("memberNickname");
      List<Notice> noticeList = nd.noticeGet(id); //?????? ????????? ?????????
      nd.noticeRead(id);
      
      model.addAttribute("noticeList", noticeList);
      session.setAttribute("noticeCount", 0);
      return "/view/member/memberNotice";      

  }
  
  /*
   * ?????? ??????
   * */
  @RequestMapping("noticeInfo")
  public String noticeInfo(Model model) {
 
	  String id = (String) session.getAttribute("memberNickname"); 
      int noticeNum = Integer.parseInt(request.getParameter("noticeNum"));
      
      //noticeNum??? ???????????? notice?????? ????????????
      Notice n = nd.noticeGetByNoticeNum(noticeNum);
      if(n.getNickname_to().equals(id)) { //????????? ???????????? ?????? ???????????? ?????????
      
    	  if(n.getInfo().contains("study")) {//Info??? ????????? Info2 ???????????? title ???????????????
    		   
    		  StudyMenu menu = mud.menuBoardOne(Integer.parseInt(n.getInfo2()));
    		  String title = menu.getTitle();  
    		  model.addAttribute("title", title);
         
    	  }  else if(n.getInfo().contains("report")) { //Info??? ?????????
    		  //???????????? ????????????
    		  int board_num = Integer.parseInt(n.getInfo2());
    		  List<String> reportReason = rd.reportReason(board_num);
    		  model.addAttribute("reportReason", reportReason);
    		  
    		  //report??????????????? ??? ??? ?????????????????? 
    		  Report report = rd.reportOne(board_num);
    		  model.addAttribute("report", report);

    	  }
      // notice ?????????
    	  model.addAttribute("notice", n);  
    	  return "/view/member/memberNoticeInfo";
      }      
 
    model.addAttribute("msg", "?????? ??????");  
    return "redirect:/board/main";
  }
  
  @ResponseBody
  @RequestMapping(value="noticeDelete", produces = MediaType.TEXT_PLAIN_VALUE)
  public String noticeDelete(@RequestBody Map<String, String> req) { 
	  Integer noticeNum = Integer.parseInt(req.get("noticeNum")); 
	  int result= nd.noticeDelete(noticeNum);
	  return result == 1 ? "1" : "0"; //?????? ?????? ????????? 1
  }
  
  
  /*
   * ??????-???????????? ??????
   * */
  @RequestMapping("groupAccept")
  public String groupAccept(RedirectAttributes redirect, Integer notice_num, Integer accept) {

	  String id = (String) session.getAttribute("memberNickname");
	
	  String msg = "????????????";
	  String url = "/board/main"; 
	  if(notice_num != null && accept != null) {
	      int noticeNum = notice_num; //???????????? ?????????
	      Notice n = nd.noticeGetByNoticeNum(noticeNum); //???????????? ??????
	      StudyMenu menu = mud.menuBoardOne(Integer.parseInt(n.getInfo2())); //??????????????? ?????? ????????? ??????????????? ????????????
	 
	      if(n.getNickname_to().equals(id)) { //????????? ?????? ???????????? ??????, ????????????
	        //group??? ???????????? ??????
	        GroupMember gm = new GroupMember();
	        gm.setBoardnum(menu.getBoard_num());
	        gm.setNickname(n.getNickname_from()); 
	        
	       if(accept == 1) { 
	        gmd.groupInsert(gm, 0);
	        nd.noticeWriteAll(n.getNickname_from(), n.getNickname_to(), "text: ????????? ?????? ??????","\'"+ menu.getTitle()+"\' ???????????? ?????????????????????.");
	        nd.noticeDelete(noticeNum);
	       } else  if(accept == 0) { 
	           nd.noticeWriteAll(n.getNickname_from(), n.getNickname_to(), "text: ????????? ?????? ??????","\'"+ menu.getTitle()+"\' ????????? ????????? ?????????????????????.");
	           nd.noticeDelete(noticeNum);
	       }
	        msg = "?????????????????????";
	        url = "/studymember/notice";   
	      }
	  }
    redirect.addFlashAttribute("msg", msg);  
    return "redirect:"+url;
  }
  
  /*
   * ????????? ?????????
   * */
  @RequestMapping("loginForm")
  public String memberloginForm() {
    return "view/member/login";
  }

  /*
   * ????????? ??????
   * 
   * ????????? ??????????????? memberID(?????????)??? memberNickname(?????????, ??????x!!),memberPicture 3?????? ????????? ???????????????.
   * */
  
  @PostMapping("loginPro")
  public String memberloginPro(Model model) {

    String id = request.getParameter("id"); // email
    String pass = request.getParameter("password"); 
    
    StudyMember mem = md.studyMemberOne(id);
    
    String msg = "???????????? ???????????????";
    String url = "/studymember/loginForm";
    if (mem != null) {
      if (pass.equals(mem.getPassword()) || passwordEncoder.matches(pass, mem.getPassword())) {
        request.getSession().setAttribute("memberID", mem.getEmail());
        request.getSession().setAttribute("memberNickname", mem.getNickname());
        request.getSession().setAttribute("memberPicture", mem.getPicture());
        msg = "";
        url = "/board/main";
      } else {
        System.out.println(mem);
        msg = "???????????? ???????????????";
      }
    } 
    model.addAttribute("msg", msg);  
    return "redirect:"+url;
  }
  
  /*
   * ????????? ?????????
   * */
  @RequestMapping("kakaologin")
  public String kakaoLogin(@RequestParam(value = "code", required = false) String code, Model model) {
      String access_Token = kakaoService.getAccessToken(code);
      HashMap<String, Object> userInfo = kakaoService.getUserInfo(access_Token);
      
      StudyMember mem = md.studyMemberOne((String)userInfo.get("email"));
      if(mem == null) {
    	  model.addAttribute("kakaoemail", userInfo.get("email"));
    	  model.addAttribute("kakaonick", userInfo.get("nickname"));
    	  return "view/member/join";
      }
      request.getSession().setAttribute("memberID", mem.getEmail());
      request.getSession().setAttribute("memberNickname", mem.getNickname());
      request.getSession().setAttribute("memberPicture", mem.getPicture());
    return "redirect:/board/main";
  }
  
  /*
   * ????????????-?????? ??????
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
  
    /*????????????*/
  
  /*
   * ???????????? ?????????
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
   * ???????????? ??????
   * 
   * */
  @RequestMapping("joinPro")
  public String memberJoinPro(StudyMember m, Model model) {
	String encPass= passwordEncoder.encode(m.getPassword());
	m.setPassword(encPass);
    int result = md.insertStudyMember(m);
    
    String msg = "?????? ??????";
    String url = "/studymember/loginForm";
    
    if(result == 1) msg="?????? ??????";
    
    model.addAttribute("msg", msg); 
    return "redirect:"+url;
  }
  /*
   * id????????????
   * */
  
  @ResponseBody
  @RequestMapping("idexist")
  public String idExist(@RequestBody String id) {
  
    System.out.println("id="+id); 
    int mem = md.studyMemberIdExist(id); 
      
    return Integer.toString(mem);
  }
  
  /*
   * ????????? ????????????
   * */
  @ResponseBody //Ajax ??????
  @RequestMapping(value="nicknameExist") 
  public String nicknameExist(@RequestBody String nickname) {  
    int mem = md.studyMemberNicknameExist(nickname); 
    System.out.println("result="+mem); 
      
    return Integer.toString(mem);
  }
  
  /*
   * ???????????? ??? ???????????? ???
   * */
  @RequestMapping("pictureForm")
  public String pictureForm() {
    return "/single/pictureForm";
  }
  
  /*
   * ???????????? ??? ???????????? ??????
   * */
  @RequestMapping("picturePro")
  public String picturePro(@RequestParam("picture") MultipartFile file, Model model) {
    
    String path = request.getServletContext().getRealPath("/")+"imgupload/";
    System.out.println(path);
    File folder = new File(path);
    if(!folder.exists()) {
    	folder.mkdir();
    }
    String filename = file.getOriginalFilename();

    if(!file.isEmpty()) {
    	File file2 = new File(path, file.getOriginalFilename());
    	try {
    		file.transferTo(file2); 
		} catch (IllegalStateException e) { 
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
    }

    model.addAttribute("filename", filename);
    return "/single/picturePro";
  }
  
  
  /*
   * ???????????????
   * */
  
  @RequestMapping("mypage")
  public String mypage(Model model) {
      String memberID = (String) session.getAttribute("memberID"); 
      StudyMember mem = md.studyMemberOne(memberID);
      model.addAttribute("memberInfo", mem);
    //?????? ??????
      List<ReputationEstimate> repList = red.getReputation(mem.getNickname());
      model.addAttribute("repList", repList);

      return "/view/member/mypage"; 
  }
  
    /*???  ????????? ??????*/
  
  /*
   * ??? ????????? ??????
   * */
  
  @RequestMapping("myprofile")
  public String myprofile(Model model) {
      String memberID = (String) session.getAttribute("memberID");   
      StudyMember mem = md.studyMemberOne(memberID);
      model.addAttribute("memberInfo", mem);
      
      return "/view/member/myprofile";
  }
  
  /*
   * ????????????
   * */
  @RequestMapping("pictureChange")
  public String pictureChange(@RequestParam("picture") MultipartFile file) {
    
    String path = request.getServletContext().getRealPath("/")+"imgupload/";
    File folder = new File(path);
    if(!folder.exists()) {
    	folder.mkdir();
    }
    String filename = file.getOriginalFilename();

    if(!file.isEmpty()) {
    	File file2 = new File(path, file.getOriginalFilename());
    	try {
    		file.transferTo(file2); 
		} catch (IllegalStateException e) { 
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
    }
    
    md.changePic((String)session.getAttribute("memberNickname"), filename);
    session.setAttribute("memberPicture", filename);
    
    return "redirect:/studymember/mypage";
  }
  
  /*
   * ??? ????????? ??????-???????????? ?????????
   * */
  @PostMapping("myprofileEdit1")
  public String myprofileEdit1(Model model, RedirectAttributes redirect) { 
	  String msg="?????? ??????"; 
	  String url= "redirect:/studymember/myprofile";
      String s_id = (String)session.getAttribute("memberID");
      String profile_intro = (String) request.getParameter("profile_intro");
 
      int result = md.studyMemberIntroUpdate(s_id, profile_intro);
      if(result == 1) {
        msg="?????????????????????"; 
      }
     //????????? ?????? post???????????? ????????????.
    redirect.addFlashAttribute("msg", msg); 
    return url;
  }
  
  
  /*
   * ???????????? ??????
   * */
  @RequestMapping("passwordChange")
  public String passwordChange(Model model) {
      return "/view/member/passwordChange"; 
  }
  
  /*
   * ???????????? ?????? ??????
   * */
  @PostMapping("passwordChangePro")
  public String passwordChangePro(
		  @RequestParam("password") String password,
		  @RequestParam("newpassword") String newpassword, 
		  RedirectAttributes redirect ) {
 
    String msg="?????? ??????";
    String url="redirect:/studymember/myprofile";
    
    if(!password.isEmpty()) {
    	msg="??????????????? ????????????.";
      String s_id = (String)request.getSession().getAttribute("memberID");
      StudyMember sm = md.studyMemberOne(s_id);
      if(sm.getPassword().equals(password)){
	      int result = md.changePassword(s_id, newpassword); 
	      msg="??????????????? ?????? ???????????????.";
      }
      url="redirect:/studymember/myprofile";
    }  
    redirect.addFlashAttribute("msg", msg); 
    return url;

  }
  
  /*
   * ????????????
   * */
  @RequestMapping("goodbye")
  public String goodBye(Model model) { 

      String memberID = (String) session.getAttribute("memberID");
      StudyMember mem = md.studyMemberOne(memberID);
      model.addAttribute("memberInfo", mem);
       
      return "/view/member/goodbye";
  }
  
  /*
   * ???????????? ??????
   * */
  @PostMapping("goodbyePro")
  public String goodbyePro(RedirectAttributes redirect ) {
	  
	  String msg="??? ??? ?????? ??????";
      String url="redirect:/board/main";

      String memberID = (String) session.getAttribute("memberID");
      String pw = request.getParameter("password");
       
      StudyMember mem = md.studyMemberOne(memberID);
      
      if(passwordEncoder.matches(pw, mem.getPassword()) || pw.equals(mem.getPassword())) {
        int deleted = md.studyMemberDelete(memberID);
        
        if(deleted == 1) {
          session.invalidate();
          msg="??????????????? ?????????????????????.";
        } else {
          msg="??? ??? ?????? ??????";
        }
      }else {
        msg="??????????????? ????????????";
        url="redirect:/studymember/goodbye";
      }
    redirect.addFlashAttribute("msg", msg); 
    return url; 
  }
 
  /*
   * ???????????????
   * */
  @RequestMapping("userinfo")
  public String userinfo(Model model) {
    //????????????
      String usernick = request.getParameter("usernick");
      StudyMember mem = md.studyMemberOneByNick(usernick);
      model.addAttribute("memberInfo", mem);
      //?????? ??????
      List<ReputationEstimate> repList = red.getReputation(usernick);
      model.addAttribute("repList", repList);
      
    return "/view/member/userinfo";
  }
  
}


