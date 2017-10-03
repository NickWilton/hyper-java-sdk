package com.guidedsoftware.hyper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import uk.co.lucasweb.aws.v4.signer.HttpRequest;
import uk.co.lucasweb.aws.v4.signer.credentials.AwsCredentials;
import uk.co.lucasweb.aws.v4.signer.hash.Sha256;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

/**
 * Created by nwilton on 12/4/17.
 */
public class Hyper {

	private String accessKey;
	private String secretKey;
	private String registryAuth;
	private Region region = Region.US_WEST_1;

	private Hyper(String accessKey, String secretKey) {
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.registryAuth = null;
	}

	public void setRegistryAuth(String registryUsername, String registryPassword, String registryEmail) {

		String authString = "{\"username\": \"" + registryUsername + "\", \"password\": \"" + registryPassword + "\", \"email\": \"" + registryEmail + "\",\n" +
				"   \"serveraddress\" : \"index.docker.io\", \"auth\": \"\"}";

		registryAuth = Base64.getEncoder().encodeToString(authString.getBytes());

	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public static Hyper getInstance(String accessKey, String secretKey) {
		return new Hyper(accessKey, secretKey);
	}

	public JsonNode performRequest(RequestType requestType, String command, String body) throws HyperException {

		if (!command.startsWith("/")) {
			command = "/" + command;
		}

		Client client = Client.create();
		URI uri;
		try {
			uri = new URI(region.getEndpoint() + command);
		} catch (URISyntaxException ex) {
			throw new HyperException("Invalid command", ex);
		}

		String contentSha256 = Sha256.get(body == null ? "" : body, StandardCharsets.UTF_8);

		SimpleDateFormat dateFormatUtc = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
		dateFormatUtc.setTimeZone(TimeZone.getTimeZone("UTC"));

		String date = dateFormatUtc.format(new Date());

		HttpRequest request = new HttpRequest(requestType.toString(), uri);


		HyperSigner.Builder builder = HyperSigner.builder()
				.awsCredentials(new AwsCredentials(accessKey, secretKey))
				.header("Content-Type", "application/json")
				.header("X-Hyper-Date", date)
				.header("X-Hyper-Content-Sha256", contentSha256);

		if (registryAuth != null) {
			builder = builder.header("X-Registry-Auth", registryAuth);
		}

		String signature = builder
				.header("Host", region.getEndpoint())
				.region(region.getName())
				.build(request, "com/guidedsoftware/hyper", contentSha256)
				.getSignature();

		WebResource resource = client.resource(uri);
		WebResource.Builder builder1 = resource
				.header("Host", region.getEndpoint())
				.header("Authorization", signature)
				.header("Content-Type", "application/json")
				.header("X-Hyper-Content-Sha256", contentSha256)
				.header("X-Hyper-Date", date);

		if (registryAuth != null) {
			builder1 = builder1.header("X-Registry-Auth", registryAuth);
		}

		ClientResponse response = builder1
				.entity(body)
				.method(requestType.toString(), ClientResponse.class);

		String output = convertStreamToString(response.getEntityInputStream());

		// Do Connection Here

		try {
			if (response.getStatus() < 200 || response.getStatus() > 299) {
				throw new HyperResponseException(response.getStatus(), output);
			}
		} finally {
			response.close();
		}

		if (output.trim().isEmpty()) {
			return null;
		} else {
			try {

				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonRoot = objectMapper.readTree(output);

				return jsonRoot;

			} catch (IOException ex) {
				throw new HyperException("Failed to parse Json: " + ex.getMessage());
			}
		}
	}

	private static String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
