package servico;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.util.List;

// For testing: mvn compile exec:java "-Dexec.mainClass=servico.MahoutUserBasedRecommender"

public class MahoutUserBasedRecommender {

    public static void main(String[] args) {
        try {
            File file = new File("dummy_data/example.csv");
            if (!file.exists()) {
                System.out.println("Please create a 'dummy_data/example.csv' file in the project root to run this example.");
                return;
            }
            
            // 1. Data Model
            // Load the data from a CSV file. Format should be: userID,itemID,preference
            DataModel model = new FileDataModel(file);

            // 2. User Similarity
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

            // 3. User Neighborhood
            // Define a neighborhood of users. Here we take the nearest 2 users.
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);

            // 4. Recommender
            // Create the user-based recommender using the model, neighborhood, and similarity.
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            // 5. Get Recommendations for user ID = 1
            int[] userIds = {1};
            
            for (int userId : userIds) {
                System.out.println("\n=== DEBUG: User " + userId + " Analysis ===");
                try {
                    // Show what user has rated
                    long[] userItems = model.getItemIDsFromUser(userId).toArray();
                    System.out.println("User " + userId + " rated " + userItems.length + " items:");
                    for (long item : userItems) {
                        System.out.println("  Item " + item + ": " + model.getPreferenceValue(userId, item));
                    }
                    
                    // Show neighbors and similarities
                    long[] neighbors = neighborhood.getUserNeighborhood(userId);
                    System.out.println("Neighbors for User " + userId + ":");
                    if (neighbors.length == 0) {
                        System.out.println("  No neighbors found!");
                    } else {
                        for (long neighbor : neighbors) {
                            double sim = similarity.userSimilarity(userId, neighbor);
                            System.out.println("  User " + neighbor + " (similarity: " + String.format("%.4f", sim) + ")");
                            
                            // Show what items neighbor has that user doesn't
                            long[] neighborItems = model.getItemIDsFromUser(neighbor).toArray();
                            System.out.println("    Neighbor " + neighbor + " has items that User " + userId + " doesn't:");
                            boolean hasNewItems = false;
                            for (long neighborItem : neighborItems) {
                                boolean userHasItem = false;
                                for (long userItem : userItems) {
                                    if (userItem == neighborItem) {
                                        userHasItem = true;
                                        break;
                                    }
                                }
                                if (!userHasItem) {
                                    hasNewItems = true;
                                    float estimate = recommender.estimatePreference(userId, neighborItem);
                                    System.out.println("      Item " + neighborItem + " (estimated preference: " + estimate + ")");
                                }
                            }
                            if (!hasNewItems) {
                                System.out.println("      No unique items from this neighbor");
                            }
                        }
                    }
                    
                    // Get recommendations
                    List<RecommendedItem> recommendations = recommender.recommend(userId, 5);
                    System.out.println("RECOMMENDATIONS for User " + userId + ":");
                    if (recommendations.isEmpty()) {
                        System.out.println("  No recommendations found!");
                    } else {
                        System.out.println("  Found " + recommendations.size() + " recommendations:");
                        for (int i = 0; i < recommendations.size(); i++) {
                            RecommendedItem recommendation = recommendations.get(i);
                            System.out.println("  " + (i + 1) + ". Item " + recommendation.getItemID() + 
                                             " | Estimated Preference: " + String.format("%.2f", recommendation.getValue()));
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Error analyzing User " + userId + ": " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
