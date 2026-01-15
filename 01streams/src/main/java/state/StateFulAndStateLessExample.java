package state;

public class StateFulAndStateLessExample {

    public static void main(String[] args) {

        // STATELESS OPERATIONS in Streams:
        // - Do not depend on any state from previously processed elements
        // - Each element is processed independently
        // - Examples: filter(), map(), flatMap(), peek()
        // - Order of processing doesn't matter
        // - Better performance in parallel streams
        // - No need to buffer elements

        // STATEFUL OPERATIONS in Streams:
        // - Depend on state from previously processed elements
        // - May need to see/buffer multiple elements before producing output
        // - Examples: distinct(), sorted(), limit(), skip()
        // - Order of processing matters
        // - Less efficient in parallel streams due to synchronization overhead
        // - sorted() needs to see ALL elements before producing any output
        // - distinct() needs to remember all previously seen elements

    }
}
