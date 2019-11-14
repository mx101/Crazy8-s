package cardgame;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameTest {

    //constructor test

    @Test
    public void testConstructor() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        HashMap<Integer, PlayerStrategy> idPlayerMap = new HashMap<>();
        HashMap<PlayerStrategy, List<Card>> playerCardMap = new HashMap<>();

        List<Card> currDeck = Card.getDeck();
        EightHunter player = new EightHunter();
        playerScoreMap.put(player, 0);
        idPlayerMap.put(0, player);

        Game game = new Game(playerScoreMap, idPlayerMap, currDeck, playerCardMap);
        int expectedListSize = 1;
        int expectedCardAmount = 52;

        assertEquals(expectedListSize, game.playerScoreMap.size());
        assertEquals(expectedListSize, game.idPlayerMap.size());
        assertEquals(expectedCardAmount, game.drawPile.size());
    }

    @Test
    public void testConstructorMultiplePlayers() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        HashMap<Integer, PlayerStrategy> idPlayerMap = new HashMap<>();
        HashMap<PlayerStrategy, List<Card>> playerCardMap = new HashMap<>();

        List<Card> currDeck = Card.getDeck();
        EightHunter player = new EightHunter();
        EightHunter player2 = new EightHunter();
        playerScoreMap.put(player, 0);
        idPlayerMap.put(0, player);
        playerScoreMap.put(player2, 0);
        idPlayerMap.put(1, player2);

        Game game = new Game(playerScoreMap, idPlayerMap, currDeck, playerCardMap);
        int expectedListSize = 2;
        int expectedCardAmount = 52;

        assertEquals(expectedListSize, game.playerScoreMap.size());
        assertEquals(expectedListSize, game.idPlayerMap.size());
        assertEquals(expectedCardAmount, game.drawPile.size());
    }

    // shuffling tests

    @Test
    public void shuffledDeck() {
        List<Card> currDeck = Card.getDeck();
        List<Card> shuffledDeck = Card.getDeck();
        Game.shuffleCards(shuffledDeck);

        // note that there is an extraordinarily small chance that this assertion will fail
        assertNotEquals(currDeck, shuffledDeck);
    }

    @Test
    public void shuffledOne() {
        List<Card> currDeck = new ArrayList<>();
        Card card = new Card(Card.Suit.DIAMONDS, Card.Rank.KING);
        currDeck.add(card);
        List<Card> shuffledDeck = new ArrayList<>();
        shuffledDeck.add(card);
        Game.shuffleCards(shuffledDeck);

        assertEquals(currDeck, shuffledDeck);
    }

    // boolean check if player out of cards tests

    @Test
    public void simpleNoCardsLeft() {
        HashMap<PlayerStrategy, List<Card>> playerCards = new HashMap<>();
        PlayerStrategy player1 = new EightHunter();
        ArrayList<Card> cards = new ArrayList<>(0);
        playerCards.put(player1, cards);

        assertEquals(player1, Game.findPlayerWithNoCards(playerCards, player1));
    }

    @Test
    public void simpleRemainingCards() {
        HashMap<PlayerStrategy, List<Card>> playerCards = new HashMap<>();
        PlayerStrategy player1 = new EightHunter();
        ArrayList<Card> cards = new ArrayList<>();
        Card stillHere = new Card(Card.Suit.DIAMONDS, Card.Rank.KING);
        cards.add(stillHere);
        playerCards.put(player1, cards);

        assertNull(Game.findPlayerWithNoCards(playerCards, player1));
    }

    @Test
    public void playerDoesNotExistInPlayerCards() {
        HashMap<PlayerStrategy, List<Card>> playerCards = new HashMap<>();
        PlayerStrategy player1 = new EightHunter();
        ArrayList<Card> cards = new ArrayList<>();
        Card stillHere = new Card(Card.Suit.DIAMONDS, Card.Rank.KING);
        cards.add(stillHere);

        assertNull(Game.findPlayerWithNoCards(playerCards, player1));
    }

    // tally points tests

    @Test
    public void simplePointTallyForSingleWinner() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        HashMap<PlayerStrategy, List<Card>> playerCardMap = new HashMap<>();
        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        ArrayList<Card> oneCards = new ArrayList<>();
        ArrayList<Card> twoCards = new ArrayList<>();
        Card five = new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE);

        twoCards.add(five);
        playerScoreMap.put(player1, 0);
        playerScoreMap.put(player2, 0);
        playerCardMap.put(player1, oneCards);
        playerCardMap.put(player2, twoCards);

        Game.tallyPoints(playerCardMap, playerScoreMap, player1);

        int expectedScore = 5;
        int actualScore = playerScoreMap.get(player1);
        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void simplePointTallyForDraw() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        HashMap<PlayerStrategy, List<Card>> playerCardMap = new HashMap<>();
        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        ArrayList<Card> oneCards = new ArrayList<>();
        ArrayList<Card> twoCards = new ArrayList<>();
        Card nine = new Card(Card.Suit.HEARTS, Card.Rank.NINE);
        Card five = new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE);

        oneCards.add(nine);
        twoCards.add(five);
        playerScoreMap.put(player1, 0);
        playerScoreMap.put(player2, 0);
        playerCardMap.put(player1, oneCards);
        playerCardMap.put(player2, twoCards);

        Game.tallyPoints(playerCardMap, playerScoreMap, player1);
        Game.tallyPoints(playerCardMap, playerScoreMap, player2);

        int expectedScoreOne = 5;
        int actualScoreOne = playerScoreMap.get(player1);
        assertEquals(expectedScoreOne, actualScoreOne);

        int expectedScoreTwo = 9;
        int actualScoreTwo = playerScoreMap.get(player2);
        assertEquals(expectedScoreTwo, actualScoreTwo);
    }

    @Test
    public void pointTallyForSingleWinnerWithStartingScore() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        HashMap<PlayerStrategy, List<Card>> playerCardMap = new HashMap<>();
        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        ArrayList<Card> oneCards = new ArrayList<>();
        ArrayList<Card> twoCards = new ArrayList<>();
        Card five = new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE);

        twoCards.add(five);
        playerScoreMap.put(player1, 20);
        playerScoreMap.put(player2, 0);
        playerCardMap.put(player1, oneCards);
        playerCardMap.put(player2, twoCards);

        Game.tallyPoints(playerCardMap, playerScoreMap, player1);

        int expectedScore = 25;
        int actualScore = playerScoreMap.get(player1);
        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void pointTallyWithEights() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        HashMap<PlayerStrategy, List<Card>> playerCardMap = new HashMap<>();
        EightHunter player1 = new EightHunter();
        EightHunter player2 = new EightHunter();
        ArrayList<Card> oneCards = new ArrayList<>();
        ArrayList<Card> twoCards = new ArrayList<>();
        Card eight = new Card(Card.Suit.DIAMONDS, Card.Rank.EIGHT);

        twoCards.add(eight);
        playerScoreMap.put(player1, 0);
        playerScoreMap.put(player2, 0);
        playerCardMap.put(player1, oneCards);
        playerCardMap.put(player2, twoCards);

        Game.tallyPoints(playerCardMap, playerScoreMap, player1);

        int expectedScoreOne = 50;
        int actualScoreOne = playerScoreMap.get(player1);
        assertEquals(expectedScoreOne, actualScoreOne);
    }

    // game logic tests
    // if this comment line is still here, it is because I meant to move the following runRound tests to a different
    // testing suite but ran short on time

    @Test
    public void runRoundSetsVarsProperly() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        HashMap<Integer, PlayerStrategy> idPlayerMap = new HashMap<>();
        HashMap<PlayerStrategy, List<Card>> playerCardMap = new HashMap<>();
        List<Card> currDeck = Card.getDeck();
        EightHunter player = new EightHunter();
        playerScoreMap.put(player, 0);
        idPlayerMap.put(0, player);

        Game game = new Game(playerScoreMap, idPlayerMap, currDeck, playerCardMap);
        Game.shuffleCards(currDeck);

        List<Card> first5 = Tournament.returnCards(5, currDeck);
        player.receiveInitialCards(first5);
        playerCardMap.put(player, first5);

        int expectedSizeAfterDealing = 47;
        assertEquals(expectedSizeAfterDealing, currDeck.size());

        game.drawPile = currDeck;
        game.topCard = game.drawPile.get(0);
        game.drawPile.remove(0);

        game.runRound();

        int expectedPastTurnSize = 1;
        assertEquals(expectedPastTurnSize, game.pastTurns.size());
    }

    /**
     * this looks very long, which it is.
     * It essentially runs through a possible round that could happen and what asserts almost everything that should happen
     */
    @Test
    public void comprehensiveStandardRunRoundTest() {
        HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
        HashMap<Integer, PlayerStrategy> idPlayerMap = new HashMap<>();
        HashMap<PlayerStrategy, List<Card>> playerCardMap = new HashMap<>();
        List<Card> currDeck = Card.getDeck();
        EightHunter player = new EightHunter();
        playerScoreMap.put(player, 0);
        idPlayerMap.put(0, player);

        EightHunter player2 = new EightHunter();
        playerScoreMap.put(player2, 0);
        idPlayerMap.put(1, player2);

        EightHunter player3 = new EightHunter();
        playerScoreMap.put(player3, 0);
        idPlayerMap.put(2, player3);

        EightHunter player4 = new EightHunter();
        playerScoreMap.put(player4, 0);
        idPlayerMap.put(3, player4);

        Game game = new Game(playerScoreMap, idPlayerMap, currDeck, playerCardMap);
        Game.shuffleCards(currDeck);
        List<Card> first5 = Tournament.returnCards(5, currDeck);
        List<Card> second5 = Tournament.returnCards(5, currDeck);
        List<Card> third5 = Tournament.returnCards(5, currDeck);
        List<Card> fourth5 = Tournament.returnCards(5, currDeck);
        player.receiveInitialCards(first5);
        player2.receiveInitialCards(second5);
        player3.receiveInitialCards(third5);
        player4.receiveInitialCards(fourth5);

        playerCardMap.put(player, first5);
        playerCardMap.put(player2, second5);
        playerCardMap.put(player3, third5);
        playerCardMap.put(player4, fourth5);

        int expectedSizeAfterDealing = 32;
        assertEquals(expectedSizeAfterDealing, currDeck.size());

        game.drawPile = currDeck;
        game.topCard = game.drawPile.get(0);
        game.drawPile.remove(0);

        PlayerTurn blank = new PlayerTurn();
        blank.playerId = -1;
        game.pastTurns.add(blank);

        game.runRound();

        int expectedPastTurnSize = 4;
        assertEquals(expectedPastTurnSize, game.pastTurns.size());
        //checks to make sure the first PlayerTurn in the PlayerTurn list gets pushed out
        assertNotEquals(blank, game.pastTurns.get(0));

        int expectedPlayerCardMapSize = 4;
        assertEquals(expectedPlayerCardMapSize, game.playerCardMap.size());
    }
}
