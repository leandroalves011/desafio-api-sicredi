/*@author=LeandroAlves
Este arquivo contém/representa o modelo de dados
*/

package br.com.api.tests.models;

public record DataModelBook(
        Integer id,
        String title,
        String description,
        Integer pageCount,
        String excerpt,
        String publishDate
) {
}

