
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;


import javafx.application.Platform;
import javafx.scene.Scene;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

class Constants{
    public final static double widthScene = 700; // ширина
    public final static double heightScene = 600;//высота
    public final static double widthCurtain = widthScene;
    public final static double heightCurtain = 100;
    public final static double delta = 2;
    public final static int radius = 20;
}


class UpRectange{
    private final double width = Constants.widthCurtain; //ширина
    private final double height = Constants.heightCurtain;  //высота
    private Rectangle rectangle;

    public UpRectange(Rectangle rectangle){
        this.rectangle = rectangle;
        rectangle.setHeight(Constants.heightCurtain);
        rectangle.setWidth(Constants.widthCurtain);
        rectangle.setFill(Color.BLUE);
    }
    public Rectangle getRectangle(){return rectangle;}
    public double getWidth(){return width;}
    public double getHeight(){return height;}
}

class WrapperForCirce{
    Random rand = new Random();
    private volatile double posX;
    private volatile double posY;
    private volatile double speed = 1.01;
    private volatile double dx = (rand.nextBoolean() ? 1 : -1) * speed;
    private volatile double dy = (rand.nextBoolean() ? 1 : -1) * speed;
    private int Left = 0;
    private int Right = 0;
    private Circle circle;
    public Circle getCircle(){return circle;}
    public WrapperForCirce(Circle circle){
        this.circle = circle;
        this.posX = circle.getCenterX();
        this.posY = circle.getCenterY();
    }
    public  void isOver(WrapperForRectangle L, WrapperForRectangle R, Text Le, Text Ri){
        if (Left == 11 || Right == 11){
            L.setAbsolutePosY((Constants.heightScene - Constants.heightCurtain + WrapperForRectangle.height) / 2);
            R.setAbsolutePosY((Constants.heightScene - Constants.heightCurtain + WrapperForRectangle.height) / 2);
            resetPosition();
            Left = 0;
            Right = 0;
            Le.setText("0");
         Ri.setText("0");

        }
    }
    public void move(WrapperForRectangle L, WrapperForRectangle R, Text Le, Text Ri) {



        if (posY - Constants.radius <= Constants.heightCurtain) {
            dy = -dy;
            posY = Constants.heightCurtain + Constants.radius;
        }


        if (posY + Constants.radius >= Constants.heightScene) {
            dy = -dy;
            posY = Constants.heightScene - Constants.radius;
        }


        if (posX - Constants.radius <= WrapperForRectangle.width &&
                posY + Constants.radius >= L.getPosY() &&
                posY - Constants.radius <= L.getPosY() + WrapperForRectangle.height) {

            dx = -dx;
            posX = WrapperForRectangle.width + Constants.radius;
            speed += 0.02;

            dx = (dx > 0 ? speed : -speed);
            dy = (dy > 0 ? speed : -speed);
        }

        else if (posX + Constants.radius >= Constants.widthScene - WrapperForRectangle.width &&
                posY + Constants.radius >= R.getPosY() &&
                posY - Constants.radius <= R.getPosY() + WrapperForRectangle.height) {

            dx = -dx;
            posX = Constants.widthScene - WrapperForRectangle.width - Constants.radius;
            speed += 0.02;

            dx = (dx > 0 ? speed : -speed);
            dy = (dy > 0 ? speed : -speed);
        }

        else if (posX + Constants.radius >= Constants.widthScene - WrapperForRectangle.width) {
            Le.setText(((Integer)(++Left)).toString() );
            resetPosition();
            isOver(L, R, Le, Ri);
        }
        else if (posX - Constants.radius <= WrapperForRectangle.width) {
            Ri.setText(((Integer)(++Right)).toString() );
            resetPosition();
            isOver(L, R, Le, Ri);
        }


        posX += dx;
        posY += dy;
        circle.setCenterX(posX);
        circle.setCenterY(posY);
    }


    private void resetPosition() {
        posX = Constants.widthScene / 2;
        posY = (Constants.heightScene + Constants.heightCurtain) / 2;
        speed = 1;
        dx = (rand.nextBoolean() ? 1 : -1) * speed;
        dy = (rand.nextBoolean() ? 1 : -1) * speed;
        circle.setCenterX(posX);
        circle.setCenterY(posY);
    }
}

