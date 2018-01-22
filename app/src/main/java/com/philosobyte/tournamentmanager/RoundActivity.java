package com.philosobyte.tournamentmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.philosobyte.tournamentmanager.model.Round;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ray on 1/10/2018.
 */

public class RoundActivity extends AppCompatActivity {
    Map<String, Round> rounds;
    Round currentRound;
    EditText etMatchName;
    EditText etPlayerName;
    SelectableAdapter saPlayers;
    SelectableAdapter saPlayersInMatch;
    SelectableAdapter saPlayersNotInMatch;
    SelectableAdapter saMatches;
    ListView lvMatches;
    ListView lvPlayers;
    ListView lvPlayersInMatch;
    ListView lvPlayersNotInMatch;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Rounds onCreate", "entered");

        //Unbundle rounds
        rounds = new LinkedHashMap<>();
        Bundle extras = getIntent().getExtras();
        String roundName = extras.getString("currentRound");
        ArrayList<Parcelable> roundBundles = extras.getParcelableArrayList("roundBundles");
        roundBundles.forEach(bundle -> {
            Round round = new Round((Bundle)bundle);
            rounds.put(round.getName(), round);
        });
        currentRound = rounds.get(roundName);

        setContentView(R.layout.activity_round);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(roundName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Find major UI elements
        lvPlayers = findViewById(R.id.lv_players);
        lvMatches = findViewById(R.id.lv_matches);
        lvPlayersInMatch = findViewById(R.id.lv_players_in_match);
        lvPlayersNotInMatch = findViewById(R.id.lv_players_not_in_match);
        etPlayerName = findViewById(R.id.et_player_name);
        etMatchName = findViewById(R.id.et_match_name);
        btnAdd = findViewById(R.id.btn_add);

        //Initialize adapters for ListViews
        saPlayers = new SelectableAdapter(this, lvPlayers, R.layout.item_player, R.id.tv_player_name, R.id.btn_remove);
        saPlayersInMatch = new SelectableAdapter(this, lvPlayersInMatch,R.layout.item_player_in_match, R.id.tv_player_name, R.id.btn_remove_player);
        saPlayersNotInMatch = new SelectableAdapter(this, lvPlayersNotInMatch, R.layout.item_player_not_in_round, R.id.tv_player_name, R.id.btn_add_player);
        saMatches = new SelectableAdapter(this, lvMatches, R.layout.item_match, R.id.tv_match_name, R.id.btn_view, R.id.btn_remove);

        lvPlayers.setAdapter(saPlayers);
        lvPlayersInMatch.setAdapter(saPlayersInMatch);
        lvPlayersNotInMatch.setAdapter(saPlayersNotInMatch);
        lvMatches.setAdapter(saMatches);

        //Add any saved round data to listviews
        if (saPlayers.getCount() == 0) {
            saPlayers.addAll(currentRound.getPlayers());
            saPlayersNotInMatch.addAll(currentRound.getPlayersNotInMatch());
            saMatches.addAll(new ArrayList<>(currentRound.getMatches().keySet()));
        }

        //Set navigation listeners
        BottomNavigationView bottomNav = findViewById(R.id.bnv_nav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavListener(btnAdd, etPlayerName, saPlayers));
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addOnTabSelectedListener(new TabSwitcher());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("Rounds onNewIntent", "entered");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Log.d("Rounds onNewIntent", "extras not null");
            saPlayers.removeAll();
            saPlayersNotInMatch.removeAll();
            saMatches.removeAll();
            ArrayList<Parcelable> roundBundles = extras.getParcelableArrayList("roundBundles");
            roundBundles.forEach(bundle -> {
                Round round = new Round((Bundle) bundle);
                rounds.put(round.getName(), round);
            });
            String currentRoundName = extras.getString("currentRound");
            currentRound = rounds.get(currentRoundName);
            saPlayers.addAll(currentRound.getPlayers());
            saPlayersNotInMatch.addAll(currentRound.getPlayersNotInMatch());
            saMatches.addAll(new ArrayList<>(currentRound.getMatches().keySet()));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d("Rounds onSaveInstanceState", "entered");
        savedInstanceState.putStringArrayList("players", new ArrayList<>(saPlayers.getItems()));
        savedInstanceState.putStringArrayList("playersNotInMatch", new ArrayList<>(saPlayersNotInMatch.getItems()));
        savedInstanceState.putStringArrayList("playersInMatch", new ArrayList<>(saPlayersInMatch.getItems()));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("Rounds onRestore", "entered");
        if (saPlayers.getCount() == 0) {
            List<String> players = savedInstanceState.getStringArrayList("players");
            List<String> playersNotInMatch = savedInstanceState.getStringArrayList("playersNotInMatch");
            List<String> playersInMatch = savedInstanceState.getStringArrayList("playersInMatch");
            players.forEach(saPlayers::add);
            playersNotInMatch.forEach(saPlayersNotInMatch::add);
            playersInMatch.forEach(saPlayersInMatch::add);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Bundle extras = new Bundle();
                ArrayList<Parcelable> roundBundles = new ArrayList<>();
                rounds.forEach((name, round) -> {
                    roundBundles.add(round.toBundle());
                    Log.d("Rounds onOptionsItemSelected", "bundling round: " + name);
                });
                extras.putParcelableArrayList("roundBundles", roundBundles);
                NavUtils.navigateUpTo(this, NavUtils.getParentActivityIntent(this).putExtras(extras));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addPlayer(View view) {
        String playerName = etPlayerName.getText().toString();
        saPlayers.add(playerName);
        etPlayerName.setText("");
        lvPlayers.setSelection(lvPlayers.getCount() - 1);
        currentRound.addPlayer(playerName);
        saPlayersNotInMatch.add(playerName);
    }

    public void removePlayer(View view) {
        String playerName = ((TextView) ((ViewGroup) view.getParent()).findViewById(R.id.tv_player_name)).getText().toString();
        saPlayers.remove(playerName);
        currentRound.removePlayer(playerName);
        if (saPlayersInMatch.contains(playerName)) {
            saPlayersInMatch.remove(playerName);
        }
        if (saPlayersNotInMatch.contains(playerName)) {
            saPlayersNotInMatch.remove(playerName);
        }
    }

    public void addPlayerToMatch(View view) {
        String playerName = saPlayersNotInMatch.getLastSelected();
        saPlayersInMatch.add(playerName);
        saPlayersNotInMatch.remove(playerName);
    }

    public void removePlayerFromMatch(View view) {
        String playerName = ((TextView) ((ViewGroup) view.getParent()).findViewById(R.id.tv_player_name)).getText().toString();
        saPlayersInMatch.remove(playerName);
        saPlayersNotInMatch.add(playerName);
    }

    public void addMatch(View view) {
        String matchName = etMatchName.getText().toString();
        etMatchName.setText("");
        List<String> playersInMatch = saPlayersInMatch.getItems();
        saPlayersInMatch.removeAll();
        currentRound.addMatch(matchName, playersInMatch);
        saMatches.add(matchName);
    }

    public void viewMatch(View view) {
        String matchName = ((TextView) ((ViewGroup) view.getParent()).findViewById(R.id.tv_match_name)).getText().toString();
        Intent intent = new Intent(this, MatchActivity.class);
        Bundle extras = new Bundle();
        ArrayList<Parcelable> roundBundles = new ArrayList<>();
        rounds.forEach((name, round) -> {
            roundBundles.add(round.toBundle());
            Log.d("Rounds onOptionsItemSelected", "bundling round: " + name);
        });
        extras.putParcelableArrayList("roundBundles", roundBundles);
        extras.putString("currentRound", currentRound.getName());
        extras.putString("currentMatch", matchName);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void removeMatch(View view) {
        String matchName = ((TextView) ((ViewGroup) view.getParent()).findViewById(R.id.tv_match_name)).getText().toString();
        saMatches.remove(matchName);
        saPlayersNotInMatch.addAll(currentRound.getMatch(matchName).getPlayers());
        currentRound.removeMatch(matchName);
    }

    class MatchAdapter extends SelectableAdapter {

        int lvId;
        public MatchAdapter(Activity activity, ListView lv, int resId, int tvId, int lvId, int... btnIds) {
            super(activity, lv, resId, tvId, btnIds);
            this.lvId = lvId;
        }

        @Override
        public void add(String matchName) {
            items.add(matchName);
            filteredItems.add(matchName);

        }
    }

    private class TabSwitcher implements TabLayout.OnTabSelectedListener {

        ConstraintLayout clPlayers = findViewById(R.id.cl_players);
        ConstraintLayout clMatches = findViewById(R.id.cl_matches);
        ConstraintLayout clAddMatches = findViewById(R.id.cl_add_matches);

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if ("Players".equals(tab.getText())) {
                clPlayers.setVisibility(View.VISIBLE);
            } else if ("Matches".equals(tab.getText())) {
                clMatches.setVisibility(View.VISIBLE);
            } else if ("Add Matches".equals(tab.getText())) {
                clAddMatches.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if ("Players".equals(tab.getText())) {
                clPlayers.setVisibility(View.GONE);
            } else if ("Matches".equals(tab.getText())) {
                clMatches.setVisibility(View.GONE);
            } else if ("Add Matches".equals(tab.getText())) {
                clAddMatches.setVisibility(View.GONE);
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            //STUB
        }
    }
}