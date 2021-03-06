package com.philosobyte.tournamentmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.philosobyte.tournamentmanager.model.Round;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TournamentActivity extends AppCompatActivity {
    private ListView lvRounds;
    private SelectableAdapter raRounds;
    private EditText etRoundName;
    private Map<String, Round> rounds;
    private CheckBox chkWinFromSel;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Tournaments onCreate", "entered");
        rounds = new LinkedHashMap<>();

        setContentView(R.layout.activity_tournament);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Rounds");

        //Find major UI elements
        etRoundName = findViewById(R.id.et_round_name);
        lvRounds = findViewById(R.id.lv_rounds);
        chkWinFromSel = findViewById(R.id.chk_win_from_sel);
        btnAdd = findViewById(R.id.btn_add);

        raRounds = new RoundAdapter(this, lvRounds, R.layout.item_round,
                                    R.id.tv_round_name, R.id.btn_view, R.id.btn_remove);
        lvRounds.setAdapter(raRounds);

        //Set navigation listeners
        BottomNavigationView bottomNav = findViewById(R.id.bnv_nav);
        bottomNav.setOnNavigationItemSelectedListener(
                new BottomNavListener(chkWinFromSel, btnAdd, etRoundName, raRounds));
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList("items", new ArrayList<>(raRounds.getItems()));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("Tournaments onNewIntent", "entered onNewIntent");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Log.d("Tournaments onNewIntent", "extras not null");
            raRounds.removeAll();
            ArrayList<Parcelable> roundBundles = extras.getParcelableArrayList("roundBundles");
            roundBundles.forEach(bundle -> {
                Round round = new Round((Bundle) bundle);
                rounds.put(round.getName(), round);
                raRounds.add(round.getName());
            });
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("Watermark", "entered onRestore");
        if (raRounds.getCount() == 0) {
            List<String> items = savedInstanceState.getStringArrayList("items");
            items.forEach(i -> raRounds.add(i));
        }
    }

    public void addRound(View view) {
        String roundName = etRoundName.getText().toString();
        raRounds.add(roundName);
        etRoundName.setText("");
        lvRounds.setSelection(lvRounds.getCount() - 1);
        if (!chkWinFromSel.isChecked()) {
            rounds.put(roundName, new Round(roundName));
            return;
        }
        String lastSelectedName = raRounds.getLastSelected();
        Round lastSelectedRound = rounds.get(lastSelectedName);
        rounds.put(roundName, new Round(roundName, lastSelectedRound.getWinners()));
    }

    public void removeRound(View view) {
        String roundName = getAssociatedRoundName((ImageButton)view);
        raRounds.remove(roundName);
        rounds.remove(roundName);
    }

    public void viewRound(View view) {
        String roundName = getAssociatedRoundName((ImageButton)view);
        Intent intent = new Intent(this, RoundActivity.class);
        Bundle extras = new Bundle();
        ArrayList<Parcelable> roundBundles = new ArrayList<>();
        rounds.forEach((name, round) -> roundBundles.add(round.toBundle()));
        extras.putParcelableArrayList("roundBundles", roundBundles);
        extras.putString("currentRound", roundName);
        intent.putExtras(extras);
        startActivity(intent);
    }

    /*
     * Obtains the round name associated with an ImageButton in a ListView using a SelectableAdapter
     */
    private String getAssociatedRoundName(ImageButton button) {
        TextView tvRoundName = ((ViewGroup)button.getParent()).findViewById(R.id.tv_round_name);
        return tvRoundName.getText().toString();
    }

    class RoundAdapter extends SelectableAdapter {

        RoundAdapter(Activity activity, ListView lv, int resId, int tvId, int... btnIds) {
            super(activity, lv, resId, tvId, btnIds);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            super.onItemClick(parent, view, position, id);
            if (!chkWinFromSel.isEnabled())
                chkWinFromSel.setEnabled(true);
        }
    }
}
