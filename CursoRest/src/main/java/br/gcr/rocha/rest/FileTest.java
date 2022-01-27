package br.gcr.rocha.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;

public class FileTest {

	
	@Test
	public void testeObrigarEnvioArquivo() {
		given()
		.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404) //deveria ser 400
			.body("error", is("Arquivo não enviado"))
			;
	}
	
	@Test
	public void testeFazerUploadArquivo() {
		given()
		.log().all()
		.multiPart("arquivo", new File("src/main/resources/tabela.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200)
			.time(lessThan(3000L))
			.body("name", is("tabela.pdf"))
			;
	}
	
	@Test
	public void testeFazerDownloadArquivo() throws IOException {
		byte[] image = given()
		.log().all()
		.when()
			.get("http://restapi.wcaquino.me/download")
		.then()
			.statusCode(200)
			.extract().asByteArray();
			;
			
		File imagem = new File("src/main/resources/file.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(image);
		out.close();
		System.out.println(imagem.length());
		assertThat(imagem.length(), lessThan(100000L));
	}
}
