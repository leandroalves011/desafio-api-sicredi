/*@author=LeandroAlves
Este arquivo contém os dados de teste reaproveitáveis
*/

package br.com.api.tests.utils;
import br.com.api.tests.models.DataModelBook;
import javax.xml.crypto.Data;
import java.time.Instant;

public class TestsDataFactoryBook {

    //Cadastra um livro válido (POST)
    public static DataModelBook validBook() {
        return new DataModelBook(
                9999,
                "Livro API Teste",
                "Descrição do livro",
                120,
                "Resumo do livro",
                Instant.now().toString()
        );
    }

    //Atualiza um livro (PUT)
    public static DataModelBook updatedBook(Integer id) {
        return new DataModelBook(
                id,
                "Livro Atualizado",
                "Descrição atualizada",
                200,
                "Resumo atualizado",
                Instant.now().toString()
        );
    }

    //Cadastra um livro inválido sem titulo (POST negativo)
    public static DataModelBook bookWithoutTitle() {
        return new DataModelBook(
                9999,
                "",
                "Descrição de livro sem titulo",
                120,
                "Resumo de livro sem titulo",
                Instant.now().toString()
        );
    }
}

