/**
 * 
 */
package org.jfm.po;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import edu.asu.ser335.jfm.UsersSingleton;
import org.jfm.main.CommonConstants;
import org.jfm.main.LoginPannel;
import org.jfm.main.Salt;
import org.jfm.main.User;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.ser335.jfm.RolesSingleton;
import io.whitfin.siphash.SipHasher;

/**
 * @author Nikhil Hiremath
 *
 */
public class ChangePasswordPannel extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JLabel labelUsername = new JLabel("Enter username: ");
	private JLabel labelPassword = new JLabel("Enter password: ");
	private JLabel labelRole = new JLabel("Enter Role: ");
	private JLabel message;
	private JTextField textUsername = new JTextField(20);
	private JPasswordField fieldPassword = new JPasswordField(20);
	private JButton buttonChangePassword = new JButton("Submit");
	private JPanel newPanel;
	private JComboBox<String> roleList;
	private List<User> users;
	private List<Salt> salts;

	public ChangePasswordPannel() {
		// create a new panel with GridBagLayout manager
		newPanel = new JPanel(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);

		// add components to the panel
		// UserName
		constraints.gridx = 0;
		constraints.gridy = 0;
		newPanel.add(labelUsername, constraints);

		constraints.gridx = 1;
		newPanel.add(textUsername, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;

		// Password
		newPanel.add(labelPassword, constraints);

		constraints.gridx = 1;
		newPanel.add(fieldPassword, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;

		// Role
		newPanel.add(labelRole, constraints);
		constraints.gridx = 1;

		// drop down
		roleList = new JComboBox<String>(RolesSingleton.getRoleMapping().getDisplayRoles());

		// add to the parent container (e.g. a JFrame):
		newPanel.add(roleList, constraints);

		// System.out.println("Selected role: " + role);

		constraints.gridx = 0;
		constraints.gridy = 3;

		message = new JLabel();
		newPanel.add(message, constraints);
		constraints.gridx = 1;

		constraints.gridwidth = 3;
		constraints.anchor = GridBagConstraints.CENTER;
		newPanel.add(buttonChangePassword, constraints);

		// Adding the listeners to components..

		buttonChangePassword.addActionListener(this);

		// set border for the panel
		newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Admin Panel"));

		// add the panel to this frame
		add(newPanel);

		pack();
		setLocationRelativeTo(null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
/*
task H3
 */
		String userName = textUsername.getText();
		String password = String.valueOf(fieldPassword.getPassword());
		String role = (String) roleList.getSelectedItem();

		if(userName.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "error: userName field is empty.");
			//message.setText("error: userName field is empty.");
		} else if (password.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "error: password field is empty.");
			//message.setText("error: password field is empty.");
		} else if (role.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "error: role field is empty.");
			//message.setText("error: role field is empty.");
		} else {

			Map<String, String> users = null;
			try {
				users = UsersSingleton.getUserRoleMapping();
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			try{
				if(users.containsKey(userName) && users.containsValue(role)) {
					if(users.get(userName).equals(role)) {
						if(UsersSingleton.updatePasswordMapping(userName, password, role)) {
							JOptionPane.showMessageDialog(null, userName + "'s password updated successfully!'");
							dispose();
						} else {
							JOptionPane.showMessageDialog(null, "error updating password");

						}
					} else {
						JOptionPane.showMessageDialog(null, "error: user and role do not match.");
					}
				}
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(null, "Some error accessing file!!");
			}

		}
		// String userName = (String) roleList.getSelectedItem();


	}

}
