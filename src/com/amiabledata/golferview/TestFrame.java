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
import javax.swing.JFileChooser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class TestFrame extends JFrame {

	private JPanel contentPane;
	private JTable teamsTable;
	
	private JScrollPane playersPane;
	private JLabel playersDefaultLabel;
	
	private ArrayList<GolfTeam> teams = new ArrayList<GolfTeam>();

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
		    	
	            setupPlayersTable();
		    }
		});
		teamsPane.setViewportView(teamsTable);
		
		playersPane = new JScrollPane();
		tabbedPane.addTab("Players", null, playersPane, null);
		
		playersDefaultLabel = new JLabel("Select a Team");
		playersDefaultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		playersDefaultLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		playersPane.setViewportView(playersDefaultLabel);
		
		//. end
		setupTeamsTable();
	}
	
	private void setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menuFile);
		
		JMenuItem menuFileNew = new JMenuItem("New");
		menuFileNew.setMnemonic('N');
		menuFileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.META_DOWN_MASK));
		menuFileNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
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
		
		setJMenuBar(menuBar);
	}
	
	protected int getTeamsTableSelectedIndex() {
		final DefaultListSelectionModel target = (DefaultListSelectionModel) teamsTable.getSelectionModel();
        return target.getAnchorSelectionIndex();
	}
	
	protected void setupTeamsTable() {
		Vector<String> header = new Vector<String>();
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		
		header.add("Team Name");
		header.add("R1");
		header.add("R2");
		header.add("R3");
		header.add("Total");
		
		for (Iterator<GolfTeam> it=this.teams.iterator(); it.hasNext(); ) {
			Vector<String> t = new Vector<String>();
			GolfTeam team = it.next();
			
			t.add(team.getName());
			t.add("" + team.getTotalScore(0));
			t.add("" + team.getTotalScore(1));
			t.add("" + team.getTotalScore(2));
			t.add("" + team.getTotalScore());
			
			data.add(t);
		}
		
		teamsTable.setModel(new TeamsTableModel(data, header));
	}
	
	protected void setupPlayersTable() {
		int index = getTeamsTableSelectedIndex();
        
        if (index < 0) {
        	playersPane.setViewportView(playersDefaultLabel);
        	return;
        }
        
        ArrayList<GolfPlayer> players = this.teams.get(index).getPlayers();
        
        Vector<String> header = new Vector<String>();
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		
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
		
		playersPane.setViewportView(new JTable(new PlayersTableModel(data, header)));
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
			if (columnIndex != 0) {
				JOptionPane.showMessageDialog(getRootPane(), "Round total scores cannot be directly edited!", "No editing allowed", JOptionPane.ERROR_MESSAGE);
			} else {
				teams.get(rowIndex).setName(new String((String) aValue));
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
			int index = getTeamsTableSelectedIndex();
			
			if (columnIndex != 0) {
				JOptionPane.showMessageDialog(getRootPane(), "Round total scores cannot be directly edited!", "No editing allowed", JOptionPane.ERROR_MESSAGE);
			} else if (index >= 0) {
				teams.get(index).getPlayer(rowIndex).setName(new String((String) aValue));
				setupPlayersTable();
			}
		}
		
	}

}
