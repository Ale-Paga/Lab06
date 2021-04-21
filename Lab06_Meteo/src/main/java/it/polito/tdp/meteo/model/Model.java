package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.HashMap;
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
	private List<Citta> best;
	private List<Citta> leCitta;


	public Model() {
		meteoDAO = new MeteoDAO();
		this.leCitta = meteoDAO.getAllCitta();
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
		List<Citta> parziale= new ArrayList<>();
		this.best=null;	
		String soluzione="Elenco città da visitare \n";
		for(Citta c: leCitta) {
			 c.setRilevamenti(meteoDAO.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		cerca(parziale, 0);
		
		for(Citta ci: best) {
			soluzione+=ci.getNome()+" ";
		}
		
		return soluzione;
	}
	
	
	public void cerca(List<Citta> parziale, int livello){
		//caso terminale
		if(livello==this.NUMERO_GIORNI_TOTALI) {
			Double costo = calcolaCosto(parziale);
			if(best==null || costo<calcolaCosto(best)) {
				best = new ArrayList<>(parziale);
			}
			return;
		}
		
		for(Citta prova: leCitta ) {
			if(aggiuntaValida(prova, parziale)) {
				parziale.add(prova);
				cerca(parziale, livello+1);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
	private Double calcolaCosto(List<Citta> parziale) {

		double costo= 0.0;
		
		for(int giorno=1; giorno<=this.NUMERO_GIORNI_TOTALI; giorno++) {
			Citta c= parziale.get(giorno-1);
			double umid=c.getRilevamenti().get(giorno-1).getUmidita();
			costo+=umid;
		}
		
		for(int giorno=2; giorno<=this.NUMERO_GIORNI_TOTALI; giorno++) {
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2))) {
				costo+=this.COST;
			}
		}
		
		return costo;
	}

	public boolean aggiuntaValida(Citta prova, List<Citta> parziale) {
		
		//controllo di tipo zero
		int conta =0;
		
		for(Citta precedente: parziale) {
			if(precedente.equals(prova)) {
				conta++;
			}
		}
		
		if(conta>= this.NUMERO_GIORNI_CITTA_MAX) {
			return false;
		}
		
		//controllo di tipo uno
		
		if(parziale.size()==0) {
			return true;
		}
		
		if(parziale.size() < this.NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
			return parziale.get(parziale.size()-1).equals(prova);
		}
		
		if(parziale.get(parziale.size()-1).equals(prova)) {
			return true;
		}
		
		for(int i=0; i<this.NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN-1; i++) {
			if(!parziale.get(parziale.size()-(i+1)).equals(parziale.get(parziale.size()-(i+2)))) {
				return false;
			}
		}
		return true;
		
	}


}
