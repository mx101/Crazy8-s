package cardgame;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class TournamentTest {
    // Please note that some of these tests may fail when run together as some tests do modify Tournament member variables

    // Load Player Tests
    // note that there used to exist 0 and 1 player loading tests here
    // they have been removed because I did not find a way to assert whether a program exited

    @Test
    public void twoPlayersLoaded() {
        PlayerStrategy[] testPlayers = new PlayerStrategy[2];
        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        testPlayers[0] = player1;
        testPlayers[1] = player2;

        Tournament.loadPlayers(testPlayers);
        int expectedPlayerCount = 2;

        assertEquals(expectedPlayerCount, Tournament.playerScoreMap.size());
    }

    @Test
    public void fourPlayersLoaded() {
        PlayerStrategy[] testPlayers = new PlayerStrategy[4];
        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();
        testPlayers[0] = player1;
        testPlayers[1] = player2;
        testPlayers[2] = player3;
        testPlayers[3] = player4;
        Tournament.loadPlayers(testPlayers);
        int expectedPlayerCount = 4;

        assertEquals(expectedPlayerCount, Tournament.playerScoreMap.size());
    }

    // Win Detection

    @Test
    public void emptyWinnerDetection() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();

        assertNull(Tournament.detectWinnerCheater(playerScoreMap));
    }

    @Test
    public void simple1PlayerYesWinDetection() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        EightHunter player1 = new EightHunter();

        playerScoreMap.put(player1, 200);

        assertEquals(player1, Tournament.detectWinnerCheater(playerScoreMap));
    }

    @Test
    public void simple1PlayerNoWinDetection() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        EightHunter player1 = new EightHunter();

        playerScoreMap.put(player1, 100);

        assertNull(Tournament.detectWinnerCheater(playerScoreMap));
    }

    @Test
    public void simple4PlayerWinnerDetection() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();

        playerScoreMap.put(player1, 100);
        playerScoreMap.put(player2, 200);
        playerScoreMap.put(player3, 130);
        playerScoreMap.put(player4, 199);

        assertEquals(player2, Tournament.detectWinnerCheater(playerScoreMap));
    }

    @Test
    public void simple4PlayerNoWinnerDetection() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();

        playerScoreMap.put(player1, 100);
        playerScoreMap.put(player2, 0);
        playerScoreMap.put(player3, 130);
        playerScoreMap.put(player4, 199);

        assertNull(Tournament.detectWinnerCheater(playerScoreMap));
    }

    @Test
    public void fourthPlayerWinDetection() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();

        playerScoreMap.put(player1, 100);
        playerScoreMap.put(player2, 200);
        playerScoreMap.put(player3, 130);
        playerScoreMap.put(player4, 300);

        assertEquals(player4, Tournament.detectWinnerCheater(playerScoreMap));
    }

    @Test
    public void firstScorerWinDetection() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();

        playerScoreMap.put(player1, 400);
        playerScoreMap.put(player2, 200);
        playerScoreMap.put(player3, 130);
        playerScoreMap.put(player4, 300);

        assertEquals(player1, Tournament.detectWinnerCheater(playerScoreMap));
    }

    // Cheater Detection

    @Test
    public void emptyCheaterDetection() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();

        assertNull(Tournament.detectWinnerCheater(playerScoreMap));
    }

    @Test
    public void simple1PlayerYesCheatDetection() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        EightHunter player1 = new EightHunter();

        playerScoreMap.put(player1, -1);

        assertEquals(player1, Tournament.detectWinnerCheater(playerScoreMap));
    }

    @Test
    public void simple1PlayerNoCheatDetection() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        EightHunter player1 = new EightHunter();

        playerScoreMap.put(player1, 100);

        assertNull(Tournament.detectWinnerCheater(playerScoreMap));
    }

    @Test
    public void simple4PlayerCheaterDetection() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();

        playerScoreMap.put(player1, 100);
        playerScoreMap.put(player2, -1);
        playerScoreMap.put(player3, 130);
        playerScoreMap.put(player4, 199);

        assertEquals(player2, Tournament.detectWinnerCheater(playerScoreMap));
    }

    @Test
    public void simple4PlayerNoCheaterDetection() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();

        playerScoreMap.put(player1, 100);
        playerScoreMap.put(player2, 0);
        playerScoreMap.put(player3, 130);
        playerScoreMap.put(player4, 199);

        assertNull(Tournament.detectWinnerCheater(playerScoreMap));
    }

    @Test
    public void fourthPlayerCheatDetection() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();

        playerScoreMap.put(player1, 100);
        playerScoreMap.put(player2, 200);
        playerScoreMap.put(player3, 130);
        playerScoreMap.put(player4, -1);

        assertEquals(player4, Tournament.detectWinnerCheater(playerScoreMap));
    }

    @Test
    public void firstScorerCheatDetection() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();

        playerScoreMap.put(player1, -1);
        playerScoreMap.put(player2, 200);
        playerScoreMap.put(player3, 130);
        playerScoreMap.put(player4, 300);

        assertEquals(player1, Tournament.detectWinnerCheater(playerScoreMap));
    }

    // Output Winner

    @Test
    public void noPlayersOutputWinner() {
        Tournament.playerScoreMap = new HashMap<>();
        Tournament.detectWinnerCheater(Tournament.playerScoreMap);
        String output = "No winner or cheater found";

        assertEquals(output, Tournament.output());
    }

    @Test
    public void singlePlayerWinnerOutput() {
        EightHunter player1 = new EightHunter();

        Tournament.playerScoreMap.put(player1, 200);
        Tournament.idPlayerMap.put(1, player1);

        Tournament.detectWinnerCheater(Tournament.playerScoreMap);
        int expectedWinningID = 1;
        String output = "Player " + expectedWinningID + " wins";

        assertEquals(output, Tournament.output());
    }

    @Test
    public void singlePlayerNoWinOutput() {
        EightHunter player1 = new EightHunter();

        Tournament.playerScoreMap.put(player1, 100);
        Tournament.idPlayerMap.put(1, player1);

        Tournament.detectWinnerCheater(Tournament.playerScoreMap);

        String output = "No winner or cheater found";

        assertEquals(output, Tournament.output());
    }

    @Test
    public void simpleOutputWinner() {

        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();

        Tournament.playerScoreMap.put(player1, 100);
        Tournament.playerScoreMap.put(player2, 200);
        Tournament.playerScoreMap.put(player3, 130);
        Tournament.playerScoreMap.put(player4, 199);

        Tournament.idPlayerMap.put(1, player1);
        Tournament.idPlayerMap.put(2, player2);
        Tournament.idPlayerMap.put(3, player3);
        Tournament.idPlayerMap.put(4, player4);

        Tournament.detectWinnerCheater(Tournament.playerScoreMap);
        int expectedWinningID = 2;
        String output = "Player " + expectedWinningID + " wins";

        assertEquals(output, Tournament.output());
    }

    @Test
    public void noWinnerOutput() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();

        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();

        Tournament.playerScoreMap.put(player1, 100);
        Tournament.playerScoreMap.put(player2, 100);
        Tournament.playerScoreMap.put(player3, 130);
        Tournament.playerScoreMap.put(player4, 199);

        Tournament.idPlayerMap.put(1, player1);
        Tournament.idPlayerMap.put(2, player2);
        Tournament.idPlayerMap.put(3, player3);
        Tournament.idPlayerMap.put(4, player4);

        Tournament.winner = null;
        String output = "No winner or cheater found";

        assertEquals(output, Tournament.output());
    }

    @Test
    public void multipleWinnersOutput() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();

        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();

        Tournament.playerScoreMap.put(player1, 100);
        Tournament.playerScoreMap.put(player2, 200);
        Tournament.playerScoreMap.put(player3, 130);
        Tournament.playerScoreMap.put(player4, 299);

        Tournament.idPlayerMap.put(1, player1);
        Tournament.idPlayerMap.put(2, player2);
        Tournament.idPlayerMap.put(3, player3);
        Tournament.idPlayerMap.put(4, player4);

        Tournament.winner = player4;
        int expectedWinningID = 4;
        String output = "Player " + expectedWinningID + " wins";

        assertEquals(output, Tournament.output());
    }

    // Output Cheater
    // note that some test cases are covered under the Output Winner tests as these two share the same method

    @Test
    public void singlePlayerCheaterOutput() {
        EightHunter player1 = new EightHunter();

        Tournament.playerScoreMap.put(player1, -1);
        Tournament.idPlayerMap.put(1, player1);

        Tournament.detectWinnerCheater(Tournament.playerScoreMap);
        int expectedCheatingID = 1;
        String output = "Player " + expectedCheatingID + " was caught cheating!";

        assertEquals(output, Tournament.output());
    }

    @Test
    public void simpleOutputCheater() {

        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();

        Tournament.playerScoreMap.put(player1, 100);
        Tournament.playerScoreMap.put(player2, 100);
        Tournament.playerScoreMap.put(player3, -1);
        Tournament.playerScoreMap.put(player4, 199);

        Tournament.idPlayerMap.put(1, player1);
        Tournament.idPlayerMap.put(2, player2);
        Tournament.idPlayerMap.put(3, player3);
        Tournament.idPlayerMap.put(4, player4);

        Tournament.detectWinnerCheater(Tournament.playerScoreMap);
        int expectedCheatingID = 3;
        String output = "Player " + expectedCheatingID + " was caught cheating!";

        assertEquals(output, Tournament.output());
    }

    @Test
    public void outputCheaterWithWinningPlayer() {

        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();

        Tournament.playerScoreMap.put(player1, 100);
        Tournament.playerScoreMap.put(player2, 200);
        Tournament.playerScoreMap.put(player3, -1);
        Tournament.playerScoreMap.put(player4, 199);

        Tournament.idPlayerMap.put(1, player1);
        Tournament.idPlayerMap.put(2, player2);
        Tournament.idPlayerMap.put(3, player3);
        Tournament.idPlayerMap.put(4, player4);

        Tournament.detectWinnerCheater(Tournament.playerScoreMap);
        int expectedCheatingID = 3;
        String output = "Player " + expectedCheatingID + " was caught cheating!";

        assertEquals(output, Tournament.output());
    }

    @Test
    public void noCheaterOutput() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();

        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        EightHunter player3 = new EightHunter();
        EightHunter player4 = new EightHunter();

        Tournament.playerScoreMap.put(player1, 100);
        Tournament.playerScoreMap.put(player2, 100);
        Tournament.playerScoreMap.put(player3, 130);
        Tournament.playerScoreMap.put(player4, 199);

        Tournament.idPlayerMap.put(1, player1);
        Tournament.idPlayerMap.put(2, player2);
        Tournament.idPlayerMap.put(3, player3);
        Tournament.idPlayerMap.put(4, player4);

        Tournament.cheater = null;
        String output = "No winner or cheater found";

        assertEquals(output, Tournament.output());
    }

    // Random Cards Test

    @Test
    public void singleCardPull() {
        List<Card> currDeck = Card.getDeck();
        int expectedCardListSize = 1;
        int expectedBeginning = 52;
        int expectedRemaining = 51;

        assertEquals(expectedBeginning, currDeck.size());
        assertEquals(expectedCardListSize, Tournament.returnCards(1, currDeck).size());
        assertEquals(expectedRemaining, currDeck.size());
    }

    @Test
    public void noCardPull() {
        List<Card> currDeck = Card.getDeck();
        int expectedCardListSize = 0;
        int expectedRemaining = 52;

        assertEquals(expectedCardListSize, Tournament.returnCards(0, currDeck).size());
        assertEquals(expectedRemaining, currDeck.size());
    }

    @Test
    public void badCardPull() {
        List<Card> currDeck = Card.getDeck();
        int expectedRemaining = 52;

        assertNull(Tournament.returnCards(-1, currDeck));
        assertEquals(expectedRemaining, currDeck.size());
    }

    @Test
    public void FiveCardPull() {
        List<Card> currDeck = Card.getDeck();
        int expectedCardListSize = 5;
        int expectedRemaining = 47;

        assertEquals(expectedCardListSize, Tournament.returnCards(5, currDeck).size());
        assertEquals(expectedRemaining, currDeck.size());
    }

}
