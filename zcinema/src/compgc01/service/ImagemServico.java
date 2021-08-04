package compgc01.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class ImagemServico {
	public static BufferedImage getImagem(String url) throws IOException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		
		CloseableHttpResponse response = client.execute(httpGet);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new IOException();
		}
		
		return ImageIO.read(response.getEntity().getContent());
	}
}
