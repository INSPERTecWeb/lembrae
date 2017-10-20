package br.edu.insper;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
		dao.getNotes(uid, notesQuery,(Map<String,Object> result) -> {
					@SuppressWarnings("unchecked")
					List<Note> resultList = (List<Note>) result.get("notes");
					JSONArray res = new JSONArray();
					for (int i=0; i<resultList.size(); i++) {
						JSONObject test = new JSONObject();
						test.put("commentary", resultList.get(i).getCommentary());
						test.put("lastUser", resultList.get(i).getlastUser());
						test.put("createdAt", resultList.get(i).getCreatedAt());
						test.put("color", resultList.get(i).getColor());
						test.put("ownerUsername", resultList.get(i).getOwnerUsername());
						test.put("id", resultList.get(i).getId());
						test.put("isPrivate", resultList.get(i).getIsPrivate());
						test.put("isConcluded", resultList.get(i).getIsConcluded());
						test.put("title", resultList.get(i).getTitle());
						test.put("userId", resultList.get(i).getUserId());
						test.put("content", resultList.get(i).getContent());
						test.put("updatedAt", resultList.get(i).getUpdatedAt());
						test.put("ownerAvatar", resultList.get(i).getOwnerAvatar());
						test.put("lastAvatar", resultList.get(i).getLastAvatar());
//						System.out.println(resultList.get(i).getLastAvatar());
//						System.out.println(test.toString());
						res.put(test);
					}
					
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
		System.out.println("POST Request received - Add note");
		// Request
		String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		JSONObject obj = new JSONObject(test);
		JSONObject payload = obj.getJSONObject("payload");
//		System.out.println(payload);
		DAO dao = new DAO();
		// response
		dao.addNote(payload,(Map<String,Object> result) -> {
			try {
				System.out.println("AQUI MEN" + new JSONObject(result.get("note")).toString());
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
