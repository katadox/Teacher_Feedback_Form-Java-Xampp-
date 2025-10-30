import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class StudentFeedbackFrame extends JFrame {
	private JTextField nameTextField;
	private JTextField rollNoTextField;
	private JComboBox<String> subjectCombo;
	private JComboBox<String>[] ratingComboBoxes;
	
	public StudentFeedbackFrame() {
		setTitle("Student Feedback Form");
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
					0, 0, new Color(135, 206, 250), 
					0, getHeight(), new Color(255, 182, 193)
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
		
		JLabel titleLabel = new JLabel("Teacher Feedback Form");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
		titleLabel.setForeground(new Color(50, 50, 50));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerPanel.add(titleLabel, BorderLayout.CENTER);
		
		JLabel subtitleLabel = new JLabel("Your feedback helps improve teaching quality");
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		subtitleLabel.setForeground(new Color(100, 100, 100));
		subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		
		// Create scrollable content panel
		JPanel contentPanel = new JPanel();
		contentPanel.setOpaque(false);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
		contentPanel.setPreferredSize(new Dimension(1200, 1500)); // Much wider for better spacing

		// Student Information Section
		JPanel studentInfoPanel = createStudentInfoPanel();
		contentPanel.add(studentInfoPanel);
		contentPanel.add(Box.createVerticalStrut(20));
		
		// Explicit Curriculum Section
		JPanel explicitPanel = createExplicitCurriculumPanel();
		contentPanel.add(explicitPanel);
		contentPanel.add(Box.createVerticalStrut(20));
		
		// Implicit Curriculum Section
		JPanel implicitPanel = createImplicitCurriculumPanel();
		contentPanel.add(implicitPanel);
		contentPanel.add(Box.createVerticalStrut(20));
		
		// Submit Button
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new FlowLayout());
		
		JButton submitButton = new JButton("Submit Feedback");
		submitButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
		submitButton.setPreferredSize(new Dimension(180, 50));
		submitButton.setBackground(new Color(46, 204, 113));
		submitButton.setForeground(Color.WHITE);
		submitButton.setBorder(new LineBorder(new Color(39, 174, 96), 2));
		submitButton.setFocusPainted(false);
		submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// Add hover effect
		submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				submitButton.setBackground(new Color(39, 174, 96));
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				submitButton.setBackground(new Color(46, 204, 113));
			}
		});
		
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = nameTextField.getText().trim();
				String roll = rollNoTextField.getText().trim();
				String subject = (String) subjectCombo.getSelectedItem();
				
				if (name.isEmpty() || roll.isEmpty()) {
					JOptionPane.showMessageDialog(StudentFeedbackFrame.this, "Name and Roll No are required", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				int[] vals = new int[28];
				for (int i = 0; i < 28; i++) {
					String s = (String) ratingComboBoxes[i].getSelectedItem();
					int score;
					if ("Strongly Agree".equals(s)) score = 5;
					else if ("Agree".equals(s)) score = 4;
					else if ("Neutral".equals(s)) score = 3;
					else if ("Disagree".equals(s)) score = 2;
					else score = 1;
					vals[i] = score;
				}
				
				DB.insertFeedback(name, roll, subject, vals);
				JOptionPane.showMessageDialog(StudentFeedbackFrame.this, "Thank you! Your feedback has been saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
				dispose();
				new LoginFrame().setVisible(true);
			}
		});

		buttonPanel.add(submitButton);
		contentPanel.add(buttonPanel);
		
		// Add extra space at the bottom to ensure full scrolling
		contentPanel.add(Box.createVerticalStrut(50));
		
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
		
		// Ensure proper scrolling to bottom
		scrollPane.getViewport().setViewPosition(new Point(0, 0));
		
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		add(mainPanel);
		
		setSize(1300, 700); // Much wider window
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private JPanel createStudentInfoPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true),
			"Student Information",
			TitledBorder.LEFT,
			TitledBorder.TOP,
			new Font("Segoe UI", Font.BOLD, 16),
			new Color(70, 70, 70)
		));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		// Name field
		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		nameLabel.setForeground(new Color(70, 70, 70));
		gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
		panel.add(nameLabel, gbc);
		
		nameTextField = new JTextField(40);
		nameTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		nameTextField.setPreferredSize(new Dimension(400, 35));
		nameTextField.setBorder(new LineBorder(new Color(200, 200, 200), 1));
		gbc.gridx = 1; gbc.gridy = 0;
		panel.add(nameTextField, gbc);
		
		// Roll No field
		JLabel rollLabel = new JLabel("Roll No:");
		rollLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		rollLabel.setForeground(new Color(70, 70, 70));
		gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
		panel.add(rollLabel, gbc);
		
		rollNoTextField = new JTextField(40);
		rollNoTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		rollNoTextField.setPreferredSize(new Dimension(400, 35));
		rollNoTextField.setBorder(new LineBorder(new Color(200, 200, 200), 1));
		gbc.gridx = 1; gbc.gridy = 1;
		panel.add(rollNoTextField, gbc);
		
		// Subject field
		JLabel subjectLabel = new JLabel("Subject:");
		subjectLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		subjectLabel.setForeground(new Color(70, 70, 70));
		gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
		panel.add(subjectLabel, gbc);
		
		subjectCombo = new JComboBox<>(new String[]{"TOC", "OOP", "MATHS", "DS", "DELD"});
		subjectCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		subjectCombo.setPreferredSize(new Dimension(400, 35));
		subjectCombo.setBorder(new LineBorder(new Color(200, 200, 200), 1));
		gbc.gridx = 1; gbc.gridy = 2;
		panel.add(subjectCombo, gbc);
		
		return panel;
	}
	
	private JPanel createExplicitCurriculumPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true),
			"Explicit Curriculum (Teaching Quality)",
			TitledBorder.LEFT,
			TitledBorder.TOP,
			new Font("Segoe UI", Font.BOLD, 16),
			new Color(70, 70, 70)
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
		
		@SuppressWarnings("unchecked")
		JComboBox<String>[] temp = new JComboBox[28];
		ratingComboBoxes = temp;
		String[] ratingOptions = {"Strongly Disagree", "Disagree", "Neutral", "Agree", "Strongly Agree"};
		
		for (int i = 0; i < 14; i++) {
			JPanel questionPanel = new JPanel();
			questionPanel.setOpaque(false);
			questionPanel.setLayout(new BorderLayout());
			questionPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
			
			JLabel questionLabel = new JLabel((i + 1) + ". " + questions[i]);
			questionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			questionLabel.setForeground(new Color(60, 60, 60));
			questionPanel.add(questionLabel, BorderLayout.WEST);
			
			ratingComboBoxes[i] = new JComboBox<>(ratingOptions);
			ratingComboBoxes[i].setFont(new Font("Segoe UI", Font.PLAIN, 12));
			ratingComboBoxes[i].setPreferredSize(new Dimension(250, 30));
			ratingComboBoxes[i].setBorder(new LineBorder(new Color(200, 200, 200), 1));
			questionPanel.add(ratingComboBoxes[i], BorderLayout.EAST);
			
			panel.add(questionPanel);
		}
		
		return panel;
	}
	
	private JPanel createImplicitCurriculumPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true),
			"Implicit Curriculum (Personal Qualities)",
			TitledBorder.LEFT,
			TitledBorder.TOP,
			new Font("Segoe UI", Font.BOLD, 16),
			new Color(70, 70, 70)
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
		
		String[] ratingOptions = {"Strongly Disagree", "Disagree", "Neutral", "Agree", "Strongly Agree"};
		
		for (int i = 0; i < 14; i++) {
			JPanel questionPanel = new JPanel();
			questionPanel.setOpaque(false);
			questionPanel.setLayout(new BorderLayout());
			questionPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
			
			JLabel questionLabel = new JLabel((i + 15) + ". " + questions[i]);
			questionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			questionLabel.setForeground(new Color(60, 60, 60));
			questionPanel.add(questionLabel, BorderLayout.WEST);
			
			ratingComboBoxes[i + 14] = new JComboBox<>(ratingOptions);
			ratingComboBoxes[i + 14].setFont(new Font("Segoe UI", Font.PLAIN, 12));
			ratingComboBoxes[i + 14].setPreferredSize(new Dimension(250, 30));
			ratingComboBoxes[i + 14].setBorder(new LineBorder(new Color(200, 200, 200), 1));
			questionPanel.add(ratingComboBoxes[i + 14], BorderLayout.EAST);
			
			panel.add(questionPanel);
		}
		
		return panel;
	}
}

