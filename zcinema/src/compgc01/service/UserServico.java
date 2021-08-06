package compgc01.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import compgc01.model.Film;
import compgc01.model.User;

public class UserServico {

	public UserServico() {
		super();
	}

	public void atualizarInformacoes(User usuario, String token) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPatch httpPatch = new HttpPatch("https://zcinema-api-gateway.herokuapp.com/api/users/" + usuario.getId());
		httpPatch.setHeader("Content-Type", "application/json");
		httpPatch.setHeader("Accept", "application/json");
		httpPatch.setHeader("Authorization", "Bearer "+token);

		StringEntity entity = new StringEntity(usuario.toJSONString(), ContentType.APPLICATION_JSON);

		httpPatch.setEntity(entity);

		CloseableHttpResponse response = client.execute(httpPatch);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new IOException();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getAll(String token) throws ParseException, IOException, org.json.simple.parser.ParseException{
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet("https://zcinema-api-gateway.herokuapp.com/api/users/");
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
		Set keys = jsonRes.keySet();

		List<User> usuarios = new ArrayList<User>();

		keys.forEach(key -> {
			JSONObject userJson = (JSONObject) jsonRes.get(key);
			String firstName = (String) userJson.get("first_name");
			String lastName = (String) userJson.get("last_name");
			String email = (String) userJson.get("email");
			String avatar = (String) userJson.get("avatar");
			String profile = (String) userJson.get("profile");
			Long id = (Long) userJson.get("id");
			User user = new User(firstName, lastName, "", "", email, avatar, profile, id);
			usuarios.add(user);
		});
		
		return usuarios;
	}
}
