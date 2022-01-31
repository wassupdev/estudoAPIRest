package br.gcr.rocha.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;



public class AuthTest {
	
	String tokenOpenWeatherMap;

	@Test
	public void testeSWAPI() {
		given()
			.log().all()
		.when()
			.get("https://swapi.dev/api/people/1/")
		.then()
			.log().all()
			.body("name", is("Luke Skywalker"))	
			.statusCode(200)
		;
	}
	
	//c968deb7749e3159b7291c4fe38525a9

	@Test
	public void testeAPIOpenWeather() {
		given()
		.log().all()
		.queryParam("q", "Sao Paulo")
		.queryParam("appid", tokenOpenWeatherMap)
		.queryParam("units", "metric")
	.when()
		.get("https://api.openweathermap.org/data/2.5/weather")
	.then()
		.log().all()
		.body("name", is("São Paulo"))	
		.body("sys.country", is("BR"))
		.body("main.temp", lessThan(31.0f))
		.body("coord.lon", is(-46.6361f))
		.statusCode(200)
		;
	}
	
	@Test
	public void testeNaoAcessarSemSenha() {
		given()
		.log().all()
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)
		;
	}
	
	@Test
	public void testeFazerAutenticacaoBasica() {
		given()
		.log().all()
		.when()
			.get("https://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void testeFazerAutenticacaoBasica2() {
		given()
		.log().all()
		.auth().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void testeFazerAutenticacaoBasicaChallenge() {
		given()
		.log().all()
		.auth().preemptive().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	//email: nao2022@nao2022
	//senha: nao2022
	
	@Test
	public void testeFazerAutenticacaoComTokenJWT() {
		Map<String, String> login  = new HashMap<String, String>();
		login.put("email", "nao2022@nao2022");
		login.put("senha", "nao2022");
		
		String token = given()
		.log().all()
		.body(login)
		.contentType(ContentType.JSON)
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token")
		;
		 
		given()
			.log().all()
			.header("Authorization", "JWT " + token)
			.when()
				.get("http://barrigarest.wcaquino.me/contas")
			.then()
				.log().all()
				;
	}
	
	@Test
	public void testeAcessarAplicacaoWeb() {
		//login
		String cookie = given()
		.log().all()
		.formParam("email", "gcr@gcr")
		.formParam("senha", "gcr")
		.contentType(ContentType.URLENC.withCharset("UTF-8"))
		.when()
			.post("http://seubarriga.wcaquino.me/logar")
		.then()
			.log().all()
			.statusCode(200)
			.extract().header("set-cookie")
		;
		cookie = cookie.split("=")[1].split(";")[0];
	
		
		//obter conta
		String path = given()
		.log().all()
		.cookie("connect.sid", cookie)
		.when()
			.get("http://seubarriga.wcaquino.me/contas")
		.then()
			.log().all()
			.statusCode(200)
			.body("html.body.table.tbody.tr[0].td[0]", is("Conta para alterar"))
			.extract().body().asString()
			;
		System.out.println();
		XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, path);
		System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
		//http://seubarriga.wcaquino.me/contas
	}
	
	}
