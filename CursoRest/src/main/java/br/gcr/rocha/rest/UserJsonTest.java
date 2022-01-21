package br.gcr.rocha.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserJsonTest {
	@Test
	public void testeVerificarPrimeiroNivel() {
		given().when().get("https://restapi.wcaquino.me/users/1").then().statusCode(200).body("id", is(1))
				.body("name", containsString("Silva")).body("age", greaterThan(18));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testeVerificarPrimeiroNivelOutrasFormas() {
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/users/1");

		// Path
		assertEquals(new Integer(1), response.path("id"));
		assertEquals(new Integer(1), response.path("%s", "id"));

		// JsonPath
		JsonPath jpath = new JsonPath(response.asString());
		assertEquals(1, jpath.getInt("id"));

		// from
		int id = JsonPath.from(response.asString()).getInt("id");
		assertEquals(1, id);

	}

	
	@Test
	public void testeVerificarSegundoNivel() {
		given().when().get("https://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
		.body("id", is(2))
		.body("name", containsString("Joaquina"))
		.body("age", greaterThan(18))
		.body("endereco.rua", is("Rua dos bobos"))
		;
	}
	
	@Test
	public void testeValidarMaisDeUmaOcorrencia() {
		given().when().get("https://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
		.body("id", is(3))
		.body("name", containsString("Ana"))
		.body("age", greaterThan(18))
		.body("filhos", hasSize(2))
		.body("filhos[0].name", is("Zezinho"))
		.body("filhos[1].name", is("Luizinho"))
		.body("filhos.name", hasItem("Luizinho"))
		.body("filhos.name", hasItems("Zezinho","Luizinho"))
		;

	}
}
