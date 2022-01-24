package br.gcr.rocha.rest;

import static io.restassured.RestAssured.request;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundo {
	
	public static void main(String[] args) {
		Response request = request(Method.GET, "https://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = request.then();
		validacao.statusCode(200);
		System.out.println(request.getBody().asString().equals("Ola Mundo!"));
		System.out.println(request.statusCode() == 200);
	}
}
