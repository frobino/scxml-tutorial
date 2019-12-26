package org.frobino.scxml.usecase;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.scxml.model.Transition;

public class AtmDisplay extends JFrame implements ActionListener {

	private static final long serialVersionUID = -5083315372455956151L;
	private AtmStatusFSM atmStatusFSM;

	private JButton button;
	private JLabel state;
	private JComboBox eventComboBox = new JComboBox();
	
	public static void main(String[] args) {
		new AtmDisplay();
	}
	
	public AtmDisplay() {
		super("ATM Display Demo");
		atmStatusFSM = new AtmStatusFSM();
		setupUI();
	}

	@SuppressWarnings("deprecation")
	private void setupUI() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		setContentPane(panel);
		// button = makeButton("FIRE_EVENT", AtmStatusEventEnum.getNamesAsCsv(), "Submit" );
		button = makeButton("FIRE_EVENT", "Submit" );
		panel.add(button, BorderLayout.CENTER);
		state = new JLabel(atmStatusFSM.getCurrentStateId());
		panel.add(state, BorderLayout.SOUTH);
		initEvents();
		panel.add(eventComboBox, BorderLayout.NORTH);
		pack();
		setLocation(200, 200);
		setResizable(false);
		setSize(300, 125);
		show();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}

	@SuppressWarnings("unchecked")
	private void initEvents() {
		eventComboBox.removeAllItems();
		// frobino: here we get all the "transations" for the current state
		List<Transition> transitionList = atmStatusFSM.getCurrentState().getTransitionsList();
		for (Transition transition : transitionList) {
			eventComboBox.addItem(transition.getEvent() );
		}
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("FIRE_EVENT")) {
			checkAndFireEvent();
		}
	}

	private boolean checkAndFireEvent() {
		// frobino: here is the logic were we trigger the FSM with an event
		// Thish should come from a event trace (such as a CTF event).
		atmStatusFSM.fireEvent(eventComboBox.getSelectedItem().toString());
		state.setText(atmStatusFSM.getCurrentStateId());
		initEvents();
		repaint();
		return true;
	}

	private JButton makeButton(final String actionCommand, final String altText) {
		JButton button = new JButton(altText);
		button.setActionCommand(actionCommand);
		// frobino:
		// button.setToolTipText(toolTipText);
		button.addActionListener(this);
		button.setOpaque(false);
		return button;
	}

}
