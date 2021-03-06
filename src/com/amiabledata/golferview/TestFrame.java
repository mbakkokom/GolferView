package com.amiabledata.golferview;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class TestFrame extends JFrame {

	private JPanel contentPane;
	private JTable teamsTable;
	private JTable playersTable;
	
	private JScrollPane playersPane;
	
	JCheckBoxMenuItem menuViewShowDetails;
	
	private ArrayList<GolfTeam> teams = new ArrayList<GolfTeam>();

	private int lastTeamIndex=-1, lastPlayerIndex=-1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestFrame frame = new TestFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		setupMenuBar();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JScrollPane teamsPane = new JScrollPane();
		tabbedPane.addTab("Teams", null, teamsPane, null);
		
		teamsTable = new JTable();
		teamsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel selectionModel = teamsTable.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent e) {
		    	if (e.getValueIsAdjusting()) {
		    		return;
		    	}
		    	
		    	if (menuViewShowDetails.isSelected() != true) {
		    		lastTeamIndex = getTeamsTableSelectedIndex();
		    		setupPlayersTable();
		    	} else {
		    		((ListSelectionModel)teamsTable.getSelectionModel()).setAnchorSelectionIndex(lastTeamIndex);
		    	}
		    }
		});
		teamsPane.setViewportView(teamsTable);
		
		playersPane = new JScrollPane();
		tabbedPane.addTab("Players", null, playersPane, null);
		
		playersPane.setViewportView(new JLabel("Select a Team", JLabel.CENTER));
		
		//. end
		setupTeamsTable();
	}
	
	private void setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menuFile);
		
		// --
		JMenuItem menuFileNew = new JMenuItem("New");
		menuFileNew.setMnemonic('N');
		menuFileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.META_DOWN_MASK));
		menuFileNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				menuViewShowDetails.setSelected(false);
				teams.clear();
				setupTeamsTable();
				setupPlayersTable();
			}
			
		});
		menuFile.add(menuFileNew);
		
		menuFile.addSeparator();
		
		JMenuItem menuFileOpen = new JMenuItem("Open");
		menuFileOpen.setMnemonic('O');
		menuFileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.META_DOWN_MASK));
		menuFileOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);
				chooser.setFileFilter(new FileNameExtensionFilter("JSON File", "json"));
				if (chooser.showOpenDialog(getRootPane()) == JFileChooser.APPROVE_OPTION) {
					try {
						teams = GolferJsonFile.fromFile(chooser.getSelectedFile()).getTeams();
						setupTeamsTable();
						setupPlayersTable();
					} catch (FileNotFoundException e1) {
						JOptionPane.showMessageDialog(getRootPane(), "File not found!", "File open", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			
		});
		menuFile.add(menuFileOpen);
		
		JMenuItem menuFileSaveAs = new JMenuItem("Save As");
		menuFileSaveAs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);
				chooser.setFileFilter(new FileNameExtensionFilter("JSON File", "json"));
				if (chooser.showSaveDialog(getRootPane()) == JFileChooser.APPROVE_OPTION) {
					//teams = GolferJsonFile.fromFile(chooser.getSelectedFile()).getTeams();
					try {
						new GolferJsonFile(teams).toFile(chooser.getSelectedFile());
						JOptionPane.showMessageDialog(getRootPane(), "JSON successfully written to file!", "Save to file", JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(getRootPane(), e1.getCause(), "Failed to save file.", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			
		});
		menuFile.add(menuFileSaveAs);
		
		menuFile.addSeparator();
		
		JMenuItem menuFileExit = new JMenuItem("Exit");
		menuFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.META_DOWN_MASK));
		//menuFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
		menuFileExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});
		menuFile.add(menuFileExit);
		
		// --
		JMenu menuTeam = new JMenu("Team");
		menuTeam.setMnemonic('T');
		menuBar.add(menuTeam);
		
		JMenuItem menuTeamNew = new JMenuItem("New");
		menuTeamNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String ret = JOptionPane.showInputDialog(getRootPane(), "Name of the team:", "New Team", JOptionPane.QUESTION_MESSAGE);
				if (ret != null && ret.length() > 0) {
					teams.add(new GolfTeam(ret));
					setupTeamsTable();
				} else {
					JOptionPane.showMessageDialog(getRootPane(), "Team name cannot be empty!", "New Team", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		menuTeam.add(menuTeamNew);
		
		JMenuItem menuTeamRemove = new JMenuItem("Remove");
		menuTeamRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (lastTeamIndex >= 0) {
					teams.remove(lastTeamIndex);
					
					lastTeamIndex = -1;
					lastPlayerIndex = -1;
					
					setupTeamsTable();
					setupPlayersTable();
				} else {
					JOptionPane.showMessageDialog(getRootPane(), "You must select a team to remove!", "Remove Team", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		menuTeam.add(menuTeamRemove);
		
		// --
		JMenu menuPlayer = new JMenu("Player");
		menuPlayer.setMnemonic('l');
		menuBar.add(menuPlayer);
				
		JMenuItem menuPlayerNew = new JMenuItem("New");
		menuPlayerNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (lastTeamIndex == -1) {
					JOptionPane.showMessageDialog(getRootPane(), "You must select a team for the new player!", "New Player", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String ret = JOptionPane.showInputDialog(getRootPane(), "Name of the player:", "New Player", JOptionPane.QUESTION_MESSAGE);
				if (ret != null && ret.length() > 0) {
					teams.get(lastTeamIndex).newPlayer(ret);
					setupPlayersTable();
				} else {
					JOptionPane.showMessageDialog(getRootPane(), "Player name cannot be empty!", "New Player", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		menuPlayer.add(menuPlayerNew);
				
		JMenuItem menuPlayerRemove = new JMenuItem("Remove");
		menuPlayerRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (lastTeamIndex >= 0 && lastPlayerIndex >= 0) {
					teams.get(lastTeamIndex).removePlayer(lastPlayerIndex);
					
					lastPlayerIndex = -1;
					
					setupPlayersTable();
				} else {
					JOptionPane.showMessageDialog(getRootPane(), "You must select a team then a player to remove!", "Remove Player", JOptionPane.ERROR_MESSAGE);
				}
			}
			
		});
		menuPlayer.add(menuPlayerRemove);
		
		// --
		JMenu menuView = new JMenu("View");
		menuView.setMnemonic('V');
		menuBar.add(menuView);
		
		menuViewShowDetails = new JCheckBoxMenuItem("Show details");
		menuViewShowDetails.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.META_DOWN_MASK));
		menuViewShowDetails.setSelected(false);
		menuViewShowDetails.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int nw = getTeamsTableSelectedIndex();
				if (((JCheckBoxMenuItem) e.getSource()).isSelected() == false && nw != lastTeamIndex) {
					lastTeamIndex = nw;
					lastPlayerIndex = -1;
				}
				setupPlayersTable();
			}
			
		});
		menuView.add(menuViewShowDetails);
		
		setJMenuBar(menuBar);
	}
	
	protected int getTeamsTableSelectedIndex() {
		final DefaultListSelectionModel target = (DefaultListSelectionModel) teamsTable.getSelectionModel();
        //return target.getAnchorSelectionIndex();
		return target.getLeadSelectionIndex();
	}
	
	protected int getPlayersTableSelectedIndex() {
		if (playersTable == null) {
			return -1;
		}
		final DefaultListSelectionModel target = (DefaultListSelectionModel) playersTable.getSelectionModel();
        return target.getAnchorSelectionIndex();
	}
	
	protected void setupTeamsTable() {
		Vector<String> header = new Vector<String>();
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		
		int index = lastTeamIndex;
		
		header.add("Team Name");
		header.add("R1");
		header.add("R2");
		header.add("R3");
		header.add("Total");
		
		for (Iterator<GolfTeam> it=this.teams.iterator(); it.hasNext(); ) {
			Vector<String> t = new Vector<String>();
			GolfTeam team = it.next();
			
			t.add(team.getName());
			t.add("" + team.getBoardTotalScore(0));
			t.add("" + team.getBoardTotalScore(1));
			t.add("" + team.getBoardTotalScore(2));
			t.add("" + team.getBoardTotalScore());
			
			data.add(t);
		}
		
		teamsTable.setModel(new TeamsTableModel(data, header));
		if (index >= 0) {
			DefaultListSelectionModel sel = (DefaultListSelectionModel) teamsTable.getSelectionModel();
			sel.setAnchorSelectionIndex(index);
			sel.setLeadSelectionIndex(index);
		}
	}
	
	protected void setupPlayersTable() {
		int index = lastTeamIndex;
		int playerIndex = lastPlayerIndex;
		
		System.out.println("Need detailed player info: " + menuViewShowDetails.isSelected());
		System.out.println("Team index: " + index);
		System.out.println("Player index: " + playerIndex);
        
        if (index < 0) {
        	playersTable = null;
        	playersPane.setViewportView(new JLabel("Select a Team", JLabel.CENTER));
        	return;
        }
        
        Vector<String> header = new Vector<String>();
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		
		ArrayList<GolfPlayer> players = this.teams.get(index).getPlayers();
		
		if (menuViewShowDetails.isSelected() == false) {
			setTitle("Team: " + this.teams.get(index).getName());
			
			header.add("Player Name");
			header.add("R1");
			header.add("R2");
			header.add("R3");
			header.add("Total");
			
			for (Iterator<GolfPlayer> it=players.iterator(); it.hasNext(); ) {
				Vector<String> t = new Vector<String>();
				GolfPlayer player = it.next();
				
				t.add(player.getName());
				t.add("" + player.getTotalScore(0));
				t.add("" + player.getTotalScore(1));
				t.add("" + player.getTotalScore(2));
				t.add("" + player.getTotalScore());
				
				data.add(t);
			}
			
			playersTable = new JTable(new PlayersTableModel(data, header));
			playersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			ListSelectionModel selectionModel = playersTable.getSelectionModel();
			selectionModel.addListSelectionListener(new ListSelectionListener() {
			    public void valueChanged(ListSelectionEvent e) {
			    	if (e.getValueIsAdjusting()) {
			    		return;
			    	}
			    	
			    	lastPlayerIndex = getPlayersTableSelectedIndex();
			    }
			});
			playersPane.setViewportView(playersTable);
		} else if (playerIndex >= 0) {
			GolfTeam team = this.teams.get(index);
			GolfPlayer player = team.getPlayer(playerIndex);
			
			setTitle("\"" + player.getName() + "\" from \"" + team.getName() + "\"");
			
			header.add("Round No.");
			for (int i=1; i<=18; i++) {
				header.add(""+i);
			}
			header.add("Total");
			
			for (int r=0; r<3; r++) {
				Vector<String> t = new Vector<String>();
				
				t.add("" + (r+1));
				
				for (int i=0; i<18; i++) {
					t.add("" + player.getScore(r, i));
				}
				
				t.add("" + player.getTotalScore(r));
				
				data.add(t);
			}
			
			playersPane.setViewportView(new JTable(new PlayersTableModel(data, header)));
		} else {
			playersTable = null;
			playersPane.setViewportView(new JLabel("Select a Player", JLabel.CENTER));
			return;
		}
	}
	
	public void addTeam(GolfTeam team) {
		this.teams.add(team);
		setupTeamsTable();
	}
	
	/*
	 * Private class for teams table model.
	 */
	private class TeamsTableModel extends DefaultTableModel {
		
		TeamsTableModel(Vector<?> data, Vector<?> header) {
			super(data, header);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			String str = new String((String) aValue);
			
			if (str.length() == 0) {
				JOptionPane.showMessageDialog(getRootPane(), "Value cannot be empty.", "Editing error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (columnIndex != 0) {
				JOptionPane.showMessageDialog(getRootPane(), "Round total scores cannot be directly edited!", "No editing allowed", JOptionPane.ERROR_MESSAGE);
			} else {
				teams.get(rowIndex).setName(str);
				setupTeamsTable();
			}
		}
		
	}
	
	/*
	 * Private class for players table model.
	 */
	private class PlayersTableModel extends DefaultTableModel {
		
		PlayersTableModel(Vector<?> data, Vector<?> header) {
			super(data, header);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			int index = lastTeamIndex;
			String str = new String((String) aValue);
			
			if (str.length() == 0) {
				JOptionPane.showMessageDialog(getRootPane(), "Value cannot be empty.", "Editing error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (menuViewShowDetails.isSelected() == false) {
				if (columnIndex != 0) {
					JOptionPane.showMessageDialog(getRootPane(), "Round total scores cannot be directly edited!", "No editing allowed", JOptionPane.ERROR_MESSAGE);
				} else if (index >= 0) {
					teams.get(index).getPlayer(rowIndex).setName(str);
					setupPlayersTable();
				}
			} else {
				if (rowIndex >= 0 && rowIndex <= 2 && columnIndex >= 1 && columnIndex <= 18) {
					try {
						teams.get(index)
							 .getPlayer(getPlayersTableSelectedIndex()).setScore(rowIndex, columnIndex-1, Integer.parseInt(str));
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(getRootPane(), "Invalid integer entered for score.", "Editing error!", JOptionPane.ERROR_MESSAGE);
					}
					setupPlayersTable();
					setupTeamsTable();
				}
			}
		}
		
	}

}
