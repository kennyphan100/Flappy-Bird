import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, MouseListener, KeyListener {

	private final int WIDTH = 800;
	private final int HEIGHT = 800;
	private final int DELAY = 50;
	private boolean started = false;
	private boolean gameOver = false;
	private int ticks;
	private int yMotion;
	private int score = 0;
	private int highScore = 0;
	private Rectangle bird;
	private ArrayList<Rectangle> columns;
	Timer timer;
	Random random;
	
	public GamePanel() {
		random = new Random();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		addMouseListener(this);
		addKeyListener(this);
		startGame();
	}
	
	public void startGame() {
		timer = new Timer(DELAY, this);
		bird = new Rectangle(WIDTH/2 - 10, HEIGHT/2 - 10, 20, 20);
		columns = new ArrayList<Rectangle>();
		
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		//Blue Background
		g.setColor(new Color(54, 186, 186));
		g.fillRect(0,0,WIDTH,HEIGHT);		
		
		//Bird
		g.setColor(Color.red);
		g.fillOval(bird.x, bird.y, bird.width, bird.height);
		
		//Ground
		g.setColor(new Color(184, 142, 51));
		g.fillRect(0, HEIGHT-120, WIDTH, 120);

		//Grass
		g.setColor(new Color(49, 196, 49));
		g.fillRect(0, HEIGHT-120, WIDTH, 20);
		
		for(Rectangle column : columns) {
			paintColumn(g, column);
		}
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 100));
		
		if (!started) {
			g.drawString("Click to start!", 75, HEIGHT/2 - 40);
		}
		
		if (!gameOver && started) {
			g.drawString(String.valueOf(score), WIDTH/2 - 25, 100);
		}
		
		if (gameOver) {
			g.setColor(Color.red);
			g.drawString("Game Over!", 130, HEIGHT/2);
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 50));
			g.drawString("High Score: " + highScore, 220, HEIGHT/2 + 100);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString("Click to Restart Game", 180, HEIGHT/2 + 200);
			
		}
		
	}
	
	public void paintColumn (Graphics g, Rectangle column) {
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}
	
	public void addColumn(boolean start) {
		int space = 300;
		int width = 100;
		int height = 50 + random.nextInt(300);
		
		if (start) {
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size()-1) *300, 0, width, HEIGHT - height - space));
		} else {
			columns.add(new Rectangle(columns.get(columns.size()-1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size()-1).x, 0, width, HEIGHT - height - space));
		}
		
	}

	// -----ACTION LISTENER-----
	@Override
	public void actionPerformed(ActionEvent e) {
		
		int speed = 15;
		ticks++;
		
		if (started) {
			for(int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);
				column.x -= speed;
			}
			
			if (ticks % 2 == 0 && yMotion < 15) {
				yMotion += 2;
			}
			
			for(int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);
				if (column.x + column.width < 0) {
					columns.remove(column);
					if (column.y == 0) {
						addColumn(false);
					}
	
				}
			}
	
			bird.y += yMotion;
			
			for (Rectangle column : columns) {
				
				if (column.y == 0 && bird.x + bird.width/2 > column.x + column.width/2 - 10 && bird.x + bird.width/2 < column.x + column.width/2 + 10) {
					if (!gameOver) {
						score++;
					}
				}
				
				if (score > highScore) {
					highScore = score;
				}
				
				if (column.intersects(bird)) {
					gameOver = true;
					
					if (bird.x <= column.x) {
						bird.x = column.x - bird.width;
					} else {
						if (column.y != 0) {
							bird.y  = column.y - bird.height;
						} else if (bird.y < column.height) {
							bird.y = column.height;
						}
					}
					
				} 
			}
			
			if (bird.y > HEIGHT - 140 || bird.y <= 0) {
				gameOver = true;
			}
			
			if (bird.y + yMotion >= HEIGHT - 120) {
				bird.y = HEIGHT - 120 - bird.height;
			}
			
			repaint();
		}	
		
	}
	
	public void jump() {
		
		if (gameOver) {
			bird = new Rectangle(WIDTH/2 - 10, HEIGHT/2 - 10, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;
			
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			
			gameOver = false;
		}
		
		if (!started) {
			started = true;
		} else if (!gameOver){
			if (yMotion > 0) {
				yMotion = 0;
			}
			yMotion -= 10;
			
		}
		
	}
	
	//-----MOUSE LISTENER------
	@Override
	public void mouseClicked(MouseEvent e) {
		jump();
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	//-----KEY LISTENER------
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
		}
		
	}
	

	
}
