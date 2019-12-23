import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import sweeper.Box;
import sweeper.Coord;
import sweeper.Game;
import sweeper.Ranges;


public class JavaSweeper extends JFrame
{
    private Game game;
    private JPanel panel;
    private JLabel label;
    private final int COLS=9, ROWS=9, BOMBS = 10, IMAGE_SIZE = 50;

    public static void main(String[] args)
    {
        new JavaSweeper();
    }

    private JavaSweeper()
    {
        game = new Game(COLS, ROWS, BOMBS);
        game.start();
        setImages();
        initLabel();
        initPanel();
        initFrame();
    }

    private void initLabel()
    {
        label = new JLabel("Welcome!");
        add (label, BorderLayout.SOUTH);
    }

    private void initPanel()
    {
        panel = new JPanel() //paint component
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                for (Coord coord: Ranges.getAllCoords())
                {
                    g.drawImage((Image) game.getBox(coord).image,coord.x*IMAGE_SIZE,coord.y*IMAGE_SIZE,this);
                }
                //paint on form
            }
        };

        panel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                int x = e.getX() / IMAGE_SIZE;
                int y = e.getY() / IMAGE_SIZE;
                Coord coord = new Coord(x,y);
                if (e.getButton() == MouseEvent.BUTTON1)
                    game.pressLeftButton(coord);
                if (e.getButton() == MouseEvent.BUTTON2)
                    game.start();
                if (e.getButton() == MouseEvent.BUTTON3)
                    game.pressRightButton(coord);
                
                label.setText(getMessage());
                panel.repaint();
            }
        });


        panel.setPreferredSize(new Dimension(Ranges.getSize().x*IMAGE_SIZE,
                                            Ranges.getSize().y*IMAGE_SIZE));        //dimension throw java.awt
        add(panel);
    }

    private void initFrame()
    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);     //auto close on exit
        setTitle("Sweeper");
        setResizable(false);
        setVisible(true);
        pack();     //minimum contain frame
        setLocationRelativeTo(null);        // frame center
        setIconImage(getImage("icon"));
    }

    private void setImages()
    {
        for (Box box: Box.values())
            box.image = getImage(box.name().toLowerCase());
    }

    private Image getImage (String name)
    {
        String filename = "img/" + name + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }

    public String getMessage()
    {
        switch (game.getState())
        {
            case PLAYED: return "Think else";
            case BOMBED: return "You Lose! Try again";
            case WINNER: return "Congratulations!!!";
            default:     return "Welcome";
        }
    }
}
