package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data").toLocalDate(), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione " +
				   "WHERE localita=? AND MONTH(data)=?  ORDER BY data ASC";
		
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

			try {
				Connection conn = ConnectDB.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				
				st.setString(1, localita);
				st.setString(2, Integer.toString(mese)); 
			
				ResultSet rs = st.executeQuery();
				
				while (rs.next()) {

					Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data").toLocalDate(), rs.getInt("Umidita"));
					rilevamenti.add(r);
				}
				
			
				conn.close();
				return rilevamenti;
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
	}
	
	public Map<String,Double> getUmiditaMediaPerMese(int mese){
		final String sql =	"SELECT Localita, AVG(Umidita) AS media "
							+ "FROM situazione "
							+ "WHERE MONTH(DATA)=? "
							+ "GROUP BY Localita "; 
		Map<String,Double> medie = new TreeMap<String,Double>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, mese);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				medie.put(rs.getString("Localita"), rs.getDouble("media"));
			}
			
			rs.close();
			st.close();
			conn.close();
			return medie;
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db", e);
		}
		
	}
	
	public List<Citta> getAllCitta(){
		String sql=" SELECT DISTINCT Localita "+
					" FROM situazione ";
		List<Citta> citta = new ArrayList<Citta>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Citta cittaTemp=new Citta(rs.getString("Localita"));
				citta.add(cittaTemp);
			}

			conn.close();
			return citta;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}


}
