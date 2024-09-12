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
    public static int direction = 3;

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();

        Scene scene = new Scene(root, WIDTHSCENE, HEIGHTSCENE);

        Canvas canvas = new Canvas(scene.getWidth(), scene.getHeight());
        root.getChildren().add(canvas);
        GraphicsContext gr = canvas.getGraphicsContext2D();

        List<String> player = new ArrayList();
        player.add("2;2");

        Timeline actualize = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            drawBoard(canvas, gr, player);
            advance(player);
            System.out.println(direction);
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

    public static void drawBoard(Canvas canvas, GraphicsContext gr, List<String> player){
        gr.setFill(Color.BLACK);
        gr.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        gr.setStroke(Color.BEIGE);
        for (int i = 0; i <WIDTHBOARD; i++) {
            for (int j = 0; j <HEIGHTBOARD; j++) {
                gr.strokeRect(i*(WIDTHSCENE/WIDTHBOARD)+2, j*(HEIGHTSCENE/HEIGHTBOARD)+2,
                        WIDTHSCENE/WIDTHBOARD-4, HEIGHTSCENE/HEIGHTBOARD-4);
            }
        }
        gr.setFill(Color.GREEN);
        for (int i = 0; i < player.size(); i++) {
            String[] coordinates = player.get(i).split(";");
            int y = Integer.parseInt(coordinates[0]), x = Integer.parseInt(coordinates[1]);
            gr.fillRect(y*(WIDTHSCENE/WIDTHBOARD)+2, x*(HEIGHTSCENE/HEIGHTBOARD)+2,
                    WIDTHSCENE/WIDTHBOARD-4, HEIGHTSCENE/HEIGHTBOARD-4);
        }
    }

    public static void advance(List<String> player){
        String[] coordinates = player.get(0).split(";");
        int y = Integer.parseInt(coordinates[0]), x = Integer.parseInt(coordinates[1]);

        switch(direction){
            case 1:
                x--;
                break;
            case 2:
                y++;
                break;
            case 3:
                x++;
                break;
            case 4:
                y--;
                break;
        }

        String newCoordinates = y+";"+x;

        for (int i = player.size()-1; i>=0 ; i--) {
            if(i==0) player.set(0, newCoordinates);
            else player.set(i, player.get(i-1));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
