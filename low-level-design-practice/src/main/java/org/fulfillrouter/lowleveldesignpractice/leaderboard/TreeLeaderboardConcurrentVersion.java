package org.fulfillrouter.lowleveldesignpractice.leaderboard;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TreeLeaderboardConcurrentVersion {

    private final Map<String, Integer> playerScores =
            new HashMap<>();

    private final TreeMap<Integer, Set<String>> scores =
            new TreeMap<>(Comparator.reverseOrder());


    /*
     * One lock protects the whole shared state.
     *
     * playerScores and scores must always stay consistent.
     *
     * update():
     *
     *     remove old score
     *          ↓
     *     update playerScores
     *          ↓
     *     add new score
     *
     * These operations must happen as one atomic operation.
     */
    private final Object lock = new Object();


    public void add(String id, int score) {

        synchronized (lock) {

            if (playerScores.containsKey(id)) {
                removeInternal(id);
            }

            addInternal(id, score);
        }
    }


    public void update(String id, int newScore) {

        synchronized (lock) {

            if (!playerScores.containsKey(id)) {
                addInternal(id, newScore);
                return;
            }

            Integer oldScore = playerScores.get(id);

            // Remove from old score bucket
            Set<String> oldPlayers = scores.get(oldScore);

            oldPlayers.remove(id);

            // Remove empty bucket
            if (oldPlayers.isEmpty()) {
                scores.remove(oldScore);
            }

            // Add to new score bucket
            playerScores.put(id, newScore);

            scores.computeIfAbsent(
                    newScore,
                    key -> new LinkedHashSet<>()
            ).add(id);
        }
    }


    public Integer getScore(String id) {

        synchronized (lock) {
            return playerScores.get(id);
        }
    }


    public int getRank(String id) {

        synchronized (lock) {

            Integer targetScore = playerScores.get(id);

            if (targetScore == null) {
                return -1;
            }

            int rank = 1;

            for (Map.Entry<Integer, Set<String>> entry
                    : scores.entrySet()) {

                int score = entry.getKey();

                if (score == targetScore) {
                    return rank;
                }

                rank += entry.getValue().size();
            }

            return -1;
        }
    }


    public List<String> getTop(int n) {

        synchronized (lock) {

            List<String> result = new ArrayList<>();

            for (Set<String> players : scores.values()) {

                for (String player : players) {

                    if (result.size() >= n) {
                        return result;
                    }

                    result.add(player);
                }
            }

            return result;
        }
    }


    public void remove(String id) {

        synchronized (lock) {
            removeInternal(id);
        }
    }


    // =========================================================
    // Internal methods
    // Caller must already hold lock.
    // =========================================================

    private void addInternal(String id, int score) {

        assert Thread.holdsLock(lock);

        playerScores.put(id, score);

        scores.computeIfAbsent(
                score,
                key -> new LinkedHashSet<>()
        ).add(id);
    }


    private void removeInternal(String id) {

        assert Thread.holdsLock(lock);

        Integer score = playerScores.remove(id);

        if (score == null) {
            return;
        }

        Set<String> players = scores.get(score);

        players.remove(id);

        if (players.isEmpty()) {
            scores.remove(score);
        }
    }


    // =========================================================
    // Verification
    // =========================================================

    /*
     * Check the invariant:
     *
     * playerScores
     *      ↕
     * scores
     *
     * Every player must exist in both structures
     * with the same score.
     */
    private boolean isConsistent() {

        synchronized (lock) {

            // playerScores -> scores
            for (Map.Entry<String, Integer> entry
                    : playerScores.entrySet()) {

                String player = entry.getKey();
                Integer score = entry.getValue();

                Set<String> players = scores.get(score);

                if (players == null || !players.contains(player)) {
                    return false;
                }
            }

            // scores -> playerScores
            for (Map.Entry<Integer, Set<String>> entry
                    : scores.entrySet()) {

                Integer score = entry.getKey();

                for (String player : entry.getValue()) {

                    Integer actualScore =
                            playerScores.get(player);

                    if (!Objects.equals(actualScore, score)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }


    private int playerCount() {

        synchronized (lock) {
            return playerScores.size();
        }
    }


    // =========================================================
    // Single-thread test
    // =========================================================

    private static void basicTest() {

        TreeLeaderboardConcurrentVersion leaderboard =
                new TreeLeaderboardConcurrentVersion();

        leaderboard.add("Alice", 100);
        leaderboard.add("Bob", 80);
        leaderboard.add("Charlie", 90);
        leaderboard.add("David", 100);

        System.out.println("Top 3:");
        System.out.println(leaderboard.getTop(3));

        System.out.println("Alice score:");
        System.out.println(leaderboard.getScore("Alice"));

        System.out.println("Alice rank:");
        System.out.println(leaderboard.getRank("Alice"));

        System.out.println("Update Bob:");
        leaderboard.update("Bob", 110);

        System.out.println(leaderboard.getTop(4));

        System.out.println("Remove David:");
        leaderboard.remove("David");

        System.out.println(leaderboard.getTop(4));

        System.out.println(
                "Consistent: "
                        + leaderboard.isConsistent()
        );
    }


    // =========================================================
    // Multi-thread test
    // =========================================================

    private static void concurrentTest()
            throws InterruptedException {

        TreeLeaderboardConcurrentVersion leaderboard =
                new TreeLeaderboardConcurrentVersion();

        int threadCount = 32;
        int operationsPerThread = 50_000;

        int expectedPlayers =
                threadCount * operationsPerThread;


        ExecutorService pool =
                Executors.newFixedThreadPool(threadCount);


        /*
         * All threads wait here.
         *
         * countDown() releases them together.
         */
        CountDownLatch start =
                new CountDownLatch(1);


        /*
         * Main thread waits here until
         * all worker threads finish.
         */
        CountDownLatch done =
                new CountDownLatch(threadCount);


        /*
         * Each thread adds a completely different
         * range of player IDs.
         *
         * Therefore:
         *
         * expectedPlayers
         *     =
         * threadCount × operationsPerThread
         */
        for (int threadId = 0;
             threadId < threadCount;
             threadId++) {

            final int idPrefix = threadId;

            pool.submit(() -> {

                try {

                    // Wait for all threads to be ready.
                    start.await();


                    for (int i = 0;
                         i < operationsPerThread;
                         i++) {

                        String id =
                                "Player-"
                                        + idPrefix
                                        + "-"
                                        + i;

                        /*
                         * Deterministic write.
                         *
                         * No Random.
                         */
                        leaderboard.add(id, i);
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {

                    done.countDown();
                }
            });
        }


        long startTime =
                System.currentTimeMillis();


        /*
         * Start all worker threads.
         */
        start.countDown();


        /*
         * Wait for all workers to finish.
         */
        done.await();


        pool.shutdown();


        long elapsed =
                System.currentTimeMillis()
                        - startTime;


        // =====================================================
        // Verification
        // =====================================================

        int actualPlayers =
                leaderboard.playerCount();

        boolean countMatched =
                actualPlayers == expectedPlayers;

        boolean consistent =
                leaderboard.isConsistent();


        System.out.println();
        System.out.println("==============================");
        System.out.println("Concurrent Test");
        System.out.println("==============================");

        System.out.println(
                "Threads: "
                        + threadCount
        );

        System.out.println(
                "Operations per thread: "
                        + operationsPerThread
        );

        System.out.println(
                "Expected players: "
                        + expectedPlayers
        );

        System.out.println(
                "Actual players: "
                        + actualPlayers
        );

        System.out.println(
                "Count matched: "
                        + countMatched
        );

        System.out.println(
                "State consistent: "
                        + consistent
        );

        System.out.println(
                "Elapsed: "
                        + elapsed
                        + " ms"
        );


        /*
         * The test passes only if:
         *
         * 1. No worker failed
         * 2. Every expected player exists
         * 3. Both data structures are consistent
         */
        if (!countMatched || !consistent) {

            throw new IllegalStateException(
                    "Concurrent test failed"
            );
        }

        System.out.println(
                "Concurrent test PASSED"
        );
    }


    // =========================================================
    // Main
    // =========================================================

    public static void main(String[] args)
            throws InterruptedException {

        basicTest();

        concurrentTest();
    }
}
