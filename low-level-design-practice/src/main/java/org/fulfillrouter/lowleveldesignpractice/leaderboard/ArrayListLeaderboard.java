package org.fulfillrouter.lowleveldesignpractice.leaderboard;

import java.util.*;

public class ArrayListLeaderboard {

    static class Player {
        private final String id;
        private int score;

        public Player(String id, int score) {
            this.id = id;
            this.score = score;
        }

        public String getId() {
            return id;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return id + " (" + score + ")";
        }
    }

    private final List<Player> players = new ArrayList<>();

    public void add(String id, int score) {
        players.add(new Player(id, score));
        sort();
    }

    public void update(String id, int newScore) {
        for (Player player : players) {
            if (player.getId().equals(id)) {
                player.setScore(newScore);
                break;
            }
        }

        sort();
    }

    public Integer getScore(String id) {
        for (Player player : players) {
            if (player.getId().equals(id)) {
                return player.getScore();
            }
        }

        return null;
    }

    public int getRank(String id) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId().equals(id)) {
                return i + 1;
            }
        }

        return -1;
    }

    public List<Player> getTop(int n) {
        int end = Math.min(n, players.size());

        return new ArrayList<>(
                players.subList(0, end)
        );
    }

    public void remove(String id) {
        players.removeIf(
                player -> player.getId().equals(id)
        );
    }

    private void sort() {
        players.sort(
                Comparator.comparingInt(Player::getScore)
                        .reversed()
        );
    }

    public static void main(String[] args) {

        ArrayListLeaderboard leaderboard =
                new ArrayListLeaderboard();

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