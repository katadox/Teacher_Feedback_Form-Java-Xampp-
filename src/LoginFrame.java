import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class LoginFrame extends JFrame {
	private final JTextField usernameField = new JTextField(20);
	private final JPasswordField passwordField = new JPasswordField(20);
	private final JComboBox<String> roleCombo = new JComboBox<>(new String[] {"STUDENT", "TEACHER"});

	public LoginFrame() {
		setTitle("Teacher Feedback System - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
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
					0, 0, new Color(74, 144, 226), 
					0, getHeight(), new Color(80, 200, 120)
				);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, getWidth(), getHeight());
				g2d.dispose();
			}
		};
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(500, 400));
		
		// Create content panel
		JPanel contentPanel = new JPanel();
		contentPanel.setOpaque(false);
		contentPanel.setLayout(new GridBagLayout());
		contentPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		// Title
		JLabel titleLabel = new JLabel("Teacher Feedback System");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 30, 0);
		contentPanel.add(titleLabel, gbc);
		
		// Subtitle
		JLabel subtitleLabel = new JLabel("Please login to continue");
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		subtitleLabel.setForeground(new Color(240, 240, 240));
		subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 40, 0);
		contentPanel.add(subtitleLabel, gbc);
		
		// Role selector
		JLabel roleLabel = new JLabel("Role:");
		roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		roleLabel.setForeground(Color.WHITE);
		gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
		gbc.insets = new Insets(15, 15, 15, 15);
		gbc.anchor = GridBagConstraints.WEST;
		contentPanel.add(roleLabel, gbc);
		
		roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		roleCombo.setPreferredSize(new Dimension(200, 35));
		roleCombo.setBackground(Color.WHITE);
		roleCombo.setBorder(new LineBorder(new Color(200, 200, 200), 1));
		gbc.gridx = 1; gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		contentPanel.add(roleCombo, gbc);
		
		// Username field
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		usernameLabel.setForeground(Color.WHITE);
		gbc.gridx = 0; gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		contentPanel.add(usernameLabel, gbc);
		
		usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		usernameField.setPreferredSize(new Dimension(200, 35));
		usernameField.setBorder(new LineBorder(new Color(200, 200, 200), 1));
		usernameField.setBackground(Color.WHITE);
		gbc.gridx = 1; gbc.gridy = 3;
		contentPanel.add(usernameField, gbc);
		
		// Password field
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		passwordLabel.setForeground(Color.WHITE);
		gbc.gridx = 0; gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.WEST;
		contentPanel.add(passwordLabel, gbc);
		
		passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		passwordField.setPreferredSize(new Dimension(200, 35));
		passwordField.setBorder(new LineBorder(new Color(200, 200, 200), 1));
		passwordField.setBackground(Color.WHITE);
		gbc.gridx = 1; gbc.gridy = 4;
		contentPanel.add(passwordField, gbc);
		
		// Login button
		JButton loginBtn = new JButton("Login");
		loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
		loginBtn.setPreferredSize(new Dimension(120, 45));
		loginBtn.setBackground(new Color(52, 152, 219));
		loginBtn.setForeground(Color.WHITE);
		loginBtn.setBorder(new LineBorder(new Color(41, 128, 185), 2));
		loginBtn.setFocusPainted(false);
		loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// Add hover effect
		loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				loginBtn.setBackground(new Color(41, 128, 185));
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				loginBtn.setBackground(new Color(52, 152, 219));
			}
		});
		
		gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
		gbc.insets = new Insets(30, 0, 0, 0);
		gbc.anchor = GridBagConstraints.CENTER;
		contentPanel.add(loginBtn, gbc);
		
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		add(mainPanel);
		
		pack();
		setLocationRelativeTo(null);

		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String role = (String) roleCombo.getSelectedItem();
				String username = usernameField.getText().trim();
				String password = new String(passwordField.getPassword());

				if (username.isEmpty()) {
					JOptionPane.showMessageDialog(LoginFrame.this, "Please enter a username", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if ("STUDENT".equals(role)) {
					// Any student username/password is accepted for now
					dispose();
					new StudentFeedbackFrame().setVisible(true);
					return;
				}

				boolean ok = DB.authenticateTeacher(username, password);
				if (!ok) {
					JOptionPane.showMessageDialog(LoginFrame.this, "Invalid teacher credentials", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String subject = DB.getTeacherSubject(username);
				dispose();
				new TeacherReportFrame(username, subject).setVisible(true);
			}
		});
	}
}


