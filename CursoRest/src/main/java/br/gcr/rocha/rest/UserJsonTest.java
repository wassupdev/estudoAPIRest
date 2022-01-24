package br.gcr.rocha.rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
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
	
	@Test
	public void testeValidarUsuarioInexistente() {
		given().when().get("https://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
		.body("error", is("Usu�rio inexistente"))
			;
	}
	
	@Test
	public void testeVerificarEstruturaNaRaiz() {
		given().when().get("https://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("name", hasItems("Jo�o da Silva", "Maria Joaquina", "Ana J�lia"))
			.body("age[1]", is(25))
			.body("filhos.name", hasItem(Arrays.asList("Zezinho","Luizinho")))
			.body("salary", contains(1234.5677f, 2500, null))
			;
	}
	
	@Test
	public void testeVerificacoesAvancadas() {
		given().when().get("https://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("name", hasItems("Jo�o da Silva", "Maria Joaquina", "Ana J�lia"))
			.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
			.body("findAll{it.age <= 25}[0].name", is("Maria Joaquina"))
			.body("findAll{it.age <= 25}[1].name", is("Ana J�lia"))
			.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana J�lia"))
			.body("findAll{it.name.length() < 10}.name", hasItems("Ana J�lia"))
			//.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
			.body("age.collect{it * 2}", hasItems(60, 50, 40))
			.body("id.max()", is(3))
			.body("salary.min()", is(1234.5678f))
			.body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f, 0.001)))
			.body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)))
			;
	}
	
	@Test
	public void testeUnirJsonpathComJava() {
		ArrayList<String> nomes =
		given().when().get("https://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
		.extract().path("name.findAll{it.startsWith('Maria')}")
		;
		assertEquals(1, nomes.size());
		assertTrue(nomes.get(0).equalsIgnoreCase("Maria joaquina"));
		assertEquals(nomes.get(0).toUpperCase(), "Maria Joaquina".toUpperCase());
	}
	
}
