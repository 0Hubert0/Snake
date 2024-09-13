package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static final int WIDTHBOARD=25, HEIGHTBOARD=25, WIDTHSCENE=900, HEIGHTSCENE=900;
    public static int direction = 3, headX = 2, headY = 2, tailX = 2, tailY = 2, appleX, appleY;
    public static Timeline actualize;

    @Override
    public void start(Stage primaryStage) throws Exception {
        int[][] board = new int[WIDTHBOARD][HEIGHTBOARD];
        board[headY][headX] = direction;
        appleX = (int)(Math.random()*WIDTHBOARD);
        appleY = (int)(Math.random()*HEIGHTBOARD);
        board[appleX][appleY] = -1;

        AnchorPane root = new AnchorPane();

        Scene scene = new Scene(root, WIDTHSCENE, HEIGHTSCENE);

        Canvas canvas = new Canvas(scene.getWidth(), scene.getHeight());
        root.getChildren().add(canvas);
        GraphicsContext gr = canvas.getGraphicsContext2D();

        actualize = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            advance(board, gr);
            drawBoard(canvas, gr, board);
            //showBoard(board);
        }));

        actualize.setCycleCount(-1);
        actualize.play();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case UP:
                    direction = 1;
                    break;
                case RIGHT:
                    direction = 2;
                    break;
                case DOWN:
                    direction = 3;
                    break;
                case LEFT:
                    direction = 4;
                    break;
            }
        });

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public static void drawBoard(Canvas canvas, GraphicsContext gr, int[][] board){
        gr.setFill(Color.BLACK);
        gr.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        gr.setStroke(Color.BEIGE);
        gr.setFill(Color.GREEN);
        for (int i = 0; i <WIDTHBOARD; i++) {
            for (int j = 0; j <HEIGHTBOARD; j++) {
                gr.strokeRect(i*(WIDTHSCENE/WIDTHBOARD)+2, j*(HEIGHTSCENE/HEIGHTBOARD)+2,
                        WIDTHSCENE/WIDTHBOARD-4, HEIGHTSCENE/HEIGHTBOARD-4);
                if(board[j][i]>0) gr.fillRect(i*(WIDTHSCENE/WIDTHBOARD)+2, j*(HEIGHTSCENE/HEIGHTBOARD)+2,
                        WIDTHSCENE/WIDTHBOARD-4, HEIGHTSCENE/HEIGHTBOARD-4);
            }
        }
        gr.setFill(Color.RED);
        gr.fillRect(appleY*(WIDTHSCENE/WIDTHBOARD)+2, appleX*(HEIGHTSCENE/HEIGHTBOARD)+2,
                WIDTHSCENE/WIDTHBOARD-4, HEIGHTSCENE/HEIGHTBOARD-4);
    }

    public static void advance(int[][] board, GraphicsContext gr){
        boolean grow = false;
        board[headY][headX] = direction;
        switch(direction){
            case 1:
                headY--;
                break;
            case 2:
                headX++;
                break;
            case 3:
                headY++;
                break;
            case 4:
                headX--;
                break;
        }
        if(board[headY][headX]>0) gameOver(gr);
        if(board[headY][headX] == -1){
            grow = true;
            do{
                appleY = (int)(Math.random()*HEIGHTBOARD);
                appleX = (int)(Math.random()*WIDTHBOARD);
            }while(board[appleX][appleY] != 0);
            board[appleX][appleY] = -1;
        }
        board[headY][headX] = direction;
        if(!grow) {
            switch (board[tailY][tailX]) {
                case 1:
                    board[tailY][tailX] = 0;
                    tailY--;
                    break;
                case 2:
                    board[tailY][tailX] = 0;
                    tailX++;
                    break;
                case 3:
                    board[tailY][tailX] = 0;
                    tailY++;
                    break;
                case 4:
                    board[tailY][tailX] = 0;
                    tailX--;
                    break;
            }
        }
    }

    public static void gameOver(GraphicsContext gr){
        actualize.stop();
        gr.clearRect(0,0,1000, 1000);
    }

    public static void showBoard(int[][] board){
        System.out.println();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j <board[i].length; j++) {
                System.out.print("["+board[i][j]+"] ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
