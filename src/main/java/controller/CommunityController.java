package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.multi.MultiLabelUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


import model.Community;
import model.Reply;
import model.StudyMember;
import service.CommunityBoardDao;
import service.ReplyDao;
import service.ReportDao;
import service.StudyMemberDao;


@Controller
@RequestMapping("/community/")
public class CommunityController {

	HttpServletRequest request;
	Model m;
	HttpSession session;
	
	@Autowired
	CommunityBoardDao cbd;
	@Autowired
	ReplyDao rd;
	@Autowired
	ReportDao report_d;
	
	
	@ModelAttribute
	void init(HttpServletRequest request, Model m) {
		this.request = request;
		this.m = m;
		this.session = request.getSession();
	}
	
	// 문의게시판에서 내가 쓴 문의글 보기
	@RequestMapping("comBoardMyAsk")
	public String comBoardMyAsk() {

		HttpSession session = request.getSession();

		String boardid = "";
		int pageInt = 1;
		int limit = 4;

		if (request.getParameter("boardid") != null) {
			session.setAttribute("boardid", "5");
			session.setAttribute("pageNum", "1");
		}

		boardid = (String) session.getAttribute("boardid");
		if (boardid == null) {
			boardid = "5";
		}

		if (request.getParameter("pageNum") != null) {
			session.setAttribute("pageNum", request.getParameter("pageNum"));
		}

		String pageNum = (String) session.getAttribute("pageNum");
		if (pageNum == null) {
			pageNum = "1";
		}

		pageInt = Integer.parseInt(pageNum);

		int boardcount = cbd.comBoardCount(boardid);
		String nickname = (String) session.getAttribute("memberNickname");
		List<Community> list = cbd.comBoardMyAsk(pageInt, limit, boardcount, nickname);

		System.out.println("boardid: " + boardid + "---" + nickname); // 값 확인
		int boardnum = boardcount - limit * (pageInt - 1);
		int bottomLine = 3;
		int startPage = (pageInt - 1) / bottomLine * bottomLine + 1;
		int endPage = startPage + bottomLine - 1;
		int maxPage = (boardcount / limit) + (boardcount % limit == 0 ? 0 : 1);
		if (endPage > maxPage)
			endPage = maxPage;

		String boardName = "문의사항";

		m.addAttribute("boardName", boardName);
		m.addAttribute("pageInt", pageInt);
		m.addAttribute("boardid", boardid);
		m.addAttribute("boardcount", boardcount);
		m.addAttribute("list", list);
		m.addAttribute("boardnum", boardnum);
		m.addAttribute("startPage", startPage);
		m.addAttribute("bottomLine", bottomLine);
		m.addAttribute("endPage", endPage);
		m.addAttribute("maxPage", maxPage);

		return "view/community/comBoardList";
	}

	// 최신순 나열
	@RequestMapping("comBoardList")
	public String comBoardList() {

		HttpSession session = request.getSession();

		String boardid = "";
		int pageInt = 1;
		int limit = 4;

		if (request.getParameter("boardid") != null) {
			session.setAttribute("boardid", request.getParameter("boardid"));
			session.setAttribute("pageNum", "1");
		}

		boardid = (String) session.getAttribute("boardid");
		if (boardid == null) {
			boardid = "1";
		}

		if (request.getParameter("pageNum") != null) {
			session.setAttribute("pageNum", request.getParameter("pageNum"));
		}

		String pageNum = (String) session.getAttribute("pageNum");
		if (pageNum == null) {
			pageNum = "1";
		}

		pageInt = Integer.parseInt(pageNum);

		
		int boardcount = cbd.comBoardCount(boardid);
		List<Community> list = cbd.comBoardList(pageInt, limit, boardcount, boardid);
		int boardnum = boardcount - limit * (pageInt - 1);
		int bottomLine = 3;
		int startPage = (pageInt - 1) / bottomLine * bottomLine + 1;
		int endPage = startPage + bottomLine - 1;
		int maxPage = (boardcount / limit) + (boardcount % limit == 0 ? 0 : 1);
		if (endPage > maxPage)
			endPage = maxPage;

		String boardName = "질문 & 답변";
		switch (boardid) {
		case "5":
			boardName = "문의사항";
			break;
		case "4":
			boardName = "공지";
			break;
		case "3":
			boardName = "정보를 나눠요";
			break;
		case "2":
			boardName = "자유";
			break;

		}
		m.addAttribute("boardName", boardName);
		m.addAttribute("pageInt", pageInt);
		m.addAttribute("boardid", boardid);
		m.addAttribute("boardcount", boardcount);
		m.addAttribute("list", list);
		m.addAttribute("boardnum", boardnum);
		m.addAttribute("startPage", startPage);
		m.addAttribute("bottomLine", bottomLine);
		m.addAttribute("endPage", endPage);
		m.addAttribute("maxPage", maxPage);

		return "view/community/comBoardList";
	}

