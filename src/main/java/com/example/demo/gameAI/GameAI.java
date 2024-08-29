package com.example.demo.gameAI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

enum UserType {
    ADMIN, NON_ADMIN;
}

enum Status {

    NEW, RUNNING, END
}

public class GameAI {


    public void boot() {
        GameService gameService = new GameService();
        MoveService moveService = new MoveService();
        TwoPlayerWithComputerGame game = gameService.startNewGameWithComputer(UUID.randomUUID());
        System.out.println(game.player1);
        gameService.startGame(game);
        Move move1 = new Move();
        Position position = new Position();
        move1.position = position;
        position.x = 0;
        position.y = 0;
        moveService.makeMove(game, move1);
        moveService.makeMove(game, move1);
        position.x = 1;
        position.y = 1;
        moveService.makeMove(game, move1);
        moveService.makeMove(game, move1);
        position.x = 2;
        position.y = 2;
        moveService.makeMove(game, move1);
        moveService.makeMove(game, move1);
        position.x = 3;
        position.y = 3;
        moveService.makeMove(game, move1);
        moveService.makeMove(game, move1);
        System.out.println("Shivam");


    }

}

class Audit {
    String createdBy;
    String createdTine;
    String updatedBy;
    String updatedTime;
}

class Statics {
    UUID gameId;
    int score;
}

class User extends Audit {

    UUID uuid;
    String name;
    List<Statics> stats;
    int globalRank;
    int Rating;
    UserType type;
}

class Position {
    int x;
    int y;
    int value = -1;
    UUID playerID;
}

class Board {
    Position[][] board;
    int xsixe;
    int ysize;

    public Board(int xsize, int ysize) {
        this.xsixe = xsize;
        this.ysize = ysize;
        board = new Position[xsize][ysize];
        for (int i = 0; i < xsize; i++) {
            for (int j = 0; j < ysize; j++) {
                Position position = new Position();
                position.x = i;
                position.y = j;
                board[i][j] = position;
            }
        }
    }
}

class Move extends Audit {
    UUID userId;
    Position position;
    int value = -1;
}

class Game extends Audit {
    UUID gameID = UUID.randomUUID();
    String winner;
    UUID currentTurn;
    Board board;
    List<User> ranks = new ArrayList<>();
    List<Move> moves = new ArrayList<>();
    Status status = Status.NEW;
}

class TwoPlayerGame extends Game {
    UUID player1;
    UUID player2;
}

class TwoPlayerWithComputerGame extends TwoPlayerGame {
    UUID player1;
    UUID player2 = GameUtil.getComputerPlayerId();
}


class GameService {

    static int SIZE = 7;

    public TwoPlayerWithComputerGame startNewGameWithComputer(UUID userId) {
        TwoPlayerWithComputerGame game = new TwoPlayerWithComputerGame();
        game.player1 = userId;
        game.board = new Board(SIZE, SIZE);
        return game;
    }

//    public Game startNewGame (String user1Id, String user2Id) {
//        TwoPlayerGame game = new TwoPlayerGame();
//        game.player1 = UUID.fromString(user1Id);
//        game.player2 = UUID.fromString(user2Id);
//        game.board = new Board(SIZE,SIZE);
//        return game;
//    }

    public void startGame(TwoPlayerWithComputerGame game) {
        game.status = Status.RUNNING;
        game.currentTurn = GameUtil.getRandomInt(1) == 1 ? game.player1 : game.player2;
    }

}

class MoveService {

    public void makeMove(TwoPlayerWithComputerGame game, Move move) {
        if (game.currentTurn.equals(GameUtil.getComputerPlayerId())) {
            makeComputerMove(game.currentTurn, game, move.value);
        } else {
            makeUserMove(game.currentTurn, game, move);
        }
        switchTurns(game);
    }

    private void switchTurns(TwoPlayerWithComputerGame game) {
        game.currentTurn = GameUtil.getComputerPlayerId().equals(game.currentTurn) ? game.player1 : game.player2;
    }


