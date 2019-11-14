package cardgame;

import java.util.*;

public class Game {
    static final int TOP = 0;

    HashMap<PlayerStrategy, Integer> playerScoreMap;
    HashMap<Integer, PlayerStrategy> idPlayerMap;
    HashMap<PlayerStrategy, List<Card>> playerCardMap;
    boolean cheaterDetected = false;
    int cheatingPlayerID = -1;

    List<Card> drawPile;
    Card topCard;
    Card.Suit eightSuit = null;
    ArrayList<PlayerTurn> pastTurns = new ArrayList<>();

    Game(HashMap<PlayerStrategy, Integer> playerScoreMapIn,
         HashMap<Integer, PlayerStrategy> idPlayerMapIn,
         List<Card> currDeck,
         HashMap<PlayerStrategy, List<Card>> playerCardMapIn) {

        playerScoreMap = playerScoreMapIn;
        idPlayerMap = idPlayerMapIn;
        drawPile = currDeck;
        playerCardMap = playerCardMapIn;
    }

    public HashMap<PlayerStrategy, Integer> playGame() {
        if (playerScoreMap == null || idPlayerMap == null || playerCardMap == null) {
            System.exit(-1);
        }

        shuffleCards(drawPile);
        topCard = drawPile.get(TOP);
        drawPile.remove(TOP);

        while(drawPile.size() != 0 && playerOutOfCards == null && !cheaterDetected) {
            runRound();
        }

        if (drawPile.size() == 0) {
            for (Map.Entry<PlayerStrategy, List<Card>> currEntry : playerCardMap.entrySet()) {
                tallyPoints(playerCardMap, playerScoreMap, currEntry.getKey());
            }
        } else {
            tallyPoints(playerCardMap, playerScoreMap, playerOutOfCards);
        }

        return playerScoreMap;
    }

    public static void tallyPoints(HashMap<PlayerStrategy, List<Card>> playerCardMap,
                            HashMap<PlayerStrategy, Integer> playerScoreMap,
                            PlayerStrategy player) {
        int scoreToAdd = 0;

        for (Map.Entry<PlayerStrategy, List<Card>> currEntry : playerCardMap.entrySet()) {
            if (currEntry.getKey().equals(player)) {
                continue;
            }

            for (Card currCard : currEntry.getValue()) {
                scoreToAdd += currCard.getPointValue();
            }
        }

        int originalScore = playerScoreMap.get(player);
        playerScoreMap.put(player, originalScore + scoreToAdd);
    }

    private PlayerStrategy playerOutOfCards = null;

    /**
     * main round logic block
     */
    public void runRound() {
        for (Map.Entry<Integer, PlayerStrategy> currEntry : idPlayerMap.entrySet()) {
            PlayerStrategy currStrategy = currEntry.getValue();
            PlayerTurn currTurn = new PlayerTurn();
            currTurn.playerId = currEntry.getKey();

            int beforeScore = playerScoreMap.get(currStrategy);
            List<Card> beforeHand = playerCardMap.get(currStrategy);

            currStrategy.processOpponentActions(pastTurns);
            boolean drawBool = currStrategy.shouldDrawCard(topCard, eightSuit);

            // if the player's score or hand somehow changes when calling these void methods, they are cheating
            // note that there shouldn't be a way for a player to modify these maps from the interface
            // this is just an additional safeguard
            if (beforeScore != playerScoreMap.get(currStrategy)
                    || !beforeHand.equals(playerCardMap.get(currStrategy))) {
                cheaterDetected = true;
                playerScoreMap.put(currStrategy, cheatingPlayerID);
                return;
            }

            if (drawPile.size() == 0) {
                return;
            }

            // determines if a player should draw or play
            if (drawBool) {
                currStrategy.receiveCard(drawPile.get(TOP));
                playerCardMap.get(currStrategy).add(drawPile.get(TOP));
                drawPile.remove(TOP);
                currTurn.drewACard = true;
                currTurn.playedCard = null;
            } else {
                beforeScore = playerScoreMap.get(currStrategy);
                beforeHand = playerCardMap.get(currStrategy);
                Card toPlay = currStrategy.playCard();

                // if the player attempts to play a card they do not have, they are cheating
                if (!playerCardMap.get(currStrategy).contains(toPlay)) {
                    cheaterDetected = true;
                    playerScoreMap.put(currStrategy, cheatingPlayerID);
                    return;
                }

                // or if the player's score or hand somehow changes when calling playCard, they are cheating
                if (beforeScore != playerScoreMap.get(currStrategy)
                        || !beforeHand.equals(playerCardMap.get(currStrategy))) {
                    cheaterDetected = true;
                    playerScoreMap.put(currStrategy, cheatingPlayerID);
                    return;
                }

                if (toPlay.getRank().equals(Card.Rank.EIGHT)) {
                    currTurn.declaredSuit = currStrategy.declareSuit();
                    drawPile.add(toPlay);
                    shuffleCards(drawPile);
                    playerCardMap.get(currStrategy).remove(toPlay);
                } else {
                    topCard = toPlay;
                }

                currTurn.drewACard = false;
                currTurn.playedCard = toPlay;
                playerCardMap.get(currStrategy).remove(toPlay);

                playerOutOfCards = findPlayerWithNoCards(playerCardMap, currStrategy);
                if (playerOutOfCards != null) {
                    return;
                }
            }

            currTurn.drewACard = drawBool;
            currTurn.playedCard = currEntry.getValue().playCard();

            pastTurns.add(currTurn);

            // once everyone has played at least once, remove the first turn as this corresponds to
            // the current player's previous turn
            if (pastTurns.size() > playerScoreMap.size()) {
                pastTurns.remove(TOP);
            }
        }
    }

    /**
     * @param playerCards
     * @return whether a player is out of cards
     */
    public static PlayerStrategy findPlayerWithNoCards(HashMap<PlayerStrategy, List<Card>> playerCards, PlayerStrategy player) {
        if (!playerCards.keySet().contains(player)) {
            return null;
        }
        if (playerCards.get(player).size() == 0) {
            return player;
        }
        return null;
    }

    /**
     * shuffles cards, if top card is an 8, shuffle again
     * @param toShuffle list of cards we want shuffled
     */
    public static void shuffleCards(List<Card> toShuffle) {
        Collections.shuffle(toShuffle);

        boolean topIsEight = false;

        if (toShuffle.get(0).getRank().equals(Card.Rank.EIGHT)) {
            topIsEight = true;
        }

        // if top card is an 8, reshuffle draw pile
        while(topIsEight) {
            Collections.shuffle(toShuffle);
            if (toShuffle.get(0).getRank().equals(Card.Rank.EIGHT)) {
                topIsEight = true;
            } else {
                topIsEight = false;
            }
        }
    }
}
