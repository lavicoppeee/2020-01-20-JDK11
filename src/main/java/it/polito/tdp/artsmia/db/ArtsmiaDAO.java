package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.artsmia.model.ArtConnessi;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getRuoli(){
		String sql = "SELECT DISTINCT role from authorship ORDER BY role ASC ";
		List<String> ruoli= new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				ruoli.add(res.getString("role"));
			}
			conn.close();
			return ruoli;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Integer> getArtist(String ruolo){
		
		final String sql= "SELECT a1.artist_id " + 
				"FROM authorship a1, artists a " + 
				"WHERE a1.artist_id= a.artist_id and role= ? ";
		
		List<Integer> artisti= new ArrayList<Integer>();
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				artisti.add(res.getInt("artist_id"));
			}
			conn.close();
			return artisti;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
				
	}
	
	public List<ArtConnessi> getConnessi(String ruolo){
		
		final String sql="select a1.artist_id AS a1, a2.artist_id AS a2, COUNT(DISTINCT eo1.exhibition_id) AS peso " + 
				"from artists a1, artists a2, authorship au1, authorship au2, exhibition_objects eo1, exhibition_objects eo2 " + 
				"where au1.role = ? and au2.role = ? " + 
				"and au1.artist_id = a1.artist_id and au2.artist_id = a2.artist_id " + 
				"and au1.object_id = eo1.object_id and au2.object_id = eo2.object_id " + 
				"and eo1.exhibition_id = eo2.exhibition_id " + 
				"and a1.artist_id > a2.artist_id " + 
				"GROUP BY a1.artist_id, a2.artist_id";
		
		
		List<ArtConnessi> connessi= new ArrayList<ArtConnessi>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			st.setString(2, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				connessi.add(new ArtConnessi(res.getInt("a1"), res.getInt("a2"), res.getInt("peso")));
			}
			conn.close();
			return connessi;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
		
		
	}
	
}
