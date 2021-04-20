package it.polito.tdp.meteo.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private MeteoDAO meteoDAO;

	public Model() {
		meteoDAO = new MeteoDAO();
	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		Map<String,Double> medie = new TreeMap<String,Double>();
		medie = this.meteoDAO.getUmiditaMediaPerMese(mese);
		String soluzione="";
		for(String key: medie.keySet()) {
			soluzione +=key+"\t\t"+medie.get(key)+"\n";
		}
		return soluzione;
	}
	
	// of course you can change the String output with what you think works best
	public String trovaSequenza(int mese) {
		return "TODO!";
	}
	

}
