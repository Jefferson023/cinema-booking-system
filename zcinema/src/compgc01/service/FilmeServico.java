package compgc01.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import compgc01.model.Film;

public class FilmeServico {

	public FilmeServico() {
		super();
	}

	@SuppressWarnings({ "unchecked", "unchecked", "unchecked" })
	public List<Film> getAll(String token) throws IOException, ParseException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet("https://zcinema-api-gateway.herokuapp.com/api/movies?" + "title=&page=0&limit=100");
		httpGet.setHeader("Content-Type", "application/json");
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Authorization", "Bearer " + token);

		CloseableHttpResponse response = client.execute(httpGet);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new IOException();
		}

		String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

		JSONParser parser = new JSONParser();
		JSONObject jsonRes = (JSONObject) parser.parse(responseBody);
		JSONArray filmesJson = (JSONArray) jsonRes.get("movies");
		List<Film> filmes = new ArrayList<Film>();

		filmesJson.forEach(filme -> {
			JSONObject filmJson = (JSONObject) filme;
			Long id = (Long) filmJson.get("id");
			String title = (String) filmJson.get("title");
			String description = (String) filmJson.get("description");
			String trailer = (String) filmJson.get("trailer");
			String startDate = (String) filmJson.get("start_date");
			String endDate = (String) filmJson.get("end_date");
			List<String> times = new ArrayList<String>();
			((JSONArray) filmJson.get("schedules")).forEach(horario -> times.add((String) horario));
			String banner = (String) filmJson.get("banner");
			filmes.add(new Film(id, title, description, trailer, startDate, 
		    		endDate, times, banner));
		});
		
		return filmes;
	}
	
	@SuppressWarnings("unchecked")
	public void cadastrar(String title, 
		      String description, 
		      String trailer,
		      String start_date,
		      String end_date,
		      List<String> schedules, byte[] file, String token) throws IOException {
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("https://zcinema-api-gateway.herokuapp.com/api/movies");
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Authorization", "Bearer "+token);
		JSONObject json = new JSONObject();
		
		json.put("title", title);
		json.put("description", description);
		json.put("trailer", trailer);
		json.put("start_date", start_date);
		json.put("end_date", end_date);
		json.put("schedules", schedules);
		
		//json.put("file", Base64.getEncoder().encode(file.toString().getBytes()));
		
		StringEntity entity = new StringEntity(json.toJSONString(), ContentType.APPLICATION_JSON);

		httpPost.setEntity(entity);

		CloseableHttpResponse response = client.execute(httpPost);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new IOException();
		}
	}
	
	public void remover(Film filme, String token) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpDelete httpDelete = 
				new HttpDelete("https://zcinema-api-gateway.herokuapp.com/api/movies/"+filme.getId());
		httpDelete.setHeader("Content-Type", "application/json");
		httpDelete.setHeader("Accept", "application/json");
		httpDelete.setHeader("Authorization", "Bearer "+token);
		
		CloseableHttpResponse response = client.execute(httpDelete);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new IOException();
		}
	}
}
