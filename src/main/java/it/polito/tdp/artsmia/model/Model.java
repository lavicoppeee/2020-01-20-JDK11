package it.polito.tdp.artsmia.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private ArtsmiaDAO dao;
	private List<ArtConnessi> connessi;
	private Graph<Integer,DefaultWeightedEdge> graph;
	private List<Integer> best;
	
	public Model() {
		dao=new ArtsmiaDAO();
	}
	
	public List<String> getRuoli(){
	 return dao.getRuoli();
	}
	
	public void creaGrafo(String ruolo) {
		
		this.graph=new SimpleWeightedGraph<Integer,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		Graphs.addAllVertices(this.graph, this.dao.getArtist(ruolo));
		
		//aggiungo gli archi, in modo intelligente
		//fare bene la query
		
		connessi=dao.getConnessi(ruolo);
		
		for(ArtConnessi a:connessi) {
			if(this.graph.getEdge(a.getArt1(), a.getArt2())==null) {
				Graphs.addEdgeWithVertices(this.graph, a.getArt1(), a.getArt2(),a.getPeso());
			}
		}		
	}
	
	public int vertici() {
		return this.graph.vertexSet().size();
	}
	
	public int archi() {
		return this.graph.edgeSet().size();
	}
	
	public List<ArtConnessi> getConnessi(){
		return this.connessi;
	}
	
	/**
	 * Inizializzazione della ricorsione con richiamo dentro di essa.
	 * Richiamo la lista parziale 
	 * @param partenza
	 * @return
	 */
	public List<Integer> trovaPercorso(Integer partenza){
		
		best=new LinkedList<Integer>();
		List<Integer> parziale=new ArrayList<Integer>();
		
		parziale.add(partenza);
		ricorsione(parziale,-1);
		
		return best;
	}
	
	private void ricorsione(List<Integer> parziale, int peso) {
		
		
		Integer last=parziale.get(parziale.size() -1 );
		
		//ottengo i vicini
		List<Integer> vicini= Graphs.neighborListOf(this.graph, last);
		
		//li scorro tutti finche non finiscono tutti i vicini 
		for(Integer vicino: vicini) {
			 if(!parziale.contains(vicino) && peso==-1) {
				 parziale.add(vicino);
				 //devo aggiornare il peso della ricorsione con il peso dell'arco
				 ricorsione(parziale, (int) this.graph.getEdgeWeight(this.graph.getEdge(last, vicino)));
			     ricorsione(parziale,-1); //backtracking
			     
			 }else {
				 if(!parziale.contains(vicino) && this.graph.getEdgeWeight(this.graph.getEdge(last,vicino))==peso) {
					 parziale.add(vicino);
					 ricorsione(parziale,peso);
					 parziale.remove(vicino);
				 }
			 }
		}
		
		if(parziale.size()>best.size()) {
			this.best=new ArrayList<>(parziale);
		}
		
		
	}
	
	public boolean esisteId(Integer id) {
		if(this.graph.containsVertex(id))
			return true;
		return false;
	}
}
