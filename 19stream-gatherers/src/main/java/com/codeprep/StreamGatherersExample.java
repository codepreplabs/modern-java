package com.codeprep;

import com.codeprep.data.Movie;
import com.codeprep.data.MovieGenre;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Gatherers;

/**
 * Stream Gatherers - Java 22+ Feature (Preview in Java 22, Standard in Java 23+)
 *
 * Stream Gatherers are intermediate operations that allow custom transformations of stream elements.
 * They provide a way to implement complex, stateful stream operations that were previously difficult
 * or impossible with standard stream operations.
 *
 * Key Characteristics:
 * - More flexible than collectors (work on intermediate streams, not just terminal operations)
 * - Can maintain state across elements
 * - Can emit zero, one, or multiple elements for each input element
 * - Can short-circuit the stream processing
 *
 * Common Built-in Gatherers and Use Cases:
 *
 * 1. windowFixed(int windowSize)
 *    - Groups consecutive elements into fixed-size windows
 *    - Use cases: Batch processing, moving averages, analyzing consecutive patterns
 *    - Example: Process log entries in batches of 100
 *
 * 2. windowSliding(int windowSize)
 *    - Creates overlapping windows of elements
 *    - Use cases: Moving averages, trend analysis, pattern detection
 *    - Example: Calculate 7-day moving average of stock prices
 *
 * 3. fold(Supplier<R> initial, BiFunction<R, T, R> folder)
 *    - Performs a reduction while emitting intermediate results
 *    - Use cases: Running totals, cumulative statistics, progressive calculations
 *    - Example: Running sum of transaction amounts
 *
 * 4. scan(Supplier<R> initial, BiFunction<R, T, R> scanner)
 *    - Similar to fold but emits all intermediate states
 *    - Use cases: Prefix sums, incremental computations, state tracking
 *    - Example: Track accumulated points in a game
 *
 * 5. mapConcurrent(int maxConcurrency, Function<T, R> mapper)
 *    - Applies a function concurrently with controlled parallelism
 *    - Use cases: Parallel API calls, concurrent I/O operations
 *    - Example: Fetch user details from database concurrently
 *
 * Advantages over Traditional Stream Operations:
 * - Can implement complex windowing operations without collectors
 * - Better performance for stateful operations
 * - More readable code for complex transformations
 * - Composable with other stream operations
 *
 * Custom Gatherers:
 * You can create custom gatherers by implementing the Gatherer interface, which provides
 * fine-grained control over element processing, state management, and emission.
 */
public class StreamGatherersExample {

    static void main() {

        List<Movie> movies = getMovies();

//        windowFixedDemo(movies);
//        windowSlidingDemo(movies);
//        foldDemo(movies);
        scanDemo(movies);
    }

    private static void scanDemo(List<Movie> movies){

        movies.stream()
                .gather(Gatherers.scan(
                        () -> 0,
                        (total, movie) -> total + movie.duration()
                ))
                .forEach(runningTotal -> System.out.println("Running total duration: " + runningTotal + " minutes"));

    }

    private static void foldDemo(List<Movie> movies){

        var totalDuration = movies.stream()
                .gather(Gatherers.fold(
                        () -> 0,
                        (total, movie) -> total + movie.duration()
                ))
                .findFirst()
                .orElse(0);
        System.out.println("Total duration of all movies: " + totalDuration + " minutes");

    }

    private static void windowFixedDemo(List<Movie> movies){

        movies.stream()
                .gather(Gatherers.windowFixed(3))
                .forEach(window -> {
                    System.out.println("WindowFixed: ");
                    window.forEach(movie ->
                            System.out.println(" - " + movie.title() + " (" + movie.getReleaseYear() + ")"));
                    System.out.println();
                });
    }

    private static void windowSlidingDemo(List<Movie> movies){

        movies.stream()
                .limit(5)
                .gather(Gatherers.windowSliding(2))
                .forEach(window -> {
                    System.out.println("WindowSliding: ");
                    window.forEach(movie ->
                            System.out.println(" - " + movie.title() + " (" + movie.getReleaseYear() + ")"));
                    System.out.println();
                });
    }

    private static List<Movie> getMovies() {
        return List.of(
                new Movie("The Godfather", MovieGenre.DRAMA, LocalDate.of(1972, 3, 24), 9.2, 175),
                new Movie("The Shawshank Redemption", MovieGenre.DRAMA, LocalDate.of(1994, 9, 23), 9.3, 142),
                new Movie("Pulp Fiction", MovieGenre.DRAMA, LocalDate.of(1994, 10, 14), 8.9, 154),
                new Movie("The Dark Knight", MovieGenre.ACTION, LocalDate.of(2008, 7, 18), 9.0, 152),
                new Movie("Schindler's List", MovieGenre.DRAMA, LocalDate.of(1993, 12, 15), 9.0, 195),
                new Movie("Forrest Gump", MovieGenre.DRAMA, LocalDate.of(1994, 7, 6), 8.8, 142),
                new Movie("Inception", MovieGenre.SCIENCE_FICTION, LocalDate.of(2010, 7, 16), 8.8, 148),
                new Movie("The Matrix", MovieGenre.SCIENCE_FICTION, LocalDate.of(1999, 3, 31), 8.7, 136),
                new Movie("Goodfellas", MovieGenre.DRAMA, LocalDate.of(1990, 9, 19), 8.7, 146),
                new Movie("Star Wars: A New Hope", MovieGenre.SCIENCE_FICTION, LocalDate.of(1977, 5, 25), 8.6, 121),
                new Movie("Casablanca", MovieGenre.ROMANCE, LocalDate.of(1942, 11, 26), 8.5, 102),
                new Movie("Citizen Kane", MovieGenre.DRAMA, LocalDate.of(1941, 5, 1), 8.3, 119)
        );
    }
}
