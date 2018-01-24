# TournamentManager

TournamentManager is an Android app for tournament organizers to run a tournament in a highly flexible manner. Ever wished you could add players mid-tournament? Or set two winners per match instead of just one? This app allows just that by offering you full control over a simple model instead of shoehorning your tournament into complicated or predefined brackets.

### What TournamentManager is and is not
This app is not for viewing or displaying a tournament in a pretty graph. TournamentManager is strictly meant for running a tournament behind the scenes. The hope for this app is to help local and student organizers - who don't have custom-tailored software - to keep their tournament running smoothly.

### Tournament Model
+ Hierarchy = Tournament -> Round -> Match -> Player. This means multiple players per match, multiple matches per round, and multiple rounds per tournament.
+ Each round and match can be given custom names
+ Rounds may be created with an empty list of matches and players or with a list of players from a previous round's winners.
+ Matches can be freely created or deleted
+ Players must be created in a Round. They can then be freely added or removed from matches, or deleted from a Round.
+ Players may be set as winners in a particular Match. Once set as winners for a Match, the players are also winners for the Match's parent Round.

### Installation
The latest unsigned apk may be downloaded and installed on the [releases page](https://github.com/Philosobyte/TournamentManager/releases). I am corrently working on getting TournamentManager on the Google Play Store.

### What's next
Bugs to fix:
+ Disallow duplicate names
+ Disallow white space or an empty string as names
+ Currently the Enter key will create a new line in a text edit box. The Enter key should map to a button.

Improvements or features to make:
+ A more distinctive name and UI theme!
+ Allow the user to view all the winners for a particular Round
+ Allow the user to create a Match with randomly selected players from the remaining players in a Round

### Tutorial
Upon opening the app, you will see the "Rounds" screen:

<img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/rounds_adding.png?raw=true" height="500"/> <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/rounds_searching.png?raw=true" height="500"/>

To add a round, type a name into the textbox and press <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/btn_add.png?raw=true" height="30" />. To search for a round, click the bottom navigation button <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/nav_search.png?raw=true" height="40" />; the textbox becomes a searchbox which searches after every keypress for any round names which contain the entry.
To remove a round, select the round by tapping it, then tap its <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/btn_delete.png?raw=true" height="25" /> button. To view and edit a round's details, tap on its <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/btn_view.png?raw=true" height="25" /> button.

Let's tap on <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/btn_view.png?raw=true" height="25" /> for "Round 1". This brings us to Round 1's individual Round screen:

<img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/round_player_adding.png?raw=true" height="500" /> <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/round_match_adding.png?raw=true" height="500" /> <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/round_match_viewing.png?raw=true" height="500" />

Adding, removing, and searching players are similar to adding, removing, and searching rounds in the previous screen. To add a match to the current round, tap on the "Add Matches" tab. Here you can enter a name for the new match and decide which players will go in the match. Pressing <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/btn_add_player.png?raw=true" height="25" /> for a player in the bottom list will add the player to the match, and pressing  <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/btn_delete.png?raw=true" height="25" /> for a player in the top list will remove the player from the match. The match will not actually be created until you hit <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/btn_creatematch.png?raw=true" height="30" />. Matches may be viewed or removed in the "Matches" tab.

Let's press <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/btn_view.png?raw=true" height="25" /> for "Match 1". This brings us to Match 1's individual Match screen: 

<img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/match_player_setwinner.png?raw=true" height="500" /> <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/match_playeradding.png?raw=true" height="500" />

The "Add Players" tab allows you to change the players in the match after the match has already been created.
In the "Players" tab, you can remove players or set a player as a winner with  <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/btn_setwinner.png?raw=true" height="30" /> . Once a player is set as a winner, the <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/btn_setwinner.png?raw=true" height="30" /> button becomes a <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/btn_unsetwinner.png?raw=true" height="30" /> button so you may unset the player. The <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/chk_filterwinners.png?raw=true" height="20" /> checkbox filters the list to show only the winners in this match. 

When we're finished with Round 1, we might want to create a new round from Round 1's winners. For this example, let's say Match 1 is the only match in Round 1, and we set both players Griffin Becker and Brian Ambrose to be the winners. Press the back arrow a couple times to go back to the "Rounds" screen:

<img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/using_a_selected_previous_rounds_winners.png?raw=true" height="500" /> 

Tap on Round 1 to select it, then check <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/chk_winners_selected_round.png?raw=true" height="20" />. Now all winners from Round 1 will become players in the new round. Let's tap <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/btn_add.png?raw=true" height="30" /> to create our new round (in this case, "Grand Finals First Set"), then view the round with <img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/btn_view.png?raw=true" height="25" />:

<img src="https://github.com/Philosobyte/TournamentManager/blob/master/doc/pictures/using_a_selected_previous_rounds_winners_result.png?raw=true" height="500" /> 

Success! Our winners Griffin Becker and Brian Ambrose are players for this round.