class WrapperForRectangle{
    public static final int width = 10; //ширина
    public static final int height = 100;  //высота
    private volatile double posX;
    private volatile double posY;
    private Rectangle rectangle;
    public void setAbsolutePosY(double absoluteY) {
        posY = absoluteY;
        // Проверка границ
        if(posY < Constants.heightCurtain) {
            posY = Constants.heightCurtain;
        }
        if(posY + height > Constants.heightScene) {
            posY = Constants.heightScene - height;
        }
        rectangle.setY(posY);
    }
    public void setPosY(double deltaY){
        if(posY + height + deltaY >= Constants.heightScene) {

            posY = Constants.heightScene - height;

        }else if(posY + deltaY  <= Constants.heightCurtain){
            posY = Constants.heightCurtain;
        }
        else{
            posY += deltaY;
        }

        rectangle.setY(posY);
    }
    public double getPosY(){return posY;}

    public Rectangle getRectangle(){return rectangle;}
    public WrapperForRectangle(Rectangle rectangle, double y, boolean right){
        this.rectangle = rectangle;

        posY = (Constants.heightScene - Constants.heightCurtain + height) / 2;
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        rectangle.setFill(Color.BLACK);
        rectangle.setY(posY);
        if (right){
            rectangle.setX(Constants.widthScene - width);
        }else {
            rectangle.setX(0);
        }
    }
}

public class Main extends Application {

    private static boolean wPressed = false;
    private static boolean sPressed = false;
    private static boolean upPressed = false;
    private static boolean downPressed = false;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        final double vScene = Constants.heightScene; // высота сцены
        final double v1Scene = Constants.widthScene;  //ширина сцены
        Pane root = new Pane();
        Pane realScore = new Pane();
        StackPane score = new StackPane();
        Text textL = new Text("0");
        Text textMiddle = new Text(":");
        Text textR = new Text("0");
        textMiddle.setFont(new Font(25));
        textR.setFont(new Font(25));
        textL.setFont(new Font(25));
        textMiddle.setY(Constants.heightCurtain / 2);
        textMiddle.setX(0.5 * Constants.widthScene);
        textR.setY(Constants.heightCurtain / 2);
        textR.setX(0.75 * Constants.widthScene);
        textL.setY(Constants.heightCurtain / 2);
        textL.setX(0.25 * Constants.widthScene);
        realScore.getChildren().addAll(textL,textMiddle ,textR);
        Scene scene = new Scene(root, v1Scene, vScene);
        stage.setScene(scene);
        stage.show();
        Circle circle = new Circle(Constants.radius, Color.GREEN);
        circle.setCenterX((Constants.widthScene + 3) / 2);
        circle.setCenterY((Constants.heightScene + WrapperForRectangle.height) / 2);
        WrapperForCirce wrapperForCirce = new WrapperForCirce(circle);
        Rectangle middle = new Rectangle(3, Constants.heightScene - Constants.heightCurtain);
        middle.setY(Constants.heightCurtain);
        middle.setX(Constants.widthScene / 2);
        WrapperForRectangle rectangleL = new WrapperForRectangle(new Rectangle(), vScene, false);
        WrapperForRectangle rectangleR = new WrapperForRectangle(new Rectangle(), vScene, true);
        UpRectange curtain = new UpRectange(new Rectangle());
        score.getChildren().addAll(curtain.getRectangle(), realScore);

        root.getChildren().addAll(rectangleL.getRectangle(),rectangleR.getRectangle(), score, middle, wrapperForCirce.getCircle());
        SetupGameControls(rectangleL, rectangleR, scene);
        AnimationTimer game = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (wPressed){
                    rectangleL.setPosY(-Constants.delta);
                }
                if (sPressed){
                    rectangleL.setPosY(Constants.delta);
                }
                if (upPressed){
                    rectangleR.setPosY(-Constants.delta);
                }
                if (downPressed){
                    rectangleR.setPosY(Constants.delta);
                }
                wrapperForCirce.move(rectangleL, rectangleR, textL, textR);
            }
        };
        game.start();


    }





    public static void SetupGameControls(WrapperForRectangle rectangleL,
                                         WrapperForRectangle rectangleR,
                                         Scene scene) {
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case W: wPressed = true; break;
                case S: sPressed = true; break;
                case UP: upPressed = true; break;
                case DOWN: downPressed = true; break;
            }
        });

        scene.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case W: wPressed = false; break;
                case S: sPressed = false; break;
                case UP: upPressed = false; break;
                case DOWN: downPressed = false; break;
            }
        });
    }
}

class Test{

}