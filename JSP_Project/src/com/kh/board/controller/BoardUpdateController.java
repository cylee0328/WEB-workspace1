package com.kh.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;
import com.kh.common.MyFileRenamePolicy;
import com.kh.member.model.vo.Member;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class BoardUpdateController
 */
@WebServlet("/update.bo")
public class BoardUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardUpdateController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 필요한 데이터를 담아서 boardUpdateForm.jsp로 포워딩 시켜주기
		
		ArrayList<Category> list = new BoardService().selectCategoryList();
		
		Board b = new Board();
		
		Attachment at = new Attachment();
		
		request.setAttribute("list", list);
		request.setAttribute("b", b);
		request.setAttribute("at", at);
		
		
		request.getRequestDispatcher("views/board/boardUpdateForm.jsp").forward(request, response);
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// 필요한 데이터를 전달받고 실제 Board과, Attachment테이블에 Update쿼리문을 실행
	
		//1. 전송된 데이터 input file이 포함된 경우 enctype="multipart/form-data"로 전송했을 것.
		if(ServletFileUpload.isMultipartContent(request)) {
			
			// 1_1. 전송파일 용량제한(10mByte)
			int maxSize = 1024* 1024 * 10;
			// 1_2. 전달된 파일을 저장시킬 서버의 폴더의 물리적인 경로 알아내기
			String savePath = request.getSession().getServletContext().getRealPath("/resources/board_upfiles/");
			// 2. 전달된 파일명 수정작업 후 서버에 업로드
			MultipartRequest multi = new MultipartRequest(request, savePath, maxSize, "UTF-8", 
					new MyFileRenamePolicy());
			// 3. 본격적으로 sql문 실행시 필요한 값들 셋팅
			String category = multi.getParameter("category");
			String title = multi.getParameter("title");
			String content = multi.getParameter("content");
			int boardNo = Integer.parseInt(multi.getParameter("boardNo"));
			
			Board b = new Board();
			b.setCategory(category);
			b.setBoardTitle(title);
			b.setBoardContent(content);
			b.setBoardNo(boardNo);
			// 새롭게 전달된 첨부파일이 있는 경우에만 at변수에 필요한 값을 추가할 것
			Attachment at = null;
			
			if(multi.getOriginalFileName("upfile") != null) {
				
				at = new Attachment();
				at.setOrginName(multi.getOriginalFileName("upfile")); 
				at.setChageName(multi.getFilesystemName("upfile"));
				at.setFilePath("resources/board_upfiles/");
				
				// 첨부파일이 원래 등록되어있을 경우 원본파일의 파일번호와 수정된이름을 hidden 넘겨받았음
				if(multi.getParameter("orginFileNo") != null) {
					// 기존에 파일이 있었던 경우
					// Attachment테이블의 정보를 update
					// 기존의 파일번호를 저장시키기
					at.setFileNo(Integer.parseInt(multi.getParameter("originFileNo")));
					
					// 기존의 첨부파일을 삭제
					new File(savePath+multi.getParameter("changeFileName")).delete();
				}else {
					// 기존에 첨부파일이 없는 경우
					// Attachment 테이블에 정보를 insert
					// REF_BNO에 현재 게시글번호를 추가시켜줌.
					at.setRefBno(Integer.parseInt(multi.getParameter("bno")));
				}
			}
			
			// 하나의 트랜잭션으로 board에 update문과 Attachment테이블의 insert,update 동시에 처리해주기
			int result = 0;
			// 항상 board에 update문은 반드시 실행시켜줘야함.
			// case1 : 새로운 첨부파일이 없는경우(x) -> insert (x), update(x), 
			// case2 : 새로운 첨부파일이 있는경우(o) -> 기존에도 첨부파일이 있던 경우(o)-> update(o), insert(x)
			// case3 : 새로운 첨부파일이 있는경우(o), 기존에는 첨부파일이 없던 경우 (x)-> update(x), insert(o)
			
			
			// 수정성공시 : 상세조회페이지로 redirect
				if(result > 0) {
					request.getSession().setAttribute("alertMsg", "게시글 작성 성공");
					response.sendRedirect(request.getContextPath()+"/list.bo?currentPage=1");
				}else {
					// 수정 실패시 : errorPage
					request.setAttribute("errorMsg", "게시글 작성 실패");
					request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
				}

		} else {
			request.setAttribute("errorMsg", "전송방식이 잘못되었습니다");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}
	}
}
