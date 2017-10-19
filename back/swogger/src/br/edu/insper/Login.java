package br.edu.insper;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
//		Users user = dao.Auth();
//		JSONObject my_obj = new JSONObject();
////		pessoas.forEach(pessoa -> System.out.println(pessoa));
//		my_obj.put("email", user.getEmail());
//		my_obj.put("username", user.getUsername());
////		response.getWriter().append("Served at: ").append(request.getContextPath());
//		response.getWriter().println(my_obj.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("POST Request received");
		
		// Request
		String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		JSONObject obj = new JSONObject(test);
		JSONObject payload = obj.getJSONObject("payload");
		System.out.println(payload);
		DAO dao = new DAO();
		
		// response
		JSONObject res = new JSONObject();
		dao.auth(payload.getString("email"), payload.getString("password"), 
				(Map<String,Object> result) -> {
					res.put("action", "AUTH");
					res.put("status", result.get("status"));
					System.out.println(result.get("status"));
					if (Objects.equals(result.get("status"), 200)){
						res.put("payload",new JSONObject(result.get("user")));
					}
					try {
						System.out.println(res);
						response.getWriter().println(res);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
		
		
//		doGet(request, response);
		
	}

}
