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
	//���������λͼ
	private BufferedImage ball;
	// ����Ŀ��
	private final int TABLE_WIDTH = 300;
	// ����ĸ߶�
	private final int TABLE_HEIGHT = 400;
	// ���ĵĴ�ֱλ��
	private final int RACKET_Y = 360;
	// ���涨�����ĵĸ߶ȺͿ��
	private final int RACKET_HEIGHT = 40;
	private final int RACKET_WIDTH = 180;
	// С��Ĵ�С
	private final int BALL_SIZE = 30;
	private Frame f = new Frame("������Ϸ");
	Random rand = new Random();
	// С������������ٶ�
	private int ySpeed = 10;
	// ����һ��-0.5~0.5�ı��ʣ����ڿ���С������з���
	private double xyRate = rand.nextDouble() - 0.5;
	// С�����������ٶ�
	private int xSpeed = (int)(ySpeed * xyRate * 2);
	// ballX��ballY����С�������
	private int ballX = rand.nextInt(200) + 20;
	private int ballY = rand.nextInt(10) + 20;
	// racketX�������ĵ�ˮƽλ��
	private int racketX = rand.nextInt(120);
	private MyCanvas tableArea = new MyCanvas();
	Timer timer;
	// ��Ϸ�Ƿ���������
	private boolean isLose = false;
	// ��Ϸ�Ʒ�
	private int score = 0;
	
	public void init() throws Exception
	{
		ball = ImageIO.read(new File("ball.jpg"));
		// ���������������Ѵ�С
		tableArea.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
		//�����¼���Ӧ����,���Ե�����Ͻǹر���Ϸ
		class MyListener extends WindowAdapter
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		}
		f.addWindowListener(new MyListener());
		f.add(tableArea);
		// ������̼�����
		var keyProcessor = new KeyAdapter()
		{
			public void keyPressed(KeyEvent ke)
			{
				// �����������Ҽ�ʱ������ˮƽ����ֱ���١�����
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
		// Ϊ���ں�tableArea����ֱ���Ӽ��̼�����
		f.addKeyListener(keyProcessor);
		tableArea.addKeyListener(keyProcessor);
		// ����ÿ0.1��ִ��һ�ε��¼���������
		ActionListener taskPerformer = evt ->
		{
			// ���С��������߱߿�
			if (ballX <= 0 || ballX >= TABLE_WIDTH - 80)
			{
				xSpeed = -xSpeed;
			}
			// ���С��߶ȳ���������λ�ã��Һ��������ķ�Χ֮�ڣ���Ϸ������
			if (ballY + 60 >= RACKET_Y &&
				(ballX + 80 < racketX || ballX > racketX + RACKET_WIDTH))
			{
				timer.stop();
				// ������Ϸ�Ƿ���������Ϊtrue��
				isLose = true;
				tableArea.repaint();
			}
			// ���С��λ������֮�ڣ��ҵ�������λ�ã�С�򷴵�
			else if (ballY <= 0 ||(ballY + 60 >= RACKET_Y&& ballX + 80 > racketX && ballX <= racketX + RACKET_WIDTH))
			{
				ySpeed = -ySpeed;
			}
			// С����������
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
		// ��дCanvas��paint������ʵ�ֻ滭
		public void paint(Graphics g)
		{
			// �����Ϸ�Ѿ�����
			if (isLose)
			{
				g.setColor(new Color(255, 0, 0));
				g.setFont(new Font("Times", Font.BOLD, 30));
				g.drawString("��Ϸ����,�÷�:" + score/10, 10, 200);
			}
			// �����Ϸ��δ����
			else
			{
				// ��������λͼ
				g.drawImage(ball, ballX, ballY, null);
				// ������ɫ������������
				g.setColor(new Color(255, 0, 0));
				g.fillRect(racketX, RACKET_Y, RACKET_WIDTH, RACKET_HEIGHT);
			}
		}
	}
}