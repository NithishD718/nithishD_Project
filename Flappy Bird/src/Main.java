import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
         int boardheight = 640;
         int boardWidth = 360;

         JFrame frame = new JFrame("Flappy Bird");
         frame.setSize(boardWidth,boardheight);
         frame.setResizable(false);
         frame.setLocationRelativeTo(null);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         FlappyBird flappyBird = new FlappyBird();
         frame.add(flappyBird);
         frame.pack();
         flappyBird.requestFocus();
         frame.setVisible(true);
    }
}