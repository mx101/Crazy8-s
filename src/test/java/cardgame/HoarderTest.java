package cardgame;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class HoarderTest {

    Hoarder player = new Hoarder();
    List<Card> currDeck = Card.getDeck();

    @Before
    public void setup() {
        player.reset();
        player.init(0, new ArrayList<>());
        currDeck = Card.getDeck();
    }

    // receiveInitialCards tests with various inputs including default 5

    @Test
    public void holdNoCardsTest() {
        int initialCardAmount = 0;
        List<Card> toHand = Tournament.returnCards(initialCardAmount, currDeck);
        player.receiveInitialCards(toHand);

        assertEquals(initialCardAmount, player.heldCards.size());
    }

    @Test
    public void hold5CardsTest() {
        int initialCardAmount = 5;
        List<Card> toHand = Tournament.returnCards(initialCardAmount, currDeck);
        player.receiveInitialCards(toHand);

        assertEquals(initialCardAmount, player.heldCards.size());
    }

    @Test
    public void hold1CardTest() {
        int initialCardAmount = 1;
        List<Card> toHand = Tournament.returnCards(initialCardAmount, currDeck);
        player.receiveInitialCards(toHand);

        assertEquals(initialCardAmount, player.heldCards.size());
    }

    // receiveCard tests

    @Test
    public void simpleDraw() {
        int initialCardAmount = 5;
        List<Card> initialToHand = Tournament.returnCards(initialCardAmount, currDeck);
        player.receiveInitialCards(initialToHand);

        Card miscCard = new Card(Card.Suit.SPADES, Card.Rank.EIGHT);
        player.receiveCard(miscCard);

        int expectedHandSize = 6;

        assertEquals(expectedHandSize, player.heldCards.size());
    }

    @Test
    public void simpleDrawFromNothing() {
        Card miscCard = new Card(Card.Suit.SPADES, Card.Rank.EIGHT);
        player.receiveCard(miscCard);

        int expectedHandSize = 1;

        assertEquals(expectedHandSize, player.heldCards.size());
    }

    // shouldDrawCard tests
    // Hoarder always draws no matter what

    @Test
    public void simpleYesDraw() {
        Card inHand = new Card(Card.Suit.SPADES, Card.Rank.ACE);
        player.receiveCard(inHand);

        Card topCard = new Card(Card.Suit.DIAMONDS, Card.Rank.EIGHT);
        Card.Suit changedBool = null;

        assertTrue(player.shouldDrawCard(topCard, changedBool));
    }

    @Test
    public void simpleNoDraw() {
        Card inHand = new Card(Card.Suit.DIAMONDS, Card.Rank.ACE);
        player.receiveCard(inHand);

        Card topCard = new Card(Card.Suit.DIAMONDS, Card.Rank.EIGHT);
        Card.Suit changedBool = null;

        assertTrue(player.shouldDrawCard(topCard, changedBool));
    }

    @Test
    public void EightPlayedMatchNoDraw() {
        Card inHand = new Card(Card.Suit.DIAMONDS, Card.Rank.ACE);
        player.receiveCard(inHand);

        Card topCard = new Card(Card.Suit.SPADES, Card.Rank.EIGHT);
        Card.Suit changedBool = Card.Suit.DIAMONDS;

        assertTrue(player.shouldDrawCard(topCard, changedBool));
    }

    @Test
    public void EightPlayedTopCardMatchDraw() {
        Card inHand = new Card(Card.Suit.SPADES, Card.Rank.ACE);
        player.receiveCard(inHand);

        Card topCard = new Card(Card.Suit.SPADES, Card.Rank.EIGHT);
        Card.Suit changedBool = Card.Suit.DIAMONDS;

        assertTrue(player.shouldDrawCard(topCard, changedBool));
    }

    @Test
    public void EightPlayedTopCardNoMatchDraw() {
        Card inHand = new Card(Card.Suit.SPADES, Card.Rank.ACE);
        player.receiveCard(inHand);

        Card topCard = new Card(Card.Suit.HEARTS, Card.Rank.EIGHT);
        Card.Suit changedBool = Card.Suit.DIAMONDS;

        assertTrue(player.shouldDrawCard(topCard, changedBool));
    }

    // processOpponentActions tests
    // this is placed beneath drawCards as opponent actions for ImpPlayerStrat does affect the draw

    @Test
    public void couldPlayButEightLastPlayed() {
        Card inHand = new Card(Card.Suit.SPADES, Card.Rank.ACE);
        player.receiveCard(inHand);
        Card topCard = new Card(Card.Suit.HEARTS, Card.Rank.EIGHT);
        Card.Suit changedBool = Card.Suit.SPADES;

        List<PlayerTurn> turns = new ArrayList<>();
        PlayerTurn turn1 = new PlayerTurn();
        turn1.playedCard = new Card(Card.Suit.CLUBS, Card.Rank.EIGHT);
        turns.add(turn1);

        player.processOpponentActions(turns);

        assertTrue(player.shouldDrawCard(topCard, changedBool));
    }

    @Test
    public void couldNotPlayButEightLastPlayed() {
        Card inHand = new Card(Card.Suit.SPADES, Card.Rank.ACE);
        player.receiveCard(inHand);
        Card topCard = new Card(Card.Suit.HEARTS, Card.Rank.EIGHT);
        Card.Suit changedBool = Card.Suit.HEARTS;

        List<PlayerTurn> turns = new ArrayList<>();
        PlayerTurn turn1 = new PlayerTurn();
        turn1.playedCard = new Card(Card.Suit.CLUBS, Card.Rank.EIGHT);
        turns.add(turn1);

        player.processOpponentActions(turns);

        assertTrue(player.shouldDrawCard(topCard, changedBool));
    }

    @Test
    public void eightNotPlayedNoDraw() {
        Card inHand = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        player.receiveCard(inHand);
        Card topCard = new Card(Card.Suit.HEARTS, Card.Rank.EIGHT);
        Card.Suit changedBool = Card.Suit.HEARTS;

        List<PlayerTurn> turns = new ArrayList<>();
        PlayerTurn turn1 = new PlayerTurn();
        turn1.playedCard = new Card(Card.Suit.CLUBS, Card.Rank.NINE);
        turns.add(turn1);

        player.processOpponentActions(turns);

        assertTrue(player.shouldDrawCard(topCard, changedBool));
    }

    @Test
    public void eightNotPlayedYesDraw() {
        Card inHand = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        player.receiveCard(inHand);
        Card topCard = new Card(Card.Suit.HEARTS, Card.Rank.EIGHT);
        Card.Suit changedBool = Card.Suit.CLUBS;

        List<PlayerTurn> turns = new ArrayList<>();
        PlayerTurn turn1 = new PlayerTurn();
        turn1.playedCard = new Card(Card.Suit.CLUBS, Card.Rank.TEN);
        turns.add(turn1);

        player.processOpponentActions(turns);

        assertTrue(player.shouldDrawCard(topCard, changedBool));
    }

    // playCard tests

    @Test
    public void simplePlayCard() {
        Card inHand = new Card(Card.Suit.SPADES, Card.Rank.ACE);
        player.receiveCard(inHand);
        player.toPlay = inHand;
        int expectedHandSizeBefore = 1;

        assertEquals(expectedHandSizeBefore, player.heldCards.size());
        assertEquals(inHand, player.playCard());

        int expectedHandSizeAfter = 0;

        assertEquals(expectedHandSizeAfter, player.heldCards.size());
    }

    @Test
    public void nullPlayCard() {
        Card inHand = new Card(Card.Suit.SPADES, Card.Rank.ACE);
        player.receiveCard(inHand);
        player.toPlay = null;
        int expectedHandSizeBefore = 1;

        assertEquals(expectedHandSizeBefore, player.heldCards.size());
        assertNull(player.playCard());

        int expectedHandSizeAfter = 1;

        assertEquals(expectedHandSizeAfter, player.heldCards.size());
    }

    // declareSuit tests

    @Test
    public void simpleDeclareSuit() {
        Card inHand = new Card(Card.Suit.SPADES, Card.Rank.EIGHT);
        player.receiveCard(inHand);
        player.toPlay = inHand;

        Card.Suit expectedSuit = Card.Suit.SPADES;

        assertEquals(expectedSuit, player.declareSuit());
    }

    @Test
    public void unableDeclareSuit() {
        Card inHand = new Card(Card.Suit.SPADES, Card.Rank.NINE);
        player.receiveCard(inHand);
        player.toPlay = inHand;

        assertNull(player.declareSuit());
    }

    @Test
    public void nullDeclareSuit() {
        Card inHand = new Card(Card.Suit.SPADES, Card.Rank.NINE);
        player.receiveCard(inHand);
        player.toPlay = null;

        assertNull(player.declareSuit());
    }

    // Reset test

    @Test
    public void simpleReset() {
        Card inHand = new Card(Card.Suit.SPADES, Card.Rank.NINE);
        player.receiveCard(inHand);
        int expectedHandSizeBefore = 1;

        assertEquals(expectedHandSizeBefore, player.heldCards.size());

        player.reset();
        int expectedHandSizeAfter = 0;

        assertEquals(expectedHandSizeAfter, player.heldCards.size());
        assertNull(player.toPlay);
        assertFalse(player.eightPlayed);
    }

    @Test
    public void resetWithMoreCards() {
        Card inHand = new Card(Card.Suit.SPADES, Card.Rank.NINE);
        Card inHandTwo = new Card(Card.Suit.DIAMONDS, Card.Rank.JACK);
        Card inHandThree = new Card(Card.Suit.CLUBS, Card.Rank.SEVEN);
        player.receiveCard(inHand);
        player.receiveCard(inHandTwo);
        player.receiveCard(inHandThree);
        int expectedHandSizeBefore = 3;

        assertEquals(expectedHandSizeBefore, player.heldCards.size());

        player.reset();
        int expectedHandSizeAfter = 0;

        assertEquals(expectedHandSizeAfter, player.heldCards.size());
        assertNull(player.toPlay);
        assertFalse(player.eightPlayed);
    }
}