    public Move makeComputerMove(UUID userId, Game game, int value) {
        Move move = new DecisionService().decideMove(userId, game);
        System.out.println(String.format("Computer move x - %s and y - %s", move.position.x, move.position.y));
        move.userId = userId;
        game.board.board[move.position.x][move.position.y].value = value;
        game.board.board[move.position.x][move.position.y].playerID = userId;
        verifyGameState(userId, game);
        return move;
    }

    public Move makeUserMove(UUID userId, Game game, Move move) {
        GameUtil.isValidMove(game, move);
        move.userId = userId;
        game.board.board[move.position.x][move.position.y].value = move.value;
        game.moves.add(move);
        verifyGameState(userId, game);
        return move;
    }

    private void verifyGameState(UUID userId, Game game) {
        if (game.status == Status.END) {
            throw new RuntimeException("Game is over");
        }
        if (DecisionService.checkIfUserWins(userId, game)) {
            System.out.println("User - " + userId.toString() + "wins");
            game.status = Status.END;
            game.winner = userId.toString();
            throw new RuntimeException("Game is over");
        }
    }

}

class DecisionService {

    public static boolean checkIfUserWins(UUID userId, Game game) {
        // check column

        for (int i = 0; i < game.board.xsixe; i++) {
            int count = 0;
            for (int j = 0; j < game.board.ysize; j++) {
                if (Objects.nonNull(game.board.board[i][j].playerID) && game.board.board[i][j].playerID.equals(userId)) {
                    count++;
                } else {
                    break;
                }
            }
            if (count == 4) {
                return true;
            }
        }

        // check row
        for (int j = 0; j < game.board.ysize; j++) {
            int count = 0;
            for (int i = 0; i < game.board.xsixe; i++) {
                if (Objects.nonNull(game.board.board[i][j].playerID) && game.board.board[i][j].playerID.equals(userId)) {
                    count++;
                } else {
                    break;
                }
            }
            if (count == 4) {
                return true;
            }
        }
        // digonal
        var x = 0;
        var y = 0;
        int count = 0;
        for (int j = 0; j < game.board.ysize; j++) {
            if (Objects.nonNull(game.board.board[x + j][y + j].playerID) && game.board.board[x + j][y + j].playerID.equals(userId)) {
                count++;
            } else {
                break;
            }
        }
        if (count == 4) {
            return true;
        }
        return false;
    }

    public Move decideMove(UUID uuid, Game game) {
        int xsize = game.board.xsixe;
        int ysize = game.board.ysize;

        while (true) {
            int x = GameUtil.getRandomInt(xsize);
            int y = GameUtil.getRandomInt(ysize);

            if (game.board.board[x][y].value == -1) {
                Move move = new Move();
                Position position = new Position();
                position.x = x;
                position.y = y;
                move.position = position;
                return move;
            }
        }
    }
}


class GameUtil {

    private static Random random = new Random();
    private static UUID computerId = UUID.randomUUID();

    public static UUID getComputerPlayerId() {
        return computerId;
    }

    public static int getRandomInt(int range) {
        return random.nextInt(range);
    }

    public static boolean isValidMove(Game game, Move move) {
        var x = move.position.x;
        var y = move.position.y;
        if (0 <= x && y >= 0 && x < GameService.SIZE && y < GameService.SIZE && game.board.board[x][y].value == -1) {
            return true;
        }
        throw new RuntimeException("Invalid Move");
    }

}


/**
 * Step 1: Implement the basic Connect 4 game.
 * The game should allow two players to take turns dropping their pieces into a 7-column, 6-row grid.
 * The game should check for a win condition (four consecutive pieces horizontally, vertically, or diagonally) and declare a winner.
 * Step 2: Enhance the game with a basic AI.
 * Implement an AI that makes random valid moves.
 * The AI should play against a human player.
 **/