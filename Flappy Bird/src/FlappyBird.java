import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    //Image
    Image backgroundImage;
    Image birdImage;
    Image bottomPipe;
    Image topPipe;

    //Bird
    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth  = 35;
    int birdHeight = 24;


    public class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        public Bird(Image img)
        {
            this.img = img;
        }
    }

    //Pipes
    int pipeX = boardHeight;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 400;

    public class Pipe
    {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        public Pipe(Image img)
        {
            this.img = img;
        }
    }
    ArrayList<Pipe> pipes;
    Random random = new Random();
    boolean gameOver = false;
    double score;
    int maxScore;

    Bird bird;
    Timer timer;
    Timer placePipeTimer;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;
   public FlappyBird()
   {
       setPreferredSize(new Dimension(boardWidth,boardHeight));
         setFocusable(true);
       addKeyListener(this);
       backgroundImage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
       birdImage = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
       bottomPipe = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
       topPipe = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        //bird
        bird = new Bird(birdImage);
        //pipes
        pipes = new ArrayList<Pipe>();
        //pipe timer
       placePipeTimer = new Timer(1500, new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               placePipes();
           }
       });
       placePipeTimer.start();
        //timer
       timer = new Timer(1000/60,this); //here this refers to action performed
       timer.start();
   }

   public void placePipes()
   {
       int randomPipeY = (int)(pipeY-pipeHeight/4-Math.random()*(pipeHeight/2));
       int openingSpace = boardHeight/4;
       //top
       Pipe topPipes = new Pipe(topPipe);
       topPipes.y = randomPipeY;
       pipes.add(topPipes);
       //bottom
       Pipe bottomPipes = new Pipe(bottomPipe);
       bottomPipes.y = topPipes.y + pipeHeight + openingSpace;
       pipes.add(bottomPipes);
   }

   public void paintComponent(Graphics g)
   {
       super.paintComponent(g);
       draw(g);
   }
   public void draw(Graphics g)
   {
       //background
       g.drawImage(backgroundImage,0,0,boardWidth,boardHeight,null);
       //bird
       g.drawImage(birdImage,bird.x,bird.y,bird.width,bird.height,null);
       //pipes
       for(int i=0;i<pipes.size();i++)
       {
           Pipe pipe = pipes.get(i);
           g.drawImage(topPipe,pipe.x,pipe.y,pipe.width,pipe.height,null);
       }
       //score
       g.setColor(Color.white);
       g.setFont(new Font("Arial",Font.PLAIN,32));
       if(gameOver)
       {
           g.setColor(Color.red);
          // g.setFont(new Font("Arial",Font.BOLD,45));
           g.drawString("Game Over: " + String.valueOf((int)score),70,boardHeight/2 );
           maxScore = (int)Math.max(score,maxScore);
           g.drawString("Highest Score: " + maxScore , 40,70);
       }
       else
           g.drawString("Score: " + String.valueOf((int)score),10,30);
   }
    public void move()
    {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y,0);
        for(int i=0;i<pipes.size();i++)
        {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;
            if(!pipe.passed && bird.x > pipe.x + pipe.width)
            {
                pipe.passed = true;
                score += 0.5;
            }
            if(collision(bird,pipe))
                gameOver = true;
        }
        if(bird.y>boardHeight)
        {
            gameOver = true;
        }
    }

    public boolean collision(Bird a , Pipe b)
    {
       return a.x<b.x +b.width &&
              a.x + a.width> b.x &&
               a.y<b.y +b.height &&
               a.y+a.height >b.y;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver) {
            placePipeTimer.stop();
              timer.stop();
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
       if(e.getKeyCode() == KeyEvent.VK_UP)
       {
           velocityY += -11;
       } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
           velocityY += 11;
       }
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            velocityY = -9;
            if(gameOver)
            {
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                timer.start();
                placePipeTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
   }
    @Override
    public void keyTyped(KeyEvent e) {
    }

}
