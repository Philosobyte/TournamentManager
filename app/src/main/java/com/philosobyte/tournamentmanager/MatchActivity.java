package com.philosobyte.tournamentmanager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ListView;

import com.philosobyte.tournamentmanager.model.Match;
import com.philosobyte.tournamentmanager.model.Round;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Ray on 1/19/2018.
 */

public class MatchActivity extends AppCompatActivity {

    Map<String, Round> rounds;
    Round currentRound;
    Match currentMatch;
    Button btnSetWinner;
    CheckBox chkFilterWinners;
    boolean filterWinners;
    SelectableAdapter saPlayers;
    SelectableAdapter saPlayersInMatch;
    SelectableAdapter saPlayersNotInMatch;
    ListView lvPlayers;
    ListView lvPlayersInMatch;
    ListView lvPlayersNotInMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Matches onCreate", "entered");

        //Unbundle rounds and match
        rounds = new LinkedHashMap<>();
        Bundle extras = getIntent().getExtras();
        String roundName = extras.getString("currentRound");
        String matchName = extras.getString("currentMatch");
        ArrayList<Parcelable> roundBundles = extras.getParcelableArrayList("roundBundles");
        roundBundles.forEach(bundle -> {
            Round round = new Round((Bundle) bundle);
            rounds.put(round.getName(), round);
        });
        currentRound = rounds.get(roundName);
        currentMatch = currentRound.getMatch(matchName);

        setContentView(R.layout.activity_match);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(matchName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Find major UI elements
        btnSetWinner = findViewById(R.id.btn_set_winner);
        chkFilterWinners = findViewById(R.id.chk_filter_winners);
        lvPlayers = findViewById(R.id.lv_players);
        lvPlayersInMatch = findViewById(R.id.lv_players_in_match);
        lvPlayersNotInMatch = findViewById(R.id.lv_players_not_in_match);

        //Initialize adapters for ListViews
        saPlayers = new FilterWinnersAdapter(this, lvPlayers, R.layout.item_player, R.id.tv_player_name, R.id.btn_remove);
        saPlayersInMatch = new SelectableAdapter(this, lvPlayersInMatch, R.layout.item_player_in_match, R.id.tv_player_name, R.id.btn_remove_player);
        saPlayersNotInMatch = new SelectableAdapter(this, lvPlayersNotInMatch, R.layout.item_player_not_in_round, R.id.tv_player_name, R.id.btn_add_player);

        lvPlayers.setAdapter(saPlayers);
        lvPlayersInMatch.setAdapter(saPlayersInMatch);
        lvPlayersNotInMatch.setAdapter(saPlayersNotInMatch);

        //Add any saved round data to listviews
        if (saPlayers.getCount() == 0) {
            saPlayers.addAll(currentMatch.getPlayers());
            saPlayersInMatch.addAll(currentMatch.getPlayers());
            saPlayersNotInMatch.addAll(currentRound.getPlayersNotInMatch());
        }

        //Set navigation listeners
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addOnTabSelectedListener(new TabSwitcher());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Bundle extras = new Bundle();
                ArrayList<Parcelable> roundBundles = new ArrayList<>();
                rounds.forEach((name, round) -> {
                    roundBundles.add(round.toBundle());
                });
                extras.putParcelableArrayList("roundBundles", roundBundles);
                extras.putString("currentRound", currentRound.getName());
                NavUtils.navigateUpTo(this, NavUtils.getParentActivityIntent(this).putExtras(extras));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void addPlayerToMatch(View view) {
        String playerName = saPlayersNotInMatch.getLastSelected();
        saPlayersInMatch.add(playerName);
        saPlayers.add(playerName);
        saPlayers.getFilter().filter("" + filterWinners);
        saPlayersNotInMatch.remove(playerName);
        currentMatch.addPlayer(playerName);
    }

    public void removePlayerFromMatch(View view) {
        String playerName = saPlayersInMatch.getLastSelected();
        saPlayersNotInMatch.add(playerName);
        saPlayersInMatch.remove(playerName);
        saPlayers.remove(playerName);
        saPlayers.getFilter().filter("" + filterWinners);
        currentMatch.removePlayer(playerName);
    }

    public void removePlayer(View view) {
        removePlayerFromMatch(view);
    }

    public void setWinner(View view) {
        String lastSelected = saPlayers.getLastSelected();
        if (currentMatch.isWinner(lastSelected)) {
            currentMatch.removeWinner(lastSelected);
            btnSetWinner.setText("Set Winner");
        } else {
            currentMatch.setWinner(lastSelected);
            btnSetWinner.setText("Unset Winner");
        }
        saPlayers.getFilter().filter("" + filterWinners);
    }

    public void filterWinners(View view) {
        filterWinners = ((CheckBox)view).isChecked();
        saPlayers.getFilter().filter("" + filterWinners);
    }

    class TabSwitcher implements TabLayout.OnTabSelectedListener {

        ConstraintLayout clPlayers = findViewById(R.id.cl_players);
        ConstraintLayout clAddPlayers = findViewById(R.id.cl_add_players);

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if ("Players".equals(tab.getText())) {
                clPlayers.setVisibility(View.VISIBLE);
            } else if ("Add Players".equals(tab.getText())) {
                clAddPlayers.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if ("Players".equals(tab.getText())) {
                clPlayers.setVisibility(View.GONE);
            } else if ("Add Players".equals(tab.getText())) {
                clAddPlayers.setVisibility(View.GONE);
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            //STUB
        }
    }

    class FilterWinnersAdapter extends SelectableAdapter {

        public FilterWinnersAdapter(Activity activity, ListView lv, int resId, int tvId, int... btnIds) {
            super(activity, lv, resId, tvId, btnIds);
            filter = new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    if ("false".equals(constraint)) {
                        filteredItems = new ArrayList<>(items);
                    } else {
                        filteredItems = items.stream().filter(currentMatch::isWinner).collect(Collectors.toList());
                    }
                    results.count = filteredItems.size();
                    results.values = filteredItems;
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    notifyDataSetChanged();
                }
            };
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            super.onItemClick(parent, view, position, id);
            if (currentMatch.isWinner(getLastSelected())) {
                btnSetWinner.setText("Unset Winner");
            } else {
                btnSetWinner.setText("Set Winner");
            }
        }
    }
}
