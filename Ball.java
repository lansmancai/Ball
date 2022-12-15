import java.util.Random;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
/**
 * Date:15/12/2022
 * @author jinniu
 * @version 1.0
 */
public class Ball
{
	//代表网球的位图
	private BufferedImage ball;
	// 桌面的宽度
	private final int TABLE_WIDTH = 300;
	// 桌面的高度
	private final int TABLE_HEIGHT = 400;
	// 球拍的垂直位置
	private final int RACKET_Y = 360;
	// 下面定义球拍的高度和宽度
	private final int RACKET_HEIGHT = 40;
	private final int RACKET_WIDTH = 180;
	// 小球的大小
	private final int BALL_SIZE = 30;
	private Frame f = new Frame("弹球游戏");
	Random rand = new Random();
	// 小球纵向的运行速度
	private int ySpeed = 10;
	// 返回一个-0.5~0.5的比率，用于控制小球的运行方向。
	private double xyRate = rand.nextDouble() - 0.5;
	// 小球横向的运行速度
	private int xSpeed = (int)(ySpeed * xyRate * 2);
	// ballX和ballY代表小球的坐标
	private int ballX = rand.nextInt(200) + 20;
	private int ballY = rand.nextInt(10) + 20;
	// racketX代表球拍的水平位置
	private int racketX = rand.nextInt(120);
	private MyCanvas tableArea = new MyCanvas();
	Timer timer;
	// 游戏是否结束的旗标
	private boolean isLose = false;
	// 游戏计分
	private int score = 0;
	
	public void init() throws Exception
	{
		ball = ImageIO.read(new File("ball.jpg"));
		// 设置桌面区域的最佳大小
		tableArea.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
		//窗口事件响应函数,可以点击右上角关闭游戏
		class MyListener extends WindowAdapter
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		}
		f.addWindowListener(new MyListener());
		f.add(tableArea);
		// 定义键盘监听器
		var keyProcessor = new KeyAdapter()
		{
			public void keyPressed(KeyEvent ke)
			{
				// 按下向左、向右键时，球拍水平坐标分别减少、增加
				if (ke.getKeyCode() == KeyEvent.VK_LEFT)
				{
					if (racketX > 0)
					racketX -= 10;
				}
				if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					if (racketX < TABLE_WIDTH - RACKET_WIDTH)
					racketX += 10;
				}
			}
		};
		// 为窗口和tableArea对象分别添加键盘监听器
		f.addKeyListener(keyProcessor);
		tableArea.addKeyListener(keyProcessor);
		// 定义每0.1秒执行一次的事件监听器。
		ActionListener taskPerformer = evt ->
		{
			// 如果小球碰到左边边框
			if (ballX <= 0 || ballX >= TABLE_WIDTH - 80)
			{
				xSpeed = -xSpeed;
			}
			// 如果小球高度超出了球拍位置，且横向不在球拍范围之内，游戏结束。
			if (ballY + 60 >= RACKET_Y &&
				(ballX + 80 < racketX || ballX > racketX + RACKET_WIDTH))
			{
				timer.stop();
				// 设置游戏是否结束的旗标为true。
				isLose = true;
				tableArea.repaint();
			}
			// 如果小球位于球拍之内，且到达球拍位置，小球反弹
			else if (ballY <= 0 ||(ballY + 60 >= RACKET_Y&& ballX + 80 > racketX && ballX <= racketX + RACKET_WIDTH))
			{
				ySpeed = -ySpeed;
			}
			// 小球坐标增加
			ballY += ySpeed;
			ballX += xSpeed;
			if (xSpeed >0)
			{
				score += xSpeed;
			}
			else
			{
				score -= xSpeed;
			}
			tableArea.repaint();
		};
		timer = new Timer(100, taskPerformer);
		timer.start();
		f.pack();
		f.setVisible(true);
	}
	public static void main(String[] args) throws Exception
	{
		new Ball().init();
	}
	class MyCanvas extends Canvas
	{
		// 重写Canvas的paint方法，实现绘画
		public void paint(Graphics g)
		{
			// 如果游戏已经结束
			if (isLose)
			{
				g.setColor(new Color(255, 0, 0));
				g.setFont(new Font("Times", Font.BOLD, 30));
				g.drawString("游戏结束,得分:" + score/10, 10, 200);
			}
			// 如果游戏还未结束
			else
			{
				// 绘制网球位图
				g.drawImage(ball, ballX, ballY, null);
				// 设置颜色，并绘制球拍
				g.setColor(new Color(255, 0, 0));
				g.fillRect(racketX, RACKET_Y, RACKET_WIDTH, RACKET_HEIGHT);
			}
		}
	}
}