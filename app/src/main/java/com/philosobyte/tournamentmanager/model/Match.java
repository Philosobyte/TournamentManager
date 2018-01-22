package com.philosobyte.tournamentmanager.model;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Match {
    static Logger logger = Logger.getGlobal();
    private String name;
    private ArrayList<String> players;
    private ArrayList<String> winners;
    private boolean finished;
    private Round parent;

    public Match(Round parent, String name, List<String> players) {
        if (players == null) {
            throw new NullPointerException(
                    "List of players passed into a new Match was null");
        }
        this.name = name;
        this.players = new ArrayList<>(players);
        this.winners = new ArrayList<>();
        this.parent = parent;
    }

    public Match(Round parent, Bundle bundle) {
        this.parent = parent;
        name = bundle.getString("name");
        players = bundle.getStringArrayList("players");
        winners = bundle.getStringArrayList("winners");
        finished = bundle.getBoolean("finished");
    }

    public Match(Round parent, String name) {
        this(parent, name, new ArrayList<>());
    }

    public void setFinished() {
        finished = true;
    }

    public boolean isFinished() {
        return finished;
    }

    public void addPlayer(String player) {
        if (player.equals("")) {
            logger.warning("Attempted to add an empty string as a player"
                           + " to match " + this.name);
        } else if (players.contains(player)) {
            logger.warning("Attempted to add already existing player " + player
                           + " to match " + this.name);
        } else {
            players.add(player);
        }
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putStringArrayList("players", players);
        bundle.putStringArrayList("winners", winners);
        bundle.putBoolean("finished", finished);
        return bundle;
    }

    public List<String> getPlayers() {
        return new ArrayList<>(players);
    }

    public void removePlayer(String player) {
        if (!players.contains(player)) {
            throw new RuntimeException("Attempted to remove player " + player
                                       + " who is not in match " + this.name);
        } else {
            players.remove(player);
        }
    }

    public boolean contains(String player) {
        return players.contains(player);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Match name: ");
        sb.append(name).append("\n");
        sb.append("Players: \n");
        players.forEach(p -> sb.append("\t").append(p).append("\n"));
        sb.append("Winners: \n");
        getWinners().forEach(w -> sb.append("\t").append(w).append("\n"));
        sb.append("\n");
        return sb.toString();
    }

    public List<String> getWinners() {
        return new ArrayList<>(winners);
    }

    public boolean isWinner(String player) {
        return winners.contains(player);
    }

    public String getName() {
        return name;
    }

    public void setWinner(String player) {
        if (!players.contains(player)) {
            throw new RuntimeException("Attempted to set player " + player
                    + " as a winner in " + this.name + "but this player is not "
                    + " in " + this.name);
        } else if (!winners.contains(player)){
            winners.add(player);
            parent.setWinner(player);
        }
    }

    public void removeWinner(String player) {
        if (!winners.contains(player)) {
            throw new RuntimeException("Attempted to remove player " + player
                    + " as a winner in " + this.name + "but this player is not "
                    + " a winner in " + this.name);
        } else {
            winners.remove(player);
            parent.removeWinner(player);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Match)) {
            return false;
        }
        Match other = (Match)o;
        return name.equals(other.getName())
               && players.equals(other.players)
               && winners.equals(other.winners)
               && finished == other.finished
               && parent.equals(other.parent);
    }
}