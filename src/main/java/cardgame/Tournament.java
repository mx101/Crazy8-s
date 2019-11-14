package cardgame;

import java.util.*;

public class Tournament {
    static HashMap<PlayerStrategy, Integer> playerScoreMap = new HashMap<>();
    //please note that at the time of declaring idPlayerMap, I did not plan on having more than two maps
    //the majority of my code is now written with the current idPlayerMap but in the future I would format it to be
    //playerIDMap to match the format of playerScore and playerCard
    static HashMap<Integer, PlayerStrategy> idPlayerMap = new HashMap<>();

    static int defaultPlayerNumber = 4;

    public static void main(String[] args) {
        PlayerStrategy[] defaultPlayers = new PlayerStrategy[defaultPlayerNumber];
        defaultPlayers[0] = new EightHunter();
        defaultPlayers[1] = new Hoarder();
        defaultPlayers[2] = new EightHunter();
        defaultPlayers[3] = new Hoarder();
        loadPlayers(defaultPlayers);
        runGame(playerScoreMap, idPlayerMap);
        System.out.println(output());
    }

    /**
     * load in the provided players and initialize their scores to 0.
     */
    public static void loadPlayers(PlayerStrategy[] inputPlayers) {
        if (inputPlayers.length <= 1) {
            System.out.println("Not enough players, exiting game...");
            System.exit(-1);
        }

        int idCount = 0;
        ArrayList<Integer> allIDs = new ArrayList<>(inputPlayers.length);

        // assigns each player an ID and logs them in the maps
        for (PlayerStrategy currPlayer : inputPlayers) {
            playerScoreMap.put(currPlayer, 0);
            idPlayerMap.put(idCount, currPlayer);
            allIDs.add(idCount);
            idCount++;
        }

        // calls init on each player
        for (Map.Entry<Integer, PlayerStrategy> currEntry : idPlayerMap.entrySet()) {
            int playerID = currEntry.getKey();
            List<Integer> opponentIDs = allIDs;
            opponentIDs.remove(playerID);
            currEntry.getValue().init(playerID, opponentIDs);
            opponentIDs.add(playerID, playerID);
        }
    }

    public static void runGame(HashMap<PlayerStrategy, Integer> playerScoreMapIn, HashMap<Integer, PlayerStrategy> idPlayerMapIn) {
        HashMap<PlayerStrategy, List<Card>> playerCardMap = new HashMap<>();

        while(winner == null && cheater == null) {
            List<Card> currDeck = Card.getDeck();
            int initCardCount = 5;

            //gives each player their starting 5 cards, logs this in the map
            for (PlayerStrategy currPlayer : playerScoreMap.keySet()) {
                List<Card> cardsGiven = returnCards(initCardCount, currDeck);
                currPlayer.receiveInitialCards(cardsGiven);
                playerCardMap.put(currPlayer, cardsGiven);

                // if the PlayerStrategy implementation's receiveInitialCards method somehow changes
                // the cards given to the player, they are cheating
                if (!playerCardMap.get(currPlayer).equals(cardsGiven)) {
                    cheater = currPlayer;
                    break;
                }
            }

            if (cheater != null) {
                continue;
            }

            Game currGame = new Game(playerScoreMapIn, idPlayerMapIn, currDeck, playerCardMap);
            playerScoreMap = currGame.playGame();
            detectWinnerCheater(playerScoreMap);
        }
    }

    /**
     * @param number of cards to retrieve
     * @return a list of cards
     */
    public static List<Card> returnCards (int number, List<Card> availableCards) {
        if (number < 0) {
            return null;
        }

        Random rand = new Random();
        List<Card> returnList = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            int indexToPull = rand.nextInt(availableCards.size());
            returnList.add(availableCards.get(indexToPull));
            availableCards.remove(indexToPull);
        }

        return returnList;
    }

    static PlayerStrategy winner = null;
    static PlayerStrategy cheater = null;

    /**
     * helper method for runGame, returns null if a player has not yet won
     * if multiple players can win, returns the player with the highest score
     * @param playerScoreMapIn map of playerStrategies to their scores
     * @return which player wins
     */
    static PlayerStrategy detectWinnerCheater(HashMap<PlayerStrategy, Integer> playerScoreMapIn) {
        int winningNumber = 200;
        int highestScore = 0;
        int cheatingNumber = -1;

        for (Map.Entry<PlayerStrategy, Integer> currPlayer : playerScoreMapIn.entrySet()) {
            //cheating takes precedence over winning
            if (currPlayer.getValue() == cheatingNumber) {
                cheater = currPlayer.getKey();
                return cheater;
            }

            if (currPlayer.getValue() >= winningNumber && currPlayer.getValue() >= highestScore) {
                highestScore = currPlayer.getValue();
                winner = currPlayer.getKey();
            }
        }

        return winner;
    }

    static int outputID = -1;

    /**
     * @return the ID of the winning or cheating player
     */
    public static String output() {
        for (Map.Entry<Integer, PlayerStrategy> currID : idPlayerMap.entrySet()) {
            //cheating takes precedence over winning
            if (currID.getValue().equals(cheater)) {
                outputID = currID.getKey();
                break;
            }
            if (currID.getValue().equals(winner)) {
                outputID = currID.getKey();
            }
        }

        if (cheater != null) {
            return "Player " + outputID + " was caught cheating!";
        } else if (winner != null) {
            return "Player " + outputID + " wins";
        } else {
            return "No winner or cheater found";
        }
    }
}
