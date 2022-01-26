package br.gcr.rocha.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

public class VerbosTeste {

	@Test
	public void testeSalvarUsuario() {
		given()
		.log().all()
		.contentType("application/json")
		.body("{\"name\" : \"CarlosAlberto\", \"age\" : 56}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
		.log().all()
		.statusCode(201)
		.body("id", is(notNullValue()))
		.body("name", is("CarlosAlberto"))
		.body("age", is(56))
		;
	}
	
	@Test
	public void testeNaoSalvarUsuarioSemNome() {
		given()
		.log().all()
		.contentType("application/json")
		.body("{\"age\" : 56}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
		.log().all()
		.statusCode(400)
		.body("id", is(nullValue()))
		.body("error", is("Name é um atributo obrigatório"))
		
		;
	}
	
	@Test
	public void testeSalvarUsuarioComXML() {
		given()
		.log().all()
		.contentType(ContentType.XML)
		.body("<user><name>Ednaldo</name><age>50</age></user>")
		.when()
			.post("https://restapi.wcaquino.me/usersxml")
		.then()
		.log().all()
		.statusCode(201)
		.body("id", is(notNullValue()))
		.body("user.name", is("Ednaldo"))
		.body("user.age", is("50"))
		//.body("name", is("Ednaldo"))
		//.body("age", is(50))
		;
	}
	
	@Test
	public void testeAlterarUsuario() {
		given()
		.log().all()
		.contentType("application/json")
		.body("{\"name\" : \"UsuarioAlterado\", \"age\" : 69}")
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
		.log().all()
		.statusCode(200)
		.body("id", is(1))
		.body("name", is("UsuarioAlterado"))
		.body("age", is(69))
		;
	
}
	@Test
	public void testeCustomizarURL() {
		given()
		.log().all()
		.contentType("application/json")
		.body("{\"name\" : \"UsuarioAlterado\", \"age\" : 69}")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
		.then()
		.log().all()
		.statusCode(200)
		.body("id", is(1))
		.body("name", is("UsuarioAlterado"))
		.body("age", is(69))
		;
	
}	
	
	@Test
	public void testeCustomizarURLParte2() {
		given()
		.log().all()
		.contentType("application/json")
		.body("{\"name\" : \"UsuarioAlterado\", \"age\" : 69}")
		.pathParam("entidade", "users")
		.pathParam("userId", 1)
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
		.log().all()
		.statusCode(200)
		.body("id", is(1))
		.body("name", is("UsuarioAlterado"))
		.body("age", is(69))
		;
}	
	
	
	@Test
	public void testeRemoverUsuario() {
		given()
		.log().all()
		.contentType(ContentType.JSON)
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
		.log().all()
		.statusCode(204)
		;
	}
	
	@Test
	public void testeNaoRemoverUsuarioInexistente() {
		given()
		.log().all()
		.contentType(ContentType.JSON)
		.when()
			.delete("https://restapi.wcaquino.me/users/5")
		.then()
		.log().all()
		.statusCode(400)
		.body("error", is("Registro inexistente"))
		;
	}
}
