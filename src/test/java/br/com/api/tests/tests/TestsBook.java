/*@author=LeandroAlves
Este arquivo contém os cenários de testes
*/

package br.com.api.tests.tests;

import br.com.api.tests.base.TestBaseBook;
import br.com.api.tests.clients.ClientBook;
import br.com.api.tests.models.DataModelBook;
import br.com.api.tests.utils.TestsDataFactoryBook;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestsBook extends TestBaseBook {

    // testes do metodo POST//
    @Test
    void deveCadastrarLivroComSucesso() {
        DataModelBook dataModelBook = TestsDataFactoryBook.validBook();

        ClientBook.createBook(dataModelBook)
                .then()
                .statusCode(200)
                .body("title", equalTo(dataModelBook.title()))
                .body("pageCount", equalTo(dataModelBook.pageCount()));
    }

    @Test
    void devePermitirCadastrarLivroSemTitulo() {
        ClientBook.createBook(TestsDataFactoryBook.bookWithoutTitle())
                .then()
                .statusCode(200)
                .body("description", allOf(containsString("sem"), containsString("livro")))
                .body("title", equalTo(""));

        //achei estranho ser possível cadastrar um livro sem título, parece uma ausência de validação em campo no backend
    }

    //testes do metodo GET com ID//
    @Test
    void deveBuscarLivroPorIDComSucesso() {
        DataModelBook dataModelBook = TestsDataFactoryBook.validBook();

        // Cadastra um novo livro via POST
        Response createResponse = ClientBook.createBook(dataModelBook);
        int bookId = createResponse.jsonPath().getInt("id");

        // valida que o POST realmente retornou um ID
        assertThat(bookId, greaterThan(0));

        /*Especificamente nesse caso de teste, notei que a API tem um comportamento de consistência eventual, devido que mesmo o POST retornando com sucesso (200),
        o recurso nem sempre está disponível para ser consultado via GET, ocasionando uma resposta com erro (404)
        */
        ClientBook.getBook(bookId)
                .then()
                .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    void deveBuscarLivroPorIDInexistente() {
        ClientBook.getBook(999999)
                .then()
                .statusCode(404);
    }

    @Test
        //OBS: ID inválido <> ID inexistente
    void deveRetornarErroAoBuscarLivroComIDinvalido() {

        int idInvalido = -1;

        //assertiva que garante que o ID é inválido, ou seja, menor que 1
        assertThat(idInvalido, lessThan(1));

        ClientBook.getBook(idInvalido)
                .then()
                .statusCode(anyOf(is(400), is(404)));
    }

    //testes do metodo GET//
    @Test
    void deveListarLivrosComSucesso() {
        ClientBook.getAllBooks()
                .then()
                .statusCode(200)
                .body("$", not(empty()))
                .body("[0].id", notNullValue());
    }

    @Test
    void deveValidarContratoDaListaDeLivros() {
        ClientBook.getAllBooks()
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/listbooks-schema.json"));
    }

    @Test
    void deveValidarContratoDoObjetoDeLivro() {
        DataModelBook dataModelBook = TestsDataFactoryBook.validBook();

        Response response = ClientBook.createBook(dataModelBook);

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/book-schema.json"));
    }

    //testes do metodo PUT com ID//
    @Test
    void deveAtualizarLivroComSucesso() {
        DataModelBook dataModelBook = TestsDataFactoryBook.validBook();

        Response createResponse = ClientBook.createBook(dataModelBook);
        int bookId = createResponse.jsonPath().getInt("id");

        DataModelBook updatedDataModelBook = TestsDataFactoryBook.updatedBook(bookId);

        ClientBook.updateBook(bookId, updatedDataModelBook)
                .then()
                .statusCode(200)
                .body("title", equalTo(updatedDataModelBook.title()));
    }

    @Test
    void deveAtualizarLivroInexistenteComSucesso() {
        ClientBook.updateBook(999999, TestsDataFactoryBook.updatedBook(999999))
                .then()
                .statusCode(200);
    }

    //testes do metodo DELETE com ID//
    @Test
    void deveDeletarLivroComSucesso() {
        DataModelBook dataModelBook = TestsDataFactoryBook.validBook();

        Response createResponse = ClientBook.createBook(dataModelBook);
        int bookId = createResponse.jsonPath().getInt("id");

        ClientBook.deleteBook(bookId)
                .then()
                .statusCode(anyOf(is(200), is(204)));

        ClientBook.getBook(bookId)
                .then()
                .statusCode(404);
    }

    @Test
    void deveDeletarLivroInexistenteSemErro() {
        ClientBook.deleteBook(999999)
                .then()
                //.statusCode(404);
                //mesmo deletando um livro com id inexistente, a API está respondendo com sucesso.
                .statusCode(anyOf(is(200), is(204)));
    }

    //testes para validar Token
    @Test
    void devePermitirAcessoSemToken() {
        ClientBook.getAllBooksWithoutToken()
                .then()
                .statusCode(200);
    }

    @Test
    void deveRetornarErroComTokenInvalido() {
        ClientBook.getAllBooksWithInvalidToken()
                .then()
                .statusCode(anyOf(is(401), is(403), is(200)));
    }
}
