package cardgame;

import java.util.ArrayList;
import java.util.List;

public class EightHunter implements PlayerStrategy {
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
     * @return if a card should be drawn or not
     */
    public boolean shouldDrawCard(Card topPileCard, Card.Suit changedSuit) {
        // check if any opponent played an 8
        if (eightPlayed) {
            eightPlayed = false;
            return true;
        }

        if (changedSuit != null) {
            for (Card card : heldCards) {
                if (card.getSuit().equals(changedSuit)) {
                    return false;
                }
            }
            return true;
        } else {
            for (Card card : heldCards) {
                if (card.getSuit().equals(topPileCard.getSuit())) {
                    toPlay = card;
                    return false;
                }
            }
            return true;
        }
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
        return toPlay;
    }

    /**
     * @return null if player tries to declare suit when not played an 8. Otherwise, sets suit to 8's suit
     */
    public Card.Suit declareSuit() {
        if (toPlay == null || !toPlay.getRank().equals(Card.Rank.EIGHT)) {
            return null;
        }
        return toPlay.getSuit();
    }

    /**
     * If EightHunter sees an 8 played, draw a card on turn
     * @param opponentActions A list of what the opponents did on each of their turns
     */
    public void processOpponentActions(List<PlayerTurn> opponentActions) {
        if (opponentActions == null || opponentActions.size() == 0) {
            return;
        }

        for (PlayerTurn currTurn : opponentActions) {
            if (currTurn == null || currTurn.playedCard == null) {
                continue;
            }

            if (currTurn.playedCard.getRank().equals(Card.Rank.EIGHT)) {
                eightPlayed = true;
                break;
            }
        }
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