	// 댓글순 나열
	@RequestMapping("comBoardReply")
	public String comBoardReply() {

		HttpSession session = request.getSession();
		String boardid = "";
		int pageInt = 1;
		int limit = 4;

		if (request.getParameter("boardid") != null) {
			session.setAttribute("boardid", request.getParameter("boardid"));
			session.setAttribute("pageNum", "1");
		}

		boardid = (String) session.getAttribute("boardid");
		if (boardid == null) {
			boardid = "1";
		}

		if (request.getParameter("pageNum") != null) {
			session.setAttribute("pageNum", request.getParameter("pageNum"));
		}

		String pageNum = (String) session.getAttribute("pageNum");
		if (pageNum == null) {
			pageNum = "1";
		}

		pageInt = Integer.parseInt(pageNum);

		int boardcount = cbd.comBoardCount(boardid);
		List<Community> list = cbd.comBoardReply(pageInt, limit, boardcount, boardid);
		int boardnum = boardcount - limit * (pageInt - 1);
		int bottomLine = 3;
		int startPage = (pageInt - 1) / bottomLine * bottomLine + 1;
		int endPage = startPage + bottomLine - 1;
		int maxPage = (boardcount / limit) + (boardcount % limit == 0 ? 0 : 1);
		if (endPage > maxPage)
			endPage = maxPage;

		String boardName = "질문 & 답변";
		switch (boardid) {
		case "5":
			boardName = "문의사항";
			break;
		case "4":
			boardName = "공지";
			break;
		case "3":
			boardName = "정보공유";
			break;
		case "2":
			boardName = "자유";
			break;

		}
		m.addAttribute("boardName", boardName);
		m.addAttribute("pageInt", pageInt);
		m.addAttribute("boardid", boardid);
		m.addAttribute("boardcount", boardcount);
		m.addAttribute("list", list);
		m.addAttribute("boardnum", boardnum);
		m.addAttribute("startPage", startPage);
		m.addAttribute("bottomLine", bottomLine);
		m.addAttribute("endPage", endPage);
		m.addAttribute("maxPage", maxPage);

		return "view/community/comBoardList";
	}

	@RequestMapping("comBoardRead")
	public String comBoardRead() {

		HttpSession session = request.getSession();
		String boardid = "";
		int pageInt = 1;
		int limit = 4;

		if (request.getParameter("boardid") != null) {
			session.setAttribute("boardid", request.getParameter("boardid"));
			session.setAttribute("pageNum", "1");
		}

		boardid = (String) session.getAttribute("boardid");
		if (boardid == null) {
			boardid = "1";
		}

		if (request.getParameter("pageNum") != null) {
			session.setAttribute("pageNum", request.getParameter("pageNum"));
		}

		String pageNum = (String) session.getAttribute("pageNum");
		if (pageNum == null) {
			pageNum = "1";
		}

		pageInt = Integer.parseInt(pageNum);

		int boardcount = cbd.comBoardCount(boardid);
		List<Community> list = cbd.comBoardRead(pageInt, limit, boardcount, boardid);
		int boardnum = boardcount - limit * (pageInt - 1);
		int bottomLine = 3;
		int startPage = (pageInt - 1) / bottomLine * bottomLine + 1;
		int endPage = startPage + bottomLine - 1;
		int maxPage = (boardcount / limit) + (boardcount % limit == 0 ? 0 : 1);
		if (endPage > maxPage)
			endPage = maxPage;

		String boardName = "질문 & 답변";
		switch (boardid) {
		case "5":
			boardName = "문의사항";
			break;
		case "4":
			boardName = "공지";
			break;
		case "3":
			boardName = "정보공유";
			break;
		case "2":
			boardName = "자유";
			break;

		}
		m.addAttribute("boardName", boardName);
		m.addAttribute("pageInt", pageInt);
		m.addAttribute("boardid", boardid);
		m.addAttribute("boardcount", boardcount);
		m.addAttribute("list", list);
		m.addAttribute("boardnum", boardnum);
		m.addAttribute("startPage", startPage);
		m.addAttribute("bottomLine", bottomLine);
		m.addAttribute("endPage", endPage);
		m.addAttribute("maxPage", maxPage);

		return "view/community/comBoardList";
	}

