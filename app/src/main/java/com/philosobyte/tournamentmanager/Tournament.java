package com.philosobyte.tournamentmanager;

import java.util.ArrayList;
import java.util.List;

public class Tournament {
    ArrayList<Round> rounds;

    public void createRound(String name) {
        rounds.add(new Round(name));
    }
    public void createRound(String name, Round round) {
        rounds.add(new Round(name, round.getWinners()));
    }

    public List<Round> getRounds() {
        return new ArrayList<>(rounds);
    }
}
