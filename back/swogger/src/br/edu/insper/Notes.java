package br.edu.insper;

<<<<<<< HEAD
import org.json.JSONArray;
import org.json.JSONObject;
=======
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
>>>>>>> 25515533c59f5eac67869b15f8eeb5634a475e30

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
<<<<<<< HEAD
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
=======
import org.json.*;
>>>>>>> 25515533c59f5eac67869b15f8eeb5634a475e30

/**
 * Servlet implementation class Login
 */
@WebServlet("/notes")
public class Notes extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Notes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("GET Request received");
		// Request
		String notesQuery = request.getParameter("q");
		Integer uid = Integer.decode(request.getParameter("uid"));
		System.out.print("User " + request.getParameter("user") + " Requested " + notesQuery);
		DAO dao = new DAO();
		dao.getNotes(uid, notesQuery,
				(Map<String,Object> result) -> {
					@SuppressWarnings("unchecked")
					List<Note> resultList = (List<Note>) result.get("notes");
					JSONArray res = new JSONArray(resultList);
					try {
						response.getWriter().println(res);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
		
		// response
		
//		dao.Auth(payload.getString("email"), payload.getString("password"), 
//				(Users user, int status) -> {
//					res.put("action", "AUTH");
//					res.put("status", status);
//					System.out.println(status);
//					if (status == 200){
//						res.put("payload",new JSONObject(user));
//					}
//					try {
//						System.out.println(res);
//						response.getWriter().println(res);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				});
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
<<<<<<< HEAD
		System.out.println("POST Request received - Add note");
=======
		System.out.println("POST Request received");
>>>>>>> 25515533c59f5eac67869b15f8eeb5634a475e30
		// Request
		String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		JSONObject obj = new JSONObject(test);
		JSONObject payload = obj.getJSONObject("payload");
<<<<<<< HEAD
//		System.out.println(payload);
=======
		System.out.println(payload);
>>>>>>> 25515533c59f5eac67869b15f8eeb5634a475e30
		DAO dao = new DAO();
		// response
		dao.addNote(payload,(Map<String,Object> result) -> {
			try {
<<<<<<< HEAD
				System.out.println("AQUI MEN" + new JSONObject(result.get("note")).toString());
=======
>>>>>>> 25515533c59f5eac67869b15f8eeb5634a475e30
				response.getWriter().println(new JSONObject(result.get("note")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
;
//		dao.Auth(payload.getString("email"), payload.getString("password"), 
//				(Map<String,Object> result) -> {
//					res.put("action", "AUTH");
//					res.put("status", result.get("status"));
//					System.out.println(result.get("status"));
//					if (Objects.equals(result.get("status"), 200)){
//						res.put("payload",new JSONObject(result.get("user")));
//					}
//					try {
//						System.out.println(res);
//						response.getWriter().println(res);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				});
//		
		
//		doGet(request, response);
		
	}

}
