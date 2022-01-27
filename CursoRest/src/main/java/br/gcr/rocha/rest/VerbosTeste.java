package br.gcr.rocha.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
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
	
	@Test
	public void testeSalvarUsuarioUsandoMap() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuário via map");
		params.put("age", 25);
		
		given()
		.log().all()
		.contentType("application/json")
		.body(params)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
		.log().all()
		.statusCode(201)
		.body("id", is(notNullValue()))
		.body("name", is("Usuário via map"))
		.body("age", is(25))
		;
	
	}
	
	@Test
	public void testeSalvarUsuarioUsandoObjeto() {
		User user = new User("Usuario via objeto", 35);
		
		given()
		.log().all()
		.contentType("application/json")
		.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
		.log().all()
		.statusCode(201)
		.body("id", is(notNullValue()))
		.body("name", is("Usuario via objeto"))
		.body("age", is(35))
		;
	}
	
	@Test
	public void testeDeserializarObjetoAoSalvarObjeto() {
		User user = new User("Usuario deserializado", 35, 2000.00);
		
		User usuarioInserido = given()
		.log().all()
		.contentType("application/json")
		.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
		.log().all()
		.statusCode(201)
		.extract().body().as(User.class)
		;
		System.out.println(usuarioInserido);
		assertEquals("Usuario deserializado", usuarioInserido.getName());
		assertThat(usuarioInserido.getAge(), is(35));
		assertThat(usuarioInserido.getId(), is(notNullValue()));
		assertThat(usuarioInserido.getSalary(), is(2000.00));
	}
	
	@Test
	public void testeSalvarUsuarioViaXMLUsandoObjeto() {
		User user = new User("Usuario XML", 45);
		
		given()
		.log().all()
		.contentType(ContentType.XML)
		.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersxml")
		.then()
		.log().all()
		.statusCode(201)
		.body("id", is(notNullValue()))
		.body("user.name", is("Usuario XML"))
		.body("user.age", is("45"))
		;
	
	}
	
	@Test
	public void testeDeserializarXMLAoSalvarUsuario() {
		
		User user = new User("Usuario XML", 45);
			
		User usuarioInserido = given()
		.log().all()
		.contentType(ContentType.XML)
		.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersxml")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
			;
		System.out.println(usuarioInserido);
		}
	}

