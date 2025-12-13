package client;

import servico.Bookmarket;
import servico.Bookstore;

import dominio.Book;

import recommendation.RecommendationCorrelationSimilarity;
import recommendation.RecommendationSettings;
import recommendation.RecommendationUserSimilarity;

import java.util.List;
import java.util.ArrayList;
import java.lang.System;

/**
 * BookMarketClient is a sample client application that demonstrates how to interact
 * with the Bookmarket system. It initializes the market, populates it with data,
 * and retrieves and displays information about books and their authors.
 * <p>
 * This class serves as an entry point for testing and showcasing the main features
 * of the Bookmarket architecture, such as initialization, data population, and book retrieval.
 * </p>
 */
public class BookMarketClient {

	/**
	 * Main method to demonstrate the initialization and usage of the Bookmarket system.
	 * <p>
	 * This method creates a Bookmarket instance, populates it with sample data, retrieves
	 * a list of books, and prints their details and authors to the console.
	 * </p>
	 * @param args Command-line arguments (not used).
	 */
	public static void main(String[] args) {
		ArrayList<Book> books = new ArrayList<Book>();

		RecommendationSettings settings = new RecommendationSettings(
			RecommendationCorrelationSimilarity.pearson,
			RecommendationUserSimilarity.nearestUserNeighborhood,
			2
		);

		Bookmarket fleaMarket = new Bookmarket();
		Bookmarket.init(100, settings);

		fleaMarket.populate(10000, 10000, 10000, 1000, 10000, 10000, 1000);
		for (int i = 0; i < 100; i++) {
			books.add(i, fleaMarket.getBook(i));
			System.out.println(books.get(i).toString() + " " + books.get(i).getAuthor().toString());
		}
	}

}
