package cardgame;

import java.util.ArrayList;
import java.util.List;

/**
 * Hoarder does not care about playing cards. Hoarder attempts to win by forcing the drawPile to zero every single game
 */
public class Hoarder implements PlayerStrategy {
    private int id;
    private List<Integer> opponents;
    List<Card> heldCards;
    Card toPlay;
    Boolean eightPlayed = false;
    int numberOfOpponents;

    /**
     * Initialization of the player strategy
     * @param playerId    The id for this player, assigned by the game engine
     * @param opponentIds A list of ids for this player's opponents
     */
    public void init(int playerId, List<Integer> opponentIds) {
        id = playerId;
        opponents = opponentIds;
        numberOfOpponents = opponentIds.size();
    }

    /**
     * @param cards The initial list of cards dealt to this player
     */
    public void receiveInitialCards(List<Card> cards) {
        heldCards = cards;
    }

    /**
     * @param topPileCard The card currently at the top of the pile
     * @param changedSuit The suit that the pile was changed to as the result of an "8" being
     *                    played. Will be null if no "8" was played.
     * @return Hoarder always draws a card
     */
    public boolean shouldDrawCard(Card topPileCard, Card.Suit changedSuit) {
        return true;
    }

    /**
     * adds drawn card to list of heldCards
     * @param drawnCard The card that this player has drawn
     */
    public void receiveCard(Card drawnCard) {
        // note that we allow for a card to be able to be drawn without init() being called for flexible testability
        if (heldCards == null) {
            heldCards = new ArrayList<Card>();
        }
        heldCards.add(drawnCard);
    }

    /**
     * removed card to be played from the heldCards, then returns it.
     * @return Card played
     */
    public Card playCard() {
        heldCards.remove(toPlay);
        return toPlay;
    }

    /**
     * Hoarder likes diamonds
     * @return null if player tries to declare suit when not played an 8. Otherwise, sets suit to 8's suit
     */
    public Card.Suit declareSuit() {
        if (toPlay == null || !toPlay.getRank().equals(Card.Rank.EIGHT)) {
            return null;
        }
        return Card.Suit.DIAMONDS;
    }

    /**
     * Hoarder does not care about other opponents.
     * @param opponentActions A list of what the opponents did on each of their turns
     */
    public void processOpponentActions(List<PlayerTurn> opponentActions) {
    }

    /**
     * resets the game by setting heldCards to a new list, card to be played to null, if an 8 was played to false
     */
    public void reset() {
        heldCards = new ArrayList<>();
        toPlay = null;
        eightPlayed = false;
    }
}
