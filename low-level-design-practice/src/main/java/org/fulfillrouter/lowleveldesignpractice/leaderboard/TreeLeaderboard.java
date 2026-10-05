package org.fulfillrouter.lowleveldesignpractice.leaderboard;

import java.util.*;

public class TreeLeaderboard {

    private final Map<String, Integer> playerScores =
            new HashMap<>();

    private final TreeMap<Integer, Set<String>> scores =
            new TreeMap<>(Comparator.reverseOrder());

    public void add(String id, int score) {

        // If the user already exists, remove old data first
        if (playerScores.containsKey(id)) {
            remove(id);
        }

        // user -> score
        playerScores.put(id, score);

        // score -> users
        scores.computeIfAbsent(
                score,
                key -> new LinkedHashSet<>()
        ).add(id);
    }

    public void update(String id, int newScore) {

        if (!playerScores.containsKey(id)) {
            add(id, newScore);
            return;
        }

        Integer oldScore = playerScores.get(id);

        // Remove from old score bucket
        Set<String> oldPlayers = scores.get(oldScore);

        oldPlayers.remove(id);

        // Remove the score bucket if no players remain
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

    public Integer getScore(String id) {
        return playerScores.get(id);
    }

    public int getRank(String id) {

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

    public List<String> getTop(int n) {

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

    public void remove(String id) {

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

    public static void main(String[] args) {

        TreeLeaderboard leaderboard =
                new TreeLeaderboard();

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
    }
}