	@RequestMapping("comBoardmyList1")
	public String comBoardmyList1() {
		HttpSession session = request.getSession();
		String nickname = (String) session.getAttribute("memberNickname");
		String boardid = "";
		int pageInt = 1;
		int limit = 3;

		if (request.getParameter("boardid") != null) {
			session.setAttribute("boardid", request.getParameter("boardid"));
			session.setAttribute("pageNum", "1");
		}

		boardid = (String) session.getAttribute("boardid");
		if (boardid == null) {
			boardid = "1";
		}

		if (request.getParameter("pageNum") != null) {
			session.setAttribute("pageNum", request.getParameter("pageNum"));
		}

		String pageNum = (String) session.getAttribute("pageNum");
		if (pageNum == null) {
			pageNum = "1";
		}

		pageInt = Integer.parseInt(pageNum);

		int boardcount = cbd.myComCount(nickname);
		List<Community> list = cbd.comBoardmyList1(pageInt, limit, boardcount, nickname);
		int boardnum = boardcount - limit * (pageInt - 1);
		int bottomLine = 3;
		int startPage = (pageInt - 1) / bottomLine * bottomLine + 1;
		int endPage = startPage + bottomLine - 1;
		int maxPage = (boardcount / limit) + (boardcount % limit == 0 ? 0 : 1);
		if (endPage > maxPage)
			endPage = maxPage;

		String boardName = "내가 쓴 게시판";

		m.addAttribute("boardName", boardName);
		m.addAttribute("pageInt", pageInt);
		m.addAttribute("boardid", boardid);
		m.addAttribute("boardcount", boardcount);
		m.addAttribute("list", list);
		m.addAttribute("boardnum", boardnum);
		m.addAttribute("startPage", startPage);
		m.addAttribute("bottomLine", bottomLine);
		m.addAttribute("endPage", endPage);
		m.addAttribute("maxPage", maxPage);

		return "view/community/myList1";
	}

	// 글쓰기 페이지
	@RequestMapping("comWriteForm")
	public String comWriteForm() {

		HttpSession session = request.getSession();
		String msg = "로그인이 필요합니다";
		String url = request.getContextPath() + "/studymember/loginForm";

		if (session.getAttribute("memberNickname") != null) {
			return "view/community/comWriteForm";
		}

		m.addAttribute("msg", msg);
		m.addAttribute("url", url);

		return "view/alert";
	}

	// 글쓰기
	@RequestMapping("comWritePro")
	public String comWritePro(Community com) {
		String path = request.getServletContext().getRealPath("/") + "/comboardupload/";

		// 폴더가 없으면 에러가 발생합니다. 디렉토리 확인 후 폴더를 생성하는 코드입니다.
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}

