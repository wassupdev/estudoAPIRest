package br.gcr.rocha.rest;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXParseException;

import io.restassured.RestAssured;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.module.jsv.JsonSchemaValidator;

import static org.junit.jupiter.api.Assertions.*;

public class SchemaTest {

	@Test
	public void testeValidarSchemaXML() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
			;
	}
	
	@Test
	
	public void testeNaoDeveValidarXMLInvalido() {
		
		SAXParseException assertThrows = assertThrows(SAXParseException.class,  () -> {
			given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/invalidusersXML")
		.then()
			.log().all()
			.statusCode(200)
			.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
			;
		});	
	}
	
	@Test
	public void testeValidarSchemaJson() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(200)
			.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("users.json"))
			;
	}
}
