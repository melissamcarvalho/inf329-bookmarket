# BookMarketCore

## Coders manifest
- Code development and comments use English language
- Commit messages should use the [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) standard
- Commits should be done to feature branches `ft/` and only then, through a PR are merged to the `master` branch
- Configure and execute linters on IDE before commiting changes
- PRs are reviewed by an opposite squad

## Class Diagram
This class diagram represents the overall changes made by consequence of the implementation of the Recommender system.
This diagram's purpose is not to be 100% complete, but to map only the required interfaces to be changed.

```mermaid
classDiagram
    namespace util {
        class TPCW_Util
    }

    namespace domain {
        class Book {
            -int id
            ...
        }

        class Customer {
            -int id
            ...
        }

        class Evaluation {
            -int id
            -Customer customer
            -Book book
            -double rating
            +Evaluation(Customer c, Book b, double rating)
            +setRating(double) void
            +getRating() double
            +getCustomer() Customer
            +getBook() Book
        }
    }

    namespace recommendation {
        class BaseMahoutRecommender {
            <<interface>>
            +refresh(DataModel model) void
            +recommend(int customerId, int count) List~Book~
        }

        class UserBasedRecommender {
            -DataModel model
            -UserSimilarity similarity
            -UserNeighborhood neighborhood
            UserBasedRecommender(DataModel model)
        }

        class ItemBasedMahoutRecommender {
            -DataModel model
            -ItemSimilarity similarity
            ItemBasedMahoutRecommender(DataModel model)
        }

        class RecommendationEngine  {
            -BaseMahoutRecommender userBasedRecommender
            -BaseMahoutRecommender itemBasedMahoutRecommender
            +refreshModel(List~Evaluation~) void
            +recommendByItens(int customerId) List~int~
            +recommendByUsers(int customerId) List~int~
        }
    }

    namespace service {
        class Bookstore {
            -List~Evaluation~ evaluationById
            -RecommendationEngine recommendationEngine
            ...
            +createEvaluation(int costumerId, int bookId, double rating) void
            +updateEvaluation(int id, double rating) void
            +populateEvaluation(Random random) void
            +getRecommendationByItens(int costumerId) List~Book~
            +getRecommendationByUsers(int costumerId) List~Book~
            +getBestSellers(SUBJECT subject) List~Book~
        }

        class Bookmarket {
            ...
            +populate(long, long, int, int, int, int, int) boolean
            +getRecommendationByItens(int costumerId) List~Book~
            +getRecommendationByUsers(int costumerId) List~Book~
            +getPriceBookRecommendationByUsers(int costumerId) Map~Book, Double~
            +getBestSellers(SUBJECT subject) Map~Book, Set~Stock~~
        }

        class StateMachine {
            -List~Bookstore~ state
            +execute(Action) Object
        }

        class Action~Object~ {
            <<interface>>
            ...
            +executeOn(Object) Object
        }

        class BookstoreAction {
            <<abstract>>
            ...
            +executeOnBookstore(Stream~Bookstore~) Object
        }

        class CreateEvaluationAction {
            ...
            CreateEvaluationAction(int costumerId, int bookId, double rating)
            +executeOnBookstore(Stream~Bookstore~) Object
        }

        class UpdateEvaluationAction {
            ...
            UpdateEvaluationAction(int id, double rating)
            +executeOnBookstore(Stream~Bookstore~) Object
        }
    }

    Bookstore --> RecommendationEngine
    RecommendationEngine --> BaseMahoutRecommender
    UserBasedRecommender ..|> BaseMahoutRecommender
    ItemBasedMahoutRecommender ..|> BaseMahoutRecommender

%% Relacionamentos do Dominio
    Evaluation --> Book
    Evaluation --> Customer

%% Relacionamentos de Serviço
    Bookmarket --> StateMachine
    StateMachine --> Bookstore

    Bookstore *-- Evaluation

    Bookstore ..> TPCW_Util

%% Command Pattern Hierarchy
    BookstoreAction ..|> Action
    CreateEvaluationAction --|> BookstoreAction
    UpdateEvaluationAction --|> BookstoreAction

    Bookmarket ..> BookstoreAction
```

