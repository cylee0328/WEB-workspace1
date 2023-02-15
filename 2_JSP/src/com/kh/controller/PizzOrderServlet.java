package com.kh.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PizzOrderServlet
 */
@WebServlet("/pizzaOrder.do")
public class PizzOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PizzOrderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1) 전달값중에 한글이 있을경우 인코딩 처리(post 방식일때만)
		request.setCharacterEncoding("UTF-8");
		
		//2) 요청시 전달값 뽑기 및 데이터 가공처리 => 변수, 객체로 기록
		
		String pizza = request.getParameter("pizza");// 치즈피자
		String [] toppings = request.getParameterValues("topping"); // [고구마무스 , 파인애플토핑]
		String [] sides    = request.getParameterValues("side");// [치즈오븐스파게티, 오븐구이통닭]
		
		int price= 0;
		
		switch(pizza) {
		case "콤비네이션피자" : price += 5000; break;
		case "치즈피자"     : price += 6000; break;
		case "포테이토피자"  : 
		case "고구미피자"    : price += 7000; break;
		case "불고기피자"    : price += 8000; break;
		}
		
		if(toppings != null) {
			for(String topping : toppings) {
				
				switch(topping) {
				case "고구마무스"  : price += 1000; break;
				case "콘크림무스"  : price += 1500; break;
				case "파인애플토핑" :
				case "치즈토핑"   :
				case "치즈바이트"   : price += 2000; break;
				case "치즈크러스트" : price += 3000; break;
				}
				
			}
		}
		
		if(sides != null) {
			for(String side : sides) {
				// 모든 사이드메뉴 가격이 5000원이라고 설정
				price+= 5000;
			}
		}
		
		// 4) 요청처리 후 사용자가 보게 될 응답페이지를 만들기
		request.setAttribute("pizza", pizza);
		request.setAttribute("toppings", toppings);
		request.setAttribute("sides", sides);
		request.setAttribute("price", price);
		
		// 응답할 뷰 를선택
		//RequestDispatcher view = request.getRequestDispatcher("views/05_PizzaPayment.jsp");
		
		//view.forward(request, response);
		
		request.getRequestDispatcher("views/05_PizzaPayment.jsp").forward(request, response);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
