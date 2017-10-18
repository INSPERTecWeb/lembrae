package br.edu.insper;

import org.json.JSONObject;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

public class DAO {
	private Connection connection = null;  
	public DAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			System.out.println("failed to setup driver");
			e1.printStackTrace();
		} 
		try {
			this.connection = DriverManager.getConnection("jdbc:mysql://localhost/notesdb", "root", "674074");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String getSalt() { // gera um salt aleatorio
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
	
	private String hashSha (String soonToBeHash){
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-512"); // usando sha-512
			try {
				byte[] bytes = md.digest(soonToBeHash.getBytes("UTF-8")); // hashiando aqui
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < bytes.length; i++) { // tranforma em string
					sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
				}
				String generatedPassword = sb.toString();
				System.out.println("hash: " + generatedPassword);
				System.out.println("deveria ser string: " + generatedPassword.getClass().getName());
				
				
				return generatedPassword;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "a casa caiu";
	}
	
	private ResultSet query(String sqlquery){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
				stmt = connection.prepareStatement(sqlquery);
				rs = stmt.executeQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return rs;
	}
	
	public void auth(String email, String password, Callback callback) {
		ResultSet rs = this.query(String.format("SELECT * FROM Users WHERE email='%s';", email));
		Users user = null;
		Integer status;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (rs.first()) {
				user = new Users(rs.getInt("ID"), rs.getString("EMAIL"), rs.getString("USERNAME"),
						rs.getString("PASSWORD"),rs.getString("SALT"), rs.getString("avatar"));
				
				String salt = user.getSalt();
				System.out.println("Authing user: " + user.getEmail() + "| pass: " + user.getPassword());
				System.out.println("User: " + user.getId());
				if (Objects.equals(user.getPassword(), hashSha(password + salt))) {
					System.out.println("PASSWORD MATCHED, RESPONDING OK (200)");
					status = 200;
				} else {
					System.out.println("WRONG PASSWORD, RESPONDING NONAUTHORIZED (401)");
					status = 401;
				}
				result.put("user", user);
				result.put("status", status);
				callback.Callback(result);
			} else {
				System.out.println("USER NOT FOUND, RESPONDING NOTFOUND (404)");
				status = 404;
				result.put("status", status);
				callback.Callback(result);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void register(JSONObject received, Callback callback) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String salt = getSalt();
			PreparedStatement stmt = this.connection
					.prepareStatement("INSERT INTO Users(email,username,password,salt,avatar) VALUES(?,?,?,?,?);");
			stmt.setString(1, received.getString("email"));
			stmt.setString(2, received.getString("username"));
			stmt.setString(3, hashSha(received.getString("password") + salt));
			stmt.setString(4, salt);
			System.out.println("oi");
			try{
					stmt.setString(5, received.getString("avatar"));
			}
			catch(JSONException e){
				e.printStackTrace();
				stmt.setString(5, "https://i.imgur.com/qbjmIEA.png");
			}
			//System.out.println(received.getString("avatar"));
			//if (received.getString("avatar") != null){
			//	stmt.setString(5, received.getString("avatar"));
			//}
			//else{
			//	stmt.setString(5, "NULL");
			//}
			
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			if (e.getErrorCode() == 1062) {
				System.out.println("Failed creating new user. user already exists");
				callback.Callback(result);
			}
			return;
		}
		System.out.println("cheguei aqui");
		try {
			PreparedStatement querystmt = this.connection.prepareStatement(String
					.format("SELECT * FROM Users where email='%s' order by id desc limit 1", received.get("email")));
			ResultSet rs = querystmt.executeQuery();
			if (rs.first()) { 
				Users user = new Users(rs.getInt("ID"), rs.getString("EMAIL"), rs.getString("USERNAME"),
						rs.getString("PASSWORD"),rs.getString("salt"),rs.getString("avatar"));
				System.out.println("Created user: " + user.getEmail() + "| username: " + user.getUsername());
				result.put("user", user);
				callback.Callback(result);
			} else {
				System.out.println("There was a problem creating the user with params " + received);
				callback.Callback(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			e.getErrorCode();
		}
	}
	
	public void addNote(JSONObject note,Callback callback){
		try {
			PreparedStatement stmt = this.connection.prepareStatement(
					"INSERT INTO Notes(user_id,content,color,private, concluded, commentary, title, last_user) VALUES(?,?,?,?,?,?,?,?);");
			stmt.setInt(1,note.getInt("userId"));
			stmt.setString(2, note.getString("content"));
			stmt.setString(3, note.getString("color"));
			stmt.setBoolean(4, note.getBoolean("isPrivate"));
			stmt.setBoolean(5, note.getBoolean("isConcluded"));
			stmt.setString(6, note.getString("commentary"));
			stmt.setString(7, note.getString("title"));
			stmt.setString(8, note.getString("lastUser"));
			stmt.execute();
			PreparedStatement querystmt = this.connection.prepareStatement(
				String.format("SELECT * FROM Notes JOIN Users on Users.id = Notes.user_id where user_id=%s order by notes.id desc limit 1",note.getInt("userId"))
			);
			ResultSet rs = querystmt.executeQuery();
			if (rs.first()){
				Map<String,Object> result = new HashMap<String,Object>();
				result.put("note",new Note(
						rs.getInt("id"),rs.getInt("user_id"),rs.getTimestamp("created_at"),
						rs.getTimestamp("updated_at"),rs.getString("content"),rs.getString("color"),
						rs.getBoolean("private"), rs.getBoolean("concluded"),rs.getString("commentary"), rs.getString("username"),rs.getString("title"),rs.getString("last_user")));
				callback.Callback(result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getNotes(Integer uid,String requested,Callback callback) {
		List<Note> notes = new ArrayList<Note>();
		ResultSet rs = null;
		Map<String,Object> result = new HashMap<String,Object>();
		if (Objects.equals(requested, "PUBLIC_NOTES")){
			rs = this.query (
				String.format("SELECT * FROM Notes JOIN Users ON Users.id = Notes.user_id WHERE (user_id='%s' and private=1) or private=0;", uid)
			);
		}
		
		try {
			while(rs.next()){
				notes.add(new Note(rs.getInt("id"),rs.getInt("user_id"),rs.getTimestamp("created_at"),rs.getTimestamp("updated_at"),
						rs.getString("content"),rs.getString("color"),rs.getBoolean("private"), rs.getBoolean("concluded"), rs.getString("commentary"), rs.getString("username"),rs.getString("title"),rs.getString("last_user")));
			}
//			notes.forEach((note)-> System.out.print(note.getTitle()));
			result.put("notes", notes);
			System.out.println(notes);
			callback.Callback(result);
			rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
//	public void removeNote(Integer uid) {
//		try {
//			PreparedStatement stmt = connection.prepareStatement(
//					"UPDATE Notes SET content=?,color=?,private=?, title=? WHERE id=?"
//					);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public void update(JSONObject received, Callback callback) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			PreparedStatement stmt = connection.prepareStatement(
					"UPDATE Notes SET content=?,color=?,private=?, concluded=?, commentary=?, title=?, last_user=? WHERE id=?"
					);
			stmt.setString(1, received.getString("content"));
			stmt.setString(2, received.getString("color"));
			stmt.setBoolean(3, received.getBoolean("isPrivate"));
			stmt.setBoolean(4, received.getBoolean("isConcluded"));
			stmt.setString(5, received.getString("commentary"));
			stmt.setString(6, received.getString("title"));
			stmt.setString(7, received.getString("lastUser"));
			stmt.setInt(8, received.getInt("id"));
			stmt.execute();
			stmt.close();
			result.put("status", "SUCCESS");
			callback.Callback(result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("status", "FAILURE");
			callback.Callback(result);
		}
	}
	
//	public void registra(Users user) {
//		String sql = "UPDATE Pessoas SET " + "nome=?, nascimento=?, altura=? WHERE id=?";
//		PreparedStatement stmt;
//		try {
//			stmt = connection.prepareStatement(sql);
//			stmt.setString(1, pessoa.getNome());
//			stmt.execute(); 
//			stmt.close(); 
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public void close() { 
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void adiciona(Pessoas pessoa) { 
		String sql = "INSERT INTO Pessoas" +  "(nome,nascimento,altura) values(?,?,?)";
		try{
			PreparedStatement stmt = connection.prepareStatement(sql);  
			stmt.setString(1,pessoa.getNome()); 
			stmt.setDate(2, new Date(pessoa.getNascimento().getTimeInMillis()));  
			stmt.setDouble(3,pessoa.getAltura());  
			stmt.execute(); 
			stmt.close();			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void remove(Integer id) {
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement("DELETE FROM Notes WHERE id=?");
			stmt.setInt(1, id);
			stmt.execute(); 
			stmt.close(); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	

	
}
