package com.philosobyte.tournamentmanager;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<String> tmntList = new ArrayList<>();
        tmntList.add("Mike");
        tmntList.add("Raph");
        tmntList.add("Don");
        tmntList.add("Leo");
        tmntList.add("Splinter");
        tmntList.add("Casey");
        tmntList.add("April");
        Round tmntRound = new Round("TMNT", tmntList);
        System.out.println(tmntRound);
        tmntRound.addMatchWithRandomizedPlayers("First", 2);
        tmntRound.addMatchWithRandomizedPlayers("Second", 2);
        System.out.println(tmntRound);
        Match match1 = tmntRound.getMatches().get(0);
        String player1 = match1.getPlayers().get(0);
        match1.setWinner(player1);
        System.out.println(tmntRound);
    }
}
