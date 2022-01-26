package br.gcr.rocha.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class UserXMLTest {
	
	public static RequestSpecification reqSpec;
	public static ResponseSpecification resSpec;
	
	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.log(LogDetail.ALL);
		reqSpec = reqBuilder.build();
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		resBuilder.expectStatusCode(200);
		resSpec = resBuilder.build();
	}

	@Test
	public void testeTrabalharComXML() {
		
		
		given()
		.spec(reqSpec)
		.when()
		.get("/usersxml/3")
		.then()
		.spec(resSpec)
		//.statusCode(200)
		.rootPath("user")
		.body("name", is("Ana Julia"))
		.body("@id", is("3"))
		.rootPath("user.filhos")
		.detachRootPath("filhos") //remove elementos ao rootpath
		.appendRootPath("filhos") //adiciona elementos ao rootpath
		.body("name.size()", is(2))
		.body("name[0]", is("Zezinho"))
		.body("name[1]", is("Luizinho"))
		.body("name", hasItem("Luizinho"))
		.body("name", hasItems("Luizinho", "Zezinho"))
		;
	}
	
	@Test
	public void testeAlternativasDeUso() {	
		given()
		//.log().all())
		.when()
		.get("/users")
		.then()
		.statusCode(200);
	}
	
	@Test
	public void testeFazerPesquisasAvancadasXML() {
		given()
		.when()
		.get("/usersxml")
		.then()
		.statusCode(200)
		.rootPath("users.user")
		.body("size()", is(3))
		.body("findAll{it.age.toInteger() <= 25}.size()", is(2))
		.body("@id", hasItems("1","2","3"))
		.body("findAll{it.@id.toInteger() == 3}.name", is("Ana Julia"))
		.body("findAll{it.age.toInteger() == 25}.name", is("Maria Joaquina"))
		.body("findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
		.body("age.collect{it.toInteger() * 2}", hasItems(60, 40, 50))
		.body("name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"))
		;
	}
	
	@Test
	public void testeUnirXMLPathEJava() {
		ArrayList<NodeImpl> nomesComN = given()
		.when()
		.get("/usersxml")
		.then()
		.statusCode(200).extract().path("users.user.name.findAll{it.toString().contains('n')}");
		
		assertEquals(nomesComN.size(), 2);
		assertEquals(nomesComN.get(0).toString().toUpperCase(), "MARIA JOAQUINA");
		assertTrue("ANA JULIA".equalsIgnoreCase(nomesComN.get(1).toString()));
}
	
	@Test
	public void testePesquisasAvancadasComXPath() {
		given()
		.when()
		.get("/usersxml")
		.then()
		.statusCode(200)
		.body(hasXPath("count(/users/user)", is("3")))
		.body(hasXPath("//name[text() = 'Zezinho']/../../name", is("Ana Julia")))
		.body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinho"),containsString("Luizinho"))))
		.body(hasXPath("/users/user/name[contains(.,'n')]"))
		.body(hasXPath("//user[age < 30 and age > 20]/name", is("Maria Joaquina")))
		.body(hasXPath("//user[age < 30][age > 20]/name", is("Maria Joaquina")))
		;
	}
}
