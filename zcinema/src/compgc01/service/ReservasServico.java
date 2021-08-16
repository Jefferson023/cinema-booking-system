package compgc01.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicListHeaderIterator;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import compgc01.model.BookingHistoryItem;
import compgc01.model.Film;
import compgc01.model.Sessao;

public class ReservasServico {

	public Sessao getSessao(String token, String data, String titulo, String horario)
			throws ParseException, ClientProtocolException, IOException {
		String url = "https://zcinema-api-gateway.herokuapp.com/api/bookings/sessions?" + "date=" + data
				+ "&movie_title=" + titulo.replaceAll("\\s", "%20") + "&schedule=" + horario.replace(":", "%3A");
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
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
		JSONArray sessoesJson = (JSONArray) jsonRes.get("sessions");
		JSONObject sessaoJson = (JSONObject) sessoesJson.get(0);
		Long id = (Long) sessaoJson.get("id");
		Long movieId = (Long) sessaoJson.get("movie_id");
		double price = (double) sessaoJson.get("price");
		String movieTitle = (String) sessaoJson.get("movie_title");
		String description = (String) sessaoJson.get("description");
		String date = (String) sessaoJson.get("date");
		String schedule = (String) sessaoJson.get("schedule");

		List<Long> watchersId = (List<Long>) sessaoJson.get("session_watchers_id");
		List<String> availableSeats = (List<String>) sessaoJson.get("available_seats");
		List<String> reservedSeats = (List<String>) sessaoJson.get("reserved_seats");
		List<String> soldSeats = (List<String>) sessaoJson.get("sold_seats");

		Sessao sessao = new Sessao(id, movieId, price, movieTitle, description, date, schedule, watchersId,
				availableSeats, reservedSeats, soldSeats);

		return sessao;
	}

	@SuppressWarnings("unchecked")
	public List<Sessao> getAllSessao(String token) throws ParseException, ClientProtocolException, IOException {
		String url = "https://zcinema-api-gateway.herokuapp.com/api/bookings/sessions/all";
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
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
		JSONArray sessoesJson = (JSONArray) jsonRes.get("sessions");
		
		List<Sessao> sessoes = new ArrayList<Sessao>();
		
		sessoesJson.forEach(s -> {
			JSONObject sessaoJson = (JSONObject) s;
			Long id = (Long) sessaoJson.get("id");
			Long movieId = (Long) sessaoJson.get("movie_id");
			double price = (double) sessaoJson.get("price");
			String movieTitle = (String) sessaoJson.get("movie_title");
			String description = (String) sessaoJson.get("description");
			String date = (String) sessaoJson.get("date");
			String schedule = (String) sessaoJson.get("schedule");

			List<Long> watchersId = (List<Long>) sessaoJson.get("session_watchers_id");
			List<String> availableSeats = (List<String>) sessaoJson.get("available_seats");
			List<String> reservedSeats = (List<String>) sessaoJson.get("reserved_seats");
			List<String> soldSeats = (List<String>) sessaoJson.get("sold_seats");

			Sessao sessao = new Sessao(id, movieId, price, movieTitle, description, date, schedule, watchersId,
					availableSeats, reservedSeats, soldSeats);
			sessoes.add(sessao);
		});

		return sessoes;
	}

	@SuppressWarnings("unchecked")
	public List<BookingHistoryItem> getAllReservas(String token)
			throws ParseException, ClientProtocolException, IOException {
		String url = "https://zcinema-api-gateway.herokuapp.com/api/bookings/?page=0&limit=100";
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
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
		JSONArray reservasJson = (JSONArray) jsonRes.get("bookings");
		List<BookingHistoryItem> reservas = new ArrayList<BookingHistoryItem>();

		reservasJson.forEach(reservaJson -> {
			JSONObject reservaJson2 = (JSONObject) reservaJson;
			String status = (String) reservaJson2.get("status");
			Long userId = (Long) reservaJson2.get("user_id");
			String username = userId.toString();
			String firstName = "";
			String lastName = "";
			Long sessionId = (Long) reservaJson2.get("session_id");
			String film = "";
			String date = (String) reservaJson2.get("session_date");
			String time = "";
			// Double priceDouble = (Double) reservaJson2.get("price");
			// String price = priceDouble.toString();
			String seat = (String) reservaJson2.get("seat");
			Long id = (Long) reservaJson2.get("id");
			String idNumber = id.toString();
			BookingHistoryItem reserva = new BookingHistoryItem(status, username, firstName, lastName, film, date, time,
					seat, idNumber, sessionId);
			reservas.add(reserva);
		});

		return reservas;
	}

	public void cadastrar(Long userId, Long sessionId, double price, String type, String seat, String session_date)
			throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(
				"https://zcinema-api-gateway.herokuapp.com/api/bookings/" + userId + "/" + sessionId);
		;
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept", "application/json");
		JSONObject json = new JSONObject();

		// json.put("user_id", userId);
		// json.put("session_id", sessionId);
		// json.put("price", price);
		json.put("type", type);
		json.put("seat", seat);
		json.put("session_date", session_date);

		StringEntity entity = new StringEntity(json.toJSONString(), ContentType.APPLICATION_JSON);

		httpPost.setEntity(entity);

		CloseableHttpResponse response = client.execute(httpPost);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new IOException();
		}
	}

	public void cancelarReserva(Long userId, Long sessionId) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPatch httpPatch = new HttpPatch(
				"https://zcinema-api-gateway.herokuapp.com/api/bookings/" + userId + "/" + sessionId);
		;
		httpPatch.setHeader("Content-Type", "application/json");
		httpPatch.setHeader("Accept", "application/json");
		JSONObject json = new JSONObject();

		json.put("status", "CANCELED");

		StringEntity entity = new StringEntity(json.toJSONString(), ContentType.APPLICATION_JSON);

		httpPatch.setEntity(entity);

		CloseableHttpResponse response = client.execute(httpPatch);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new IOException();
		}
	}

}
