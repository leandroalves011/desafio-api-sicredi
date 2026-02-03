/*@author=LeandroAlves
Este arquivo contém as chamadas/requisições HTTP de forma encapsuladas
*/

package br.com.api.tests.clients;

import br.com.api.tests.models.DataModelBook;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class ClientBook {

    //Cadastra/cria livro
    public static Response createBook(DataModelBook dataModelBook) {
        return given()
                .contentType("application/json")
                .body(dataModelBook)
                .when()
                .post("/Books");
    }

    //Busca livro por ID
    public static Response getBook(int id) {
        return given()
                .when()
                .get("/Books/{id}", id);
    }

    //Busca todos os livros
    public static Response getAllBooks() {
        return given()
                .when()
                .get("/Books");
    }

    //Atualiza livro passando ID como parametro
    public static Response updateBook(int id, DataModelBook dataModelBook) {
        return given()
                .contentType("application/json")
                .body(dataModelBook)
                .when()
                .put("/Books/{id}", id);
    }

    //Deleta livro passando ID como parametro
    public static Response deleteBook(int id) {
        return given()
                .when()
                .delete("/Books/{id}", id);
    }

    //Retorna get da API sem token
    public static Response getAllBooksWithoutToken() {
        return given()
                .when()
                .get("/Books");
    }

    //Simula retorno get da API com token inválido
    public static Response getAllBooksWithInvalidToken() {
        return given()
                .header("Authorization", "Bearer token_invalido")
                .when()
                .get("/Books");
    }
}
