package br.gcr.rocha.rest;

import static io.restassured.RestAssured.given;

import static io.restassured.RestAssured.request;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.Test;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {

	@Test
	public void testeOlaMundo() {
		Response request = request(Method.GET, "https://restapi.wcaquino.me/ola");
		assertEquals(200, request.statusCode());
		assertEquals("Ola Mundo!", request.getBody().asString());
	}
	
	@Test
	public void testeConhecerOutrasFormasRestAssured() {
		Response request = request(Method.GET, "https://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = request.then();
		//validacao.statusCode(200);
		
		given() //Pré-condição
		.when()
			.get("https://restapi.wcaquino.me/ola").
		then()
			.statusCode(200);
	}
	
	@Test
	public void testeConhecerMatchersHamcrest() {
		assertThat("Maria", not("Pedro"));
		assertThat("Joaquina", anyOf(is("Maria"), is("Joaquina")));
		assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui")));
	}
	
	@Test 
	public void testeValidarBody() {
		given() //Pré-condição
		.when()
			.get("https://restapi.wcaquino.me/ola").
		then()
			.statusCode(200)
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(not(nullValue()));
	}
}
