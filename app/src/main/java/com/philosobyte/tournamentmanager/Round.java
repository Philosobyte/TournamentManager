package com.philosobyte.tournamentmanager;

import android.os.Bundle;
import android.os.Parcelable;

import java.util.*;
import java.util.logging.Logger;

public class Round {
    static Logger logger = Logger.getGlobal();
    private String name;
    private ArrayList<String> players;
    private ArrayList<String> playersNotInMatch;
    private ArrayList<String> winners;
    private HashMap<String, Match> matches;
    private boolean finished;
    private Random random = new Random();

    public Round(String name) {
        this(name, new ArrayList<>());
    }

    public Round(String name, List<String> players) {
        this.name = name;
        this.players = new ArrayList<>(players);
        this.playersNotInMatch = new ArrayList<>(players);
        winners = new ArrayList<>();
        matches = new LinkedHashMap<>();
    }

    public Round(Bundle bundle) {
        name = bundle.getString("name");
        players = bundle.getStringArrayList("players");
        playersNotInMatch = bundle.getStringArrayList("playersNotInMatch");
        winners = bundle.getStringArrayList("winners");
        finished = bundle.getBoolean("finished");
        ArrayList<Parcelable> matchBundles = bundle.getParcelableArrayList("matchBundles");
        matches = new LinkedHashMap<>();
        for (Parcelable p : matchBundles) {
            Match m = new Match(this, (Bundle)p);
            matches.put(m.getName(), m);
        }
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putStringArrayList("players", players);
        bundle.putStringArrayList("playersNotInMatch", playersNotInMatch);
        bundle.putStringArrayList("winners", winners);
        bundle.putBoolean("finished", finished);
        ArrayList<Parcelable> matchBundles = new ArrayList<>();
        matches.forEach((name, match) -> matchBundles.add(match.toBundle()));
        bundle.putParcelableArrayList("matchBundles", matchBundles);
        return bundle;
    }

    public String getName() {
        return name;
    }

    public List<String> getPlayers() {
        return new ArrayList<>(players);
    }

    public List<String> getPlayersNotInMatch() {
        return new ArrayList<>(playersNotInMatch);
    }

    public List<String> getWinners() {
        List<String> winners = new ArrayList<>();
        matches.forEach((name, match) -> winners.addAll(match.getWinners()));
        return winners;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished() {
        finished = true;
    }

    public boolean contains(String player) {
        return players.contains(player);
    }

    public void addMatch(String name) {
        addMatch(name, new ArrayList<>());
    }

    public void addMatchWithRandomizedPlayers(String name, int numPlayers) {
        if (numPlayers > playersNotInMatch.size()) {
            numPlayers = playersNotInMatch.size();
        }

        List<String> matchPlayers = new ArrayList<>(numPlayers);
        while (matchPlayers.size() < numPlayers) {
            String player = playersNotInMatch.get(
                    random.nextInt(playersNotInMatch.size()));
            if (!matchPlayers.contains(player)) {
                matchPlayers.add(player);
                playersNotInMatch.remove(player);
            }
        }
        addMatch(name, matchPlayers);
    }

    public void addMatch(String name, List<String> players) {
        Match match = new Match(this, name, players);
        matches.put(match.getName(), match);
        playersNotInMatch.removeAll(players);
    }

    public void removeMatch(String name) {
        Match match = matches.get(name);
        matches.remove(name);
        playersNotInMatch.addAll(match.getPlayers());
        winners.removeAll(match.getWinners());
    }

    public void addPlayer(String player) {
        if (!contains(player)) {
            players.add(player);
            playersNotInMatch.add(player);
        }
    }

    public Match getMatch(String name) {
        return matches.get(name);
    }

    public void removePlayer(String player) {
        if (players.contains(player)) {
            players.remove(player);
        }
        if (playersNotInMatch.contains(player)) {
            players.remove(player);
        }
        matches.forEach((name, match) -> {
            if (match.contains(player)) match.removePlayer(player);
        });
    }

    public void setWinner(String player) {
        if (!players.contains(player)) {
            throw new RuntimeException("Attempted to set player " + player
                    + " as a winner in " + this.name + "but this player is not "
                    + " in " + this.name);
        } else if (!winners.contains(player)){
            winners.add(player);
        }
    }

    public void removeWinner(String player) {
        if (!winners.contains(player)) {
            throw new RuntimeException("Attempted to remove player " + player
                    + " as a winner in " + this.name + "but this player is not "
                    + " a winner in " + this.name);
        } else {
            winners.remove(player);
        }
    }

    public Map<String, Match> getMatches() {
        return new LinkedHashMap<>(matches);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Round name: ");
        sb.append(name).append("\n");
        sb.append("Players: \n");
        players.forEach(p -> sb.append("\t ").append(p).append("\n"));
        sb.append("Winners: \n");
        winners.forEach(p -> sb.append("\t ").append(p).append("\n"));
        sb.append("Matches: \n");
        matches.forEach((name, match) -> sb.append(match));
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Round)) {
            return false;
        }
        Round other = (Round)o;
        return name.equals(other.name)
                && players.equals(other.players)
                && playersNotInMatch.equals(other.playersNotInMatch)
                && winners.equals(other.winners)
                && matches.equals(other.matches)
                && finished == other.finished;
    }
}
