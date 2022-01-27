package br.gcr.rocha.rest;

import static io.restassured.RestAssured.*;

import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.*;

public class HTMLTest {

	
	@Test
	public void testeFazerBuscasComHTML() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.body("html.body.div.table.tbody.tr.size()", is(3))
			.body("html.body.div.table.tbody.tr[1].td[2]", is("25"))
			.appendRootPath("html.body.div.table.tbody")
			.body("tr.find{it.toString().startsWith('2')}.td[1]", is("Maria Joaquina"))
			;
	}
	
	@Test
	public void testeFazerBuscasComXpathEmHTML() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users?format=clean")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.body(hasXPath("count(//table/tr)", is("4")))
			;
	}
}
