import javax.swing.*;

public class GameFrame extends JFrame {

	public GameFrame() {

		this.setTitle("Flappy Bird Game");
		this.add(new GamePanel());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocation(550,100);
		this.pack();	
		this.setVisible(true);

		
		
	}
	
}
