import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class TeacherReportFrame extends JFrame {

	public TeacherReportFrame(String teacherUsername, String subject) {
		setTitle("Teacher Report - " + teacherUsername + " (" + subject + ")");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		
		// Use default look and feel
		
		// Create main panel with gradient background
		JPanel mainPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// Create gradient background
				GradientPaint gradient = new GradientPaint(
					0, 0, new Color(52, 152, 219), 
					0, getHeight(), new Color(155, 89, 182)
				);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, getWidth(), getHeight());
				g2d.dispose();
			}
		};
		mainPanel.setLayout(new BorderLayout());
		
		// Create header panel
		JPanel headerPanel = new JPanel();
		headerPanel.setOpaque(false);
		headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		headerPanel.setLayout(new BorderLayout());
		
		JLabel titleLabel = new JLabel("Teacher Performance Report");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerPanel.add(titleLabel, BorderLayout.CENTER);
		
		JLabel subtitleLabel = new JLabel(teacherUsername + " - " + subject);
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		subtitleLabel.setForeground(new Color(240, 240, 240));
		subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		
		// Create content panel
		JPanel contentPanel = new JPanel();
		contentPanel.setOpaque(false);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
		contentPanel.setPreferredSize(new Dimension(1200, 1800)); // Much wider for better spacing
		
		// Get data from database
		double[] means = DB.getQuestionMeansForSubject(subject);
		
		// Create explicit curriculum section
		JPanel explicitPanel = createExplicitCurriculumReport(means);
		contentPanel.add(explicitPanel);
		contentPanel.add(Box.createVerticalStrut(20));
		
		// Create implicit curriculum section
		JPanel implicitPanel = createImplicitCurriculumReport(means);
		contentPanel.add(implicitPanel);
		contentPanel.add(Box.createVerticalStrut(20));
		
		// Create summary panel
		JPanel summaryPanel = createSummaryPanel(means);
		contentPanel.add(summaryPanel);
		
		// Add extra space at the bottom to ensure full scrolling
		contentPanel.add(Box.createVerticalStrut(100));
		
		// Force the content panel to recalculate its size
		contentPanel.revalidate();
		contentPanel.repaint();
		
		// Create scroll pane
		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0));
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		
		// Customize scroll bar appearance
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.getVerticalScrollBar().setBlockIncrement(50);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
		
		// Enable smooth scrolling
		scrollPane.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
		
		// Force the scroll pane to recalculate its preferred size
		scrollPane.getViewport().revalidate();
		scrollPane.revalidate();
		
		// Ensure proper scrolling to bottom
		scrollPane.getViewport().setViewPosition(new Point(0, 0));
		
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		
		// Create footer with logout button
		JPanel footerPanel = new JPanel();
		footerPanel.setOpaque(false);
		footerPanel.setLayout(new FlowLayout());
		footerPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
		
		JButton logoutButton = new JButton("Logout");
		logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		logoutButton.setPreferredSize(new Dimension(120, 40));
		logoutButton.setBackground(new Color(231, 76, 60));
		logoutButton.setForeground(Color.WHITE);
		logoutButton.setBorder(new LineBorder(new Color(192, 57, 43), 2));
		logoutButton.setFocusPainted(false);
		logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// Add hover effect
		logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				logoutButton.setBackground(new Color(192, 57, 43));
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				logoutButton.setBackground(new Color(231, 76, 60));
			}
		});
		
		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new LoginFrame().setVisible(true);
			}
		});
		
		footerPanel.add(logoutButton);
		mainPanel.add(footerPanel, BorderLayout.SOUTH);
		
		add(mainPanel);
		setSize(1300, 800); // Much wider window
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private JPanel createExplicitCurriculumReport(double[] means) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2, true),
			"Explicit Curriculum (Teaching Quality)",
			TitledBorder.LEFT,
			TitledBorder.TOP,
			new Font("Segoe UI", Font.BOLD, 18),
			Color.WHITE
		));
		
		String[] questions = {
			"Teacher is prepared for class",
			"Teacher knows His/Her Subject",
			"Teacher is Organised and neat",
			"Teacher Plans class time and Assignments",
			"Teacher is flexible",
			"Teacher is clear about assignments and test",
			"Teacher allows you to be active in the classroom",
			"Teacher manages the time well",
			"Teacher returns homework in a timely manner",
			"Teacher grades Fairly",
			"I have learned a lot from the teacher about this subject",
			"Teacher gives good feedback on projects",
			"Teacher is creative in developing lesson plans",
			"Teacher encourages students to speak up"
		};
		
		for (int i = 0; i < 14; i++) {
			JPanel questionPanel = new JPanel();
			questionPanel.setOpaque(false);
			questionPanel.setLayout(new BorderLayout());
			questionPanel.setBorder(new EmptyBorder(8, 15, 8, 15));
			
			JLabel questionLabel = new JLabel((i + 1) + ". " + questions[i]);
			questionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			questionLabel.setForeground(Color.WHITE);
			questionPanel.add(questionLabel, BorderLayout.WEST);
			
			// Create rating display with color coding
			double rating = means[i];
			Color ratingColor = getRatingColor(rating);
			JLabel ratingLabel = new JLabel(String.format("%.2f / 5.0", rating));
			ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
			ratingLabel.setForeground(ratingColor);
			ratingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			// Add a subtle background for better visibility
			ratingLabel.setOpaque(true);
			ratingLabel.setBackground(new Color(255, 255, 255, 30)); // Semi-transparent white background
			ratingLabel.setBorder(new javax.swing.border.EmptyBorder(4, 8, 4, 8));
			questionPanel.add(ratingLabel, BorderLayout.EAST);
			
			panel.add(questionPanel);
		}
		
		return panel;
	}
	
	private JPanel createImplicitCurriculumReport(double[] means) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2, true),
			"Implicit Curriculum (Personal Qualities)",
			TitledBorder.LEFT,
			TitledBorder.TOP,
			new Font("Segoe UI", Font.BOLD, 18),
			Color.WHITE
		));
		
		String[] questions = {
			"Teacher follows through on what he/she says",
			"Teacher listens student's point of view",
			"Teacher respects the opinions and decision of students",
			"Teacher accepts responsibility for his/her own mistakes",
			"Teacher is willing to learn from students",
			"Teacher is sensitive to the needs of students",
			"Teacher's words and actions match",
			"Teacher is fun to be with",
			"Teacher likes and respects students",
			"Teacher helps when you ask for help",
			"Teacher is consistent and fair in discipline",
			"I trust this teacher",
			"Teacher tries to model student's expectations",
			"Teacher is firm in discipline without being too strict"
		};
		
		for (int i = 0; i < 14; i++) {
			JPanel questionPanel = new JPanel();
			questionPanel.setOpaque(false);
			questionPanel.setLayout(new BorderLayout());
			questionPanel.setBorder(new EmptyBorder(8, 15, 8, 15));
			
			JLabel questionLabel = new JLabel((i + 15) + ". " + questions[i]);
			questionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			questionLabel.setForeground(Color.WHITE);
			questionPanel.add(questionLabel, BorderLayout.WEST);
			
			// Create rating display with color coding
			double rating = means[i + 14];
			Color ratingColor = getRatingColor(rating);
			JLabel ratingLabel = new JLabel(String.format("%.2f / 5.0", rating));
			ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
			ratingLabel.setForeground(ratingColor);
			ratingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			// Add a subtle background for better visibility
			ratingLabel.setOpaque(true);
			ratingLabel.setBackground(new Color(255, 255, 255, 30)); // Semi-transparent white background
			ratingLabel.setBorder(new javax.swing.border.EmptyBorder(4, 8, 4, 8));
			questionPanel.add(ratingLabel, BorderLayout.EAST);
			
			panel.add(questionPanel);
		}
		
		return panel;
	}
	
	private JPanel createSummaryPanel(double[] means) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridLayout(1, 3, 20, 0));
		panel.setBorder(BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2, true),
			"Performance Summary",
			TitledBorder.LEFT,
			TitledBorder.TOP,
			new Font("Segoe UI", Font.BOLD, 18),
			Color.WHITE
		));
		
		// Calculate averages
		double explicitAvg = 0;
		double implicitAvg = 0;
		double overallAvg = 0;
		
		for (int i = 0; i < 14; i++) {
			explicitAvg += means[i];
		}
		explicitAvg /= 14;
		
		for (int i = 14; i < 28; i++) {
			implicitAvg += means[i];
		}
		implicitAvg /= 14;
		
		overallAvg = (explicitAvg + implicitAvg) / 2;
		
		// Create summary cards
		JPanel explicitCard = createSummaryCard("Explicit Curriculum", explicitAvg, new Color(52, 152, 219));
		JPanel implicitCard = createSummaryCard("Implicit Curriculum", implicitAvg, new Color(155, 89, 182));
		JPanel overallCard = createSummaryCard("Overall Performance", overallAvg, new Color(46, 204, 113));
		
		panel.add(explicitCard);
		panel.add(implicitCard);
		panel.add(overallCard);
		
		return panel;
	}
	
	private JPanel createSummaryCard(String title, double rating, Color color) {
		JPanel card = new JPanel();
		card.setOpaque(false);
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBorder(new LineBorder(new Color(255, 255, 255, 50), 1, true));
		card.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		card.add(titleLabel);
		
		card.add(Box.createVerticalStrut(10));
		
		// Use bright colors based on rating value for better visibility
		Color ratingColor = getRatingColor(rating);
		JLabel ratingLabel = new JLabel(String.format("%.2f", rating));
		ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
		ratingLabel.setForeground(ratingColor);
		ratingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		// Add a subtle background for better visibility
		ratingLabel.setOpaque(true);
		ratingLabel.setBackground(new Color(255, 255, 255, 40)); // Semi-transparent white background
		ratingLabel.setBorder(new javax.swing.border.EmptyBorder(8, 16, 8, 16));
		card.add(ratingLabel);
		
		JLabel outOfLabel = new JLabel("out of 5.0");
		outOfLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		outOfLabel.setForeground(new Color(220, 220, 220)); // Brighter text
		outOfLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		card.add(outOfLabel);
		
		return card;
	}
	
	private Color getRatingColor(double rating) {
		if (rating >= 4.0) return new Color(0, 255, 0); // Bright Green
		else if (rating >= 3.0) return new Color(255, 255, 0); // Bright Yellow
		else if (rating >= 2.0) return new Color(255, 165, 0); // Bright Orange
		else return new Color(255, 0, 0); // Bright Red
	}
}


