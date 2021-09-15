package it.softwareinside.football.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import it.softwareinside.football.model.Partite;
import it.softwareinside.football.model.Videos;
import it.softwareinside.football.repository.PartiteRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class ServicePartite {
	
	
	@Autowired
	private PartiteRepository partiteRepository;

	public List<Partite> takePartite() throws IOException {
		String urlAPI = "https://www.scorebat.com/video-api/v3/";
		ObjectMapper mapper = new ObjectMapper();
		OkHttpClient client = new OkHttpClient();
		ArrayList <Partite> listaPartite = new ArrayList<Partite>();
	    Request request = new Request.Builder()
	      .url(urlAPI)
	      .build();
	    
	    try (Response response = client.newCall(request).execute()){
	    	JsonNode actualObj = mapper.readTree(response.body().string());
	    	for(int i = 0; i < actualObj.get("response").size(); i++) {
	    		Partite partita = new ObjectMapper().readValue(actualObj.get("response").get(i).toString(), Partite.class);
	    		partita.setDate(actualObj.get("response").get(i).get("date").asText());
	    		partita.setDate(actualObj.get("response").get(i).get("date").asText().split("T")[0]);
	    		for(int j = 0;j<actualObj.get("response").get(i).get("videos").size();j++) {
	    			String title = actualObj.get("response").get(i).get("videos").get(j).get("title").asText();
	    			String embed = actualObj.get("response").get(i).get("videos").get(j).get("embed").asText();
	    			Videos video = new Videos(title,embed);
	    			partita.getVideo().add(video);
	    		}
	    		partiteRepository.save(partita);
	    		listaPartite.add(partita);
	    	}
	    	return listaPartite;
	    } catch(IOException e) {
	    	e.printStackTrace();
	    }
	    return null;
	}
	
	
	
	
	
	public List<Partite> trovaPartite(String titlePartita) {
		List <Partite> partite = new ArrayList<Partite>();
		titlePartita = titlePartita.toLowerCase();
		for(int i = 0; i < partiteRepository.findAll().size(); i++) {
			if(partiteRepository.findAll().get(i).getTitle().toLowerCase().contains(titlePartita)) 
				partite.add(partiteRepository.findAll().get(i));
		}
		return partite;
			
	}
	
	
	public List<Partite> trovaCampionato(String competition){
		List <Partite> partite = new ArrayList<Partite>();
		competition = competition.toLowerCase();
		for(int i = 0; i < partiteRepository.findAll().size(); i++) {
			if(partiteRepository.findAll().get(i).getCompetition().toLowerCase().contains(competition)) 
				partite.add(partiteRepository.findAll().get(i));
		}
		return partite;
	}
	
	public List<Partite> trovaData(String data){
		List <Partite> partite = new ArrayList<Partite>();
		data = data.toLowerCase();
		for(int i = 0; i < partiteRepository.findAll().size(); i++) {
			if(partiteRepository.findAll().get(i).getDate().toLowerCase().contains(data)) 
				partite.add(partiteRepository.findAll().get(i));
		}
		return partite;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