		int size = 10 * 10 * 1024 * 10 * 10; 
		MultipartRequest multi = null;
		try {
			multi = new MultipartRequest(request, path, size, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 세션에 저장된 닉네임 가져와서 커뮤니티 닉네임으로 저장하기
		HttpSession session = request.getSession();
		com.setNickname((String)session.getAttribute("memberNickname"));
		
		//com.setTitle(multi.getParameter("title"));

		// 썸머노트는 텍스트를 <p></p>태그 붙여서 데이터저장함 -- p태그 제거하고 db저장
		//String content = multi.getParameter("content");

		// content1 == 맨앞 맨뒤 p태그 제거한 내용
		//String content1 = content.substring(3, content.length() - 4);
		//com.setContent(content1);
		
		com.setIp(request.getLocalAddr());

		String boardid = (String) session.getAttribute("boardid");
		if (boardid == null) {
			boardid = "1";
		}
		//com.setBoardid(boardid);

		com.setBoard_num(cbd.comNextNum());
		
		
		//셋팅한 com으로 insert하기 
		int num = cbd.comInsertBoard(com);

		String msg = "게시물이 등록되지 않습니다.";
		String url = request.getContextPath() + "/community/comWriteForm";
		if (num == 1) {
			msg = "게시물이 등록되었습니다.";
			url = request.getContextPath() + "/community/comBoardList?pageNum=1";

		}

		m.addAttribute("msg", msg);
		m.addAttribute("url", url);

		/*
		// 글 일부만 보여주기----수정해야함 --------------------------------------------------
		// 글자수 길이 조건 걸기 추가해야함

		// p태그 자체를 공백으로 , content3 == 모든 <p></p>태그를 공백으로 만든 내용
		String content2 = content.replaceAll("<p>", " ");
		String content3 = content2.replaceAll("</p>", " ");

		System.out.println(content3);

		if (content3.length() > 20) {
			content3 = content3.substring(0, 10);
			System.out.println("--" + content3 + "...");
		} else {
			// m.addAttribute("preContent", preContent);
			System.out.println(content3 + "...");
		}
		*/
		// -----------------------------------------------------------------------

		return "view/alert";
	}

	// 게시글 상세보기
	@RequestMapping("comBoardInfo")
	public String comBoardInfo(int board_num) {

		HttpSession session = request.getSession();
		Community com = cbd.comBoardOne(board_num);
		m.addAttribute("com", com);
	
		String msg;
		String url;

		// 문의 게시판 검증단계
		//String boardid = (String) request.getParameter("boardid");
		String boardid = com.getBoardid();
		System.out.println(boardid + "---글:" + com.getNickname() + "---현재:" + session.getAttribute("memberNickname")); // 값 확인
			
		
		
		if (boardid.equals("5")) {// 문의 게시판이면
			//System.out.println("------문의게시판------");// 확인
			if (session.getAttribute("memberNickname")==null) {// 로그인이 아예 안 되어있으면
				//(String)session.getAttribute("memberNickname")==null --ok 
				//(String)session.getAttribute("memberNickname").equals(null) -- 앞에꺼가 애초에 null이어서 equals를 못 부름 
				msg = "로그인을 먼저 해주세요";
				url = request.getContextPath() + "/studymember/loginForm";

			}

			// 로그인 되어있는데 1)닉네임과 게시글 작성자가 같거나 2)관리자이면
			else if (com.getNickname().equals(session.getAttribute("memberNickname"))
					|| session.getAttribute("memberNickname").equals("관리자")) {

				// 조회수 올리기
				cbd.comReadCountUp(board_num);

				// 신고한 유저 닉네임 리스트 세팅
				List<String> nicknameList = report_d.reportNickname(board_num);
				m.addAttribute("nicknameList", nicknameList);

				// 댓글보여주기
				List<Reply> reply_list = rd.replyWriteList(board_num);
				int reply_count = rd.replyCount(board_num);

				m.addAttribute("reply_list", reply_list);
				m.addAttribute("reply_count", reply_count);
				return "view/community/comBoardInfo"; // 게시글 상세보기 jsp로 보내기

			} else { //로그인은 되어있는데 글 작성자가 아닐 경우 
				msg = "글 작성자만 열람가능합니다"; 
				url = request.getContextPath() + "/community/comBoardList"; // community컨트롤러 타서 다시 comBoardList()로 back
			}
			m.addAttribute("msg", msg);
			//System.out.println("----------------------" + msg); //값 확인
			m.addAttribute("url", url);

		} else { // 문의 게시판이 아닌 다른 게시판은 검증 거치지 않고 바로 열람 가능 
			System.out.println("---문의 게시판이 아닌 다른 게시판 ---");
			// 조회수 올리기
			cbd.comReadCountUp(board_num);

			// 신고한 유저 닉네임 리스트 세팅
			List<String> nicknameList = report_d.reportNickname(board_num);
			m.addAttribute("nicknameList", nicknameList);

			// 댓글보여주기
			List<Reply> reply_list = rd.replyWriteList(board_num);
			int reply_count = rd.replyCount(board_num);

			m.addAttribute("reply_list", reply_list);
			m.addAttribute("reply_count", reply_count);
			return "view/community/comBoardInfo"; // 바로 jsp로 보내주기

		}
		//System.out.println(msg); //확인
		//System.out.println(url); //확인
		return "view/alert";

	}

	
	
	// 게시글 수정페이지
	@RequestMapping("comBoardUpdateForm")
	public String comBoardUpdateForm(int board_num) {
	
		Community com = cbd.comBoardOne(board_num);
		m.addAttribute("com", com);

		return "view/community/comBoardUpdateForm";
	}

	// 게시글 수정
	@RequestMapping("comBoardUpdatePro")
	public String comBoardUpdatePro(Community com) {
		System.out.println("==================");
		String path = request.getServletContext().getRealPath("/") + "/comboardupload/";

		int size = 10 * 10 * 1024 * 10 * 10;
		MultipartRequest multi = null;
		try {
			multi = new MultipartRequest(request, path, size, "utf-8");
		} catch (Exception e) {
			System.out.println("error   ============");
			e.printStackTrace();
		}
		
		String msg = "";
		String url = "";

	
		if (cbd.comBoardUpdate(com) > 0) {
			msg = "수정되었습니다";
			url = request.getContextPath() + "/community/comBoardInfo?board_num=" + com.getBoard_num();

		} else {
			msg = "수정이 실패하였습니다";
			url = request.getContextPath()+"/community/comBoardInfo?board_num=" + com.getBoard_num();
		}

		m.addAttribute("msg", msg);
		m.addAttribute("url", url);

		return "view/alert";

	}

	// 게시글 삭제
	@RequestMapping("comBoardDelete")
	public String comBoardDelete(int board_num) {

		Community com = cbd.comBoardOne(board_num);
		m.addAttribute("com", com);

		String msg = "";
		String url = "";

		if (cbd.comBoardDelete(board_num) > 0) {

			msg = "게시글이 삭제되었습니다.";
			url = request.getContextPath() + "/community/comBoardList";
		} else {
			msg = "삭제가 불가능합니다";
			url = request.getContextPath() + "/community/comBoardInfo?board_num=" + com.getBoard_num();
		}

		m.addAttribute("msg", msg);
		m.addAttribute("url", url);

		return "view/alert";

	}

	// 검색
	@RequestMapping("comSearch")
	public String comSearch(String part, String searchData,String boardid) {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		cbd.comSearch(part, searchData, boardid);
		System.out.println("part:" + part);
		System.out.println("searchData:" + searchData);

		return "/community/comSearchList";

	}

	// 검색한 페이지
	@RequestMapping("comSearchList")
	public String comSearchList(String part, String searchData) {
		HttpSession session = request.getSession();
		String boardid = "";
		int pageInt = 1;
		int limit = 4;		

		if (request.getParameter("boardid") != null) { // 게시판번호 있으면
			session.setAttribute("boardid", request.getParameter("boardid")); // 세션에 게시판 번호 셋팅
			session.setAttribute("pageNum", "1"); // 세션에 페이지번호 셋팅
		}

		boardid = (String) session.getAttribute("boardid");
		if (boardid == null) {
			boardid = "1";
		}

		if (request.getParameter("pageNum") != null) {
			session.setAttribute("pageNum", request.getParameter("pageNum"));
		}

		String pageNum = (String) session.getAttribute("pageNum");
		if (pageNum == null) {
			pageNum = "1";
		}

		pageInt = Integer.parseInt(pageNum);
		m.addAttribute("part", part);
		m.addAttribute("searchData", searchData);

		int boardcount = cbd.comSearchCount(boardid, part, searchData);
		List<Community> searchList = cbd.comSearchList(pageInt, limit, boardcount, boardid, part, searchData);
		int boardnum = boardcount - limit * (pageInt - 1);
		int bottomLine = 3;
		int startPage = (pageInt - 1) / bottomLine * bottomLine + 1;
		int endPage = startPage + bottomLine - 1;
		int maxPage = (boardcount / limit) + (boardcount % limit == 0 ? 0 : 1);
		if (endPage > maxPage)
			endPage = maxPage;

		String boardName = "질문 & 답변";
		switch (boardid) {
		case "5":
			boardName = "문의사항";
			break;
		case "4":
			boardName = "공지";
			break;
		case "3":
			boardName = "정보공유";
			break;
		case "2":
			boardName = "자유";
			break;

		}
		m.addAttribute("boardName", boardName);
		m.addAttribute("pageInt", pageInt);
		m.addAttribute("boardid", boardid);
		m.addAttribute("boardcount", boardcount);
		m.addAttribute("searchList", searchList);
		m.addAttribute("boardnum", boardnum);
		m.addAttribute("startPage", startPage);
		m.addAttribute("bottomLine", bottomLine);
		m.addAttribute("endPage", endPage);
		m.addAttribute("maxPage", maxPage);

		return "view/community/comSearchList";
	}

	// 이미지 업로드 ajax가 보내는 url 받는 메서드
	@RequestMapping("comImageUpload")
	public String comImageUpload() {
		String path = request.getServletContext().getRealPath("/") + "comboardupload";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		String filename = null;
		MultipartRequest multi = null;
		try {
			multi = new MultipartRequest(request, path, 10 * 1024 * 1024, "utf-8");

		} catch (IOException e) {
			e.printStackTrace();
		}

		filename = multi.getFilesystemName("file"); // ajax에서 FormData로 보낸 file 값 찾기
		System.out.println("------controller확인 filename=" + filename);
		m.addAttribute("filename", filename);
		return "single/comBoardPicture";

	}

	// 메인
	@RequestMapping("main")
	public String main() {

		return "view/main";
	}

}
