/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import edu.stanford.nlp.util.CollectionUtils;
import filemanagment.InputReader;
import filemanagment.OutputGenerator;
import parsers.OpenNLParser;
import parsers.StanfordNLParser;
import gui.PanelTriplet;
import utils.ETInput;
import utils.ParserDriver;
import utils.RDFTriplet;

/**
 *
 * @author aliha
 */
public class MainFrame extends javax.swing.JFrame {

	/**
	 * Creates new form MainFrame
	 */
	private ETInput input;
	private String fileName;
	private ArrayList<RDFTriplet> triplets;
	private int counter_sentence;

	private class ParsingThread extends Thread {

		private ETInput input;
		MainFrame frame;

		public ParsingThread(ETInput input, MainFrame frame) {
			// TODO Auto-generated constructor stub

			this.input = input;
			this.frame = frame;
		}

		@Override
		public void run() {
			panel_triplets.removeAll();
			frame.validate();
			frame.repaint();
			ParserDriver driver = null;
			ParserDriver driverOpen = null; // for test both cases
			if (checkbox_stanford.isSelected() && !checkbox_open.isSelected()) {
				input.setVoacbDomain((String) combo_domain.getSelectedItem());
				driver = new ParserDriver(new StanfordNLParser(counter_sentence));
				if (input != null) {
					driver.executeStrategy(this.input);
					if (driver.getTripletsList() != null) {

						for (RDFTriplet triplet : driver.getTripletsList()) {

							PanelTriplet triplet_panel = new PanelTriplet(triplet);
							panel_triplets.add(triplet_panel);
							frame.validate();
							frame.repaint();
						}
						btn_validate.setEnabled(true);
					} else {
						JOptionPane.showMessageDialog(null, "Sentence can't be parsed go to the next one", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {

					JOptionPane.showMessageDialog(null, "Please choose an input file");
				}
			} else if (checkbox_open.isSelected() && !checkbox_stanford.isSelected()) {
				driver = new ParserDriver(new OpenNLParser(counter_sentence));
				if (input != null) {
					driver.executeStrategy(this.input);
					if (driver.getTripletsList() != null) {
						for (RDFTriplet triplet : driver.getTripletsList()) {

							PanelTriplet triplet_panel = new PanelTriplet(triplet);
							panel_triplets.add(triplet_panel);
							frame.validate();
							frame.repaint();
						}
						btn_validate.setEnabled(true);
					} else {
						JOptionPane.showMessageDialog(null, "Sentence can't be parsed go to the next one", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {

					JOptionPane.showMessageDialog(null, "Please choose an input file");
				}
			} else if (checkbox_open.isSelected() && checkbox_stanford.isSelected()) {
				input.setVoacbDomain((String) combo_domain.getSelectedItem());
				if (input != null) {
					driver = new ParserDriver(new StanfordNLParser(counter_sentence));
					driver.executeStrategy(this.input);

					driverOpen = new ParserDriver(new OpenNLParser(counter_sentence));
					driverOpen.executeStrategy(this.input);

					ArrayList<RDFTriplet> stanfordTripletList = driver.getTripletsList();
					ArrayList<RDFTriplet> openTripletList = driverOpen.getTripletsList();

					if (stanfordTripletList != null && openTripletList != null) {
						List<RDFTriplet> openTripletListFinal = new ArrayList<>(openTripletList);
						List<RDFTriplet> stanfordTripletListFinal = new ArrayList<>(stanfordTripletList);

						List<RDFTriplet> openTripletListCommon = new ArrayList<>(openTripletList);
						List<RDFTriplet> stanfordTripletListCommon = new ArrayList<>(stanfordTripletList);

						stanfordTripletListFinal.removeAll(openTripletList);

						openTripletListFinal.removeAll(stanfordTripletList);

						openTripletListCommon.retainAll(stanfordTripletListCommon);

						if (stanfordTripletListFinal != null) {
							for (RDFTriplet triplet : stanfordTripletListFinal) {
								JTextArea sentence_area = new JTextArea();
								sentence_area.setText(triplet.toString() + "\nfrom StanfordNLP");
								sentence_area.setSize(2, 2);
								sentence_area.setFont(new java.awt.Font("Tahoma", 0, 16));
								panel_triplets.add(sentence_area);
								frame.validate();
								frame.repaint();
							}
						}
						if (openTripletListFinal != null) {
							for (RDFTriplet triplet : openTripletListFinal) {
								JTextArea sentence_area = new JTextArea();
								sentence_area.setText(triplet.toString() + "\nfrom OpenNLP");
								sentence_area.setSize(2, 2);
								sentence_area.setFont(new java.awt.Font("Tahoma", 0, 16));
								panel_triplets.add(sentence_area);
								frame.validate();
								frame.repaint();
							}
						}

						if (openTripletListCommon != null) {
							for (RDFTriplet triplet : openTripletListCommon) {
								JTextArea sentence_area = new JTextArea();
								sentence_area.setText(triplet.toString() + "\nfrom Both");
								sentence_area.setSize(2, 2);
								sentence_area.setFont(new java.awt.Font("Tahoma", 0, 16));
								panel_triplets.add(sentence_area);
								frame.validate();
								frame.repaint();
							}

						}
						btn_validate.setEnabled(true);

					} else if (stanfordTripletList != null && openTripletList == null) {
						for (RDFTriplet triplet : stanfordTripletList) {
							JTextArea sentence_area = new JTextArea();
							sentence_area.setText(triplet.toString() + "\nfrom StanfordNLP o");
							sentence_area.setSize(2, 2);
							sentence_area.setFont(new java.awt.Font("Tahoma", 0, 16));
							panel_triplets.add(sentence_area);
							frame.validate();
							frame.repaint();
						}
						btn_validate.setEnabled(true);
					} else if (stanfordTripletList == null && openTripletList != null) {
						for (RDFTriplet triplet : openTripletList) {
							JTextArea sentence_area = new JTextArea();
							sentence_area.setText(triplet.toString() + "\nfrom OpenNLP o");
							sentence_area.setSize(2, 2);
							sentence_area.setFont(new java.awt.Font("Tahoma", 0, 16));
							panel_triplets.add(sentence_area);
							frame.validate();
							frame.repaint();
						}
						btn_validate.setEnabled(true);
					} else {
						JOptionPane.showMessageDialog(null, "Sentence can't be parsed go to the next one", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				} else {

					JOptionPane.showMessageDialog(null, "Please choose an input file", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			} else if (!checkbox_open.isSelected() && !checkbox_stanford.isSelected()) {
				JOptionPane.showMessageDialog(null, "Please choose a parser", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	private void initTriplets() {
		for (int i = 0; i < input.getSentences().size(); i++) {
			triplets.add(null);
		}
	}

	public MainFrame() {
		counter_sentence = 0;
		triplets = new ArrayList<>();
		initComponents();
		panel_triplets.setLayout(new GridLayout(0, 3));
		panel_triplets.setPreferredSize(new Dimension(300, 200));
		btn_next_sentence.setEnabled(false);
		btn_previous_sentence.setEnabled(false);
		btn_validate.setEnabled(false);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabel2 = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jPanel3 = new javax.swing.JPanel();
		label_file_name = new javax.swing.JLabel();
		btn_browse = new javax.swing.JButton();
		jLabel5 = new javax.swing.JLabel();
		jPanel6 = new javax.swing.JPanel();
		jPanel7 = new javax.swing.JPanel();
		text_field_outfile_name = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		btn_save_output = new javax.swing.JButton();
		checkbox_stanford = new javax.swing.JCheckBox();
		checkbox_open = new javax.swing.JCheckBox();
		jPanel4 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		text_area_sentence = new javax.swing.JTextArea();
		jPanel5 = new javax.swing.JPanel();
		btn_next_sentence = new javax.swing.JButton();
		btn_validate = new javax.swing.JButton();
		jSeparator1 = new javax.swing.JSeparator();
		btn_previous_sentence = new javax.swing.JButton();
		btn_start = new javax.swing.JButton();
		combo_domain = new javax.swing.JComboBox<>();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		panel_triplets = new javax.swing.JPanel();

		jLabel2.setText("jLabel2");

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jPanel1.setBackground(new java.awt.Color(204, 51, 0));
		jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));

		jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
		jLabel1.setForeground(new java.awt.Color(255, 255, 255));
		jLabel1.setText("Triplet Extraction Project CPS2");

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addGap(21, 21, 21).addComponent(jLabel1)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
						.addContainerGap(29, Short.MAX_VALUE).addComponent(jLabel1).addGap(26, 26, 26)));

		jPanel3.setBackground(new java.awt.Color(51, 51, 51));

		label_file_name.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		label_file_name.setForeground(new java.awt.Color(255, 255, 255));
		label_file_name.setText("Choose input file...");

		btn_browse.setText("Browse");
		btn_browse.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_browseActionPerformed(evt);
			}
		});

		jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		jLabel5.setForeground(new java.awt.Color(255, 255, 255));
		jLabel5.setText("Choose parser");

		javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
		jPanel6.setLayout(jPanel6Layout);
		jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 12, Short.MAX_VALUE));
		jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));

		javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
		jPanel7.setLayout(jPanel7Layout);
		jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 10, Short.MAX_VALUE));
		jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));

		jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		jLabel3.setForeground(new java.awt.Color(255, 255, 255));
		jLabel3.setText("Output file name:");

		btn_save_output.setText("Save ouput");
		btn_save_output.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_save_outputActionPerformed(evt);
			}
		});

		checkbox_stanford.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		checkbox_stanford.setForeground(new java.awt.Color(255, 255, 255));
		checkbox_stanford.setText("StanfordNLP");

		checkbox_open.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		checkbox_open.setForeground(new java.awt.Color(255, 255, 255));
		checkbox_open.setText("OpenNLP");

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel3Layout.createSequentialGroup().addGap(59, 59, 59).addComponent(btn_browse,
								javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(jPanel3Layout.createSequentialGroup().addGap(43, 43, 43).addComponent(label_file_name,
								javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addGap(53, 53, 53)
						.addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(
										jPanel3Layout.createSequentialGroup().addGap(61, 61, 61).addComponent(jLabel5))
								.addGroup(jPanel3Layout.createSequentialGroup().addGap(43, 43, 43)
										.addGroup(jPanel3Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(checkbox_open, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(checkbox_stanford, javax.swing.GroupLayout.DEFAULT_SIZE,
														156, Short.MAX_VALUE))))
						.addGap(40, 40, 40)
						.addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(btn_save_output, javax.swing.GroupLayout.PREFERRED_SIZE, 157,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(text_field_outfile_name, javax.swing.GroupLayout.PREFERRED_SIZE, 157,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 157,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(224, Short.MAX_VALUE)));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout
										.createSequentialGroup()
										.addGroup(jPanel3Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(label_file_name).addComponent(jLabel5))
										.addGap(18, 18, 18)
										.addGroup(jPanel3Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(btn_browse).addComponent(checkbox_stanford))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(checkbox_open).addGap(0, 0, Short.MAX_VALUE))
								.addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addContainerGap())
				.addGroup(jPanel3Layout.createSequentialGroup().addGap(19, 19, 19).addComponent(jLabel3)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(text_field_outfile_name, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18).addComponent(btn_save_output).addContainerGap(31, Short.MAX_VALUE)));

		jPanel4.setBackground(new java.awt.Color(51, 51, 51));

		text_area_sentence.setColumns(20);
		text_area_sentence.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
		text_area_sentence.setRows(5);
		jScrollPane1.setViewportView(text_area_sentence);

		jPanel5.setBackground(new java.awt.Color(153, 153, 153));

		btn_next_sentence.setText("Next Sentence");
		btn_next_sentence.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_next_sentenceActionPerformed(evt);
			}
		});

		btn_validate.setText("Validate");
		btn_validate.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_validateActionPerformed(evt);
			}
		});

		btn_previous_sentence.setText("Previous Sentence");
		btn_previous_sentence.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_previous_sentenceActionPerformed(evt);
			}
		});

		btn_start.setText("Parse Current");
		btn_start.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btn_startActionPerformed(evt);
			}
		});

		combo_domain.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "politics", "economics" }));

		javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
		jPanel5.setLayout(jPanel5Layout);
		jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addGroup(jPanel5Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(btn_start, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(jPanel5Layout.createSequentialGroup()
								.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(jPanel5Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(btn_validate, javax.swing.GroupLayout.DEFAULT_SIZE, 143,
														Short.MAX_VALUE)
												.addComponent(jSeparator1))
										.addComponent(btn_next_sentence, javax.swing.GroupLayout.PREFERRED_SIZE, 137,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(btn_previous_sentence))
								.addGap(0, 0, Short.MAX_VALUE))
						.addComponent(combo_domain, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));
		jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
						.addGap(30, 30, 30)
						.addComponent(btn_next_sentence, javax.swing.GroupLayout.PREFERRED_SIZE, 37,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addComponent(btn_previous_sentence, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
						.addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(43, 43, 43)
						.addComponent(combo_domain, javax.swing.GroupLayout.PREFERRED_SIZE, 31,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(btn_start, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18).addComponent(btn_validate, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(81, 81, 81)));

		jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		jLabel6.setForeground(new java.awt.Color(255, 255, 255));
		jLabel6.setText("Sentence");

		jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		jLabel7.setForeground(new java.awt.Color(255, 255, 255));
		jLabel7.setText("Triplet");

		javax.swing.GroupLayout panel_tripletsLayout = new javax.swing.GroupLayout(panel_triplets);
		panel_triplets.setLayout(panel_tripletsLayout);
		panel_tripletsLayout.setHorizontalGroup(panel_tripletsLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
		panel_tripletsLayout.setVerticalGroup(panel_tripletsLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1)
						.addComponent(panel_triplets, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(jPanel4Layout.createSequentialGroup()
								.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jLabel6).addComponent(jLabel7))
								.addGap(0, 0, Short.MAX_VALUE)))
						.addGap(18, 18, 18)
						.addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(jPanel4Layout.createSequentialGroup().addComponent(jLabel6)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 74,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jLabel7)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(panel_triplets, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addContainerGap()));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18).addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void btn_save_outputActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_save_outputActionPerformed
		String filename = text_field_outfile_name.getText();
		if (!filename.equals("")) {
			ArrayList<String> unsuccessful = new ArrayList<>();
			ArrayList<RDFTriplet> successfulTriplets = new ArrayList<>();
			for (int i = 0; i < triplets.size(); i++) {
				if (triplets.get(i) == null) {
					unsuccessful.add(input.getSentences().get(i));
				} else {
					successfulTriplets.add(triplets.get(i));
				}

			}
			OutputGenerator generator = new OutputGenerator(successfulTriplets, filename);
			generator.write();

			/* write unsuccessful sentences to a rubish file */

			File rubishFile = new File(filename + "_rubish.txt");
			try {
				FileWriter writer = new FileWriter(rubishFile);
				for (String str : unsuccessful) {
					writer.write(str);
					writer.write("\n");
				}
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please Enter output file name", "Input Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_btn_save_outputActionPerformed

	private void btn_browseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_browseActionPerformed
		JFileChooser chooser = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		chooser.setCurrentDirectory(workingDirectory);
		int accept_value = chooser.showOpenDialog(this);
		if (accept_value == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			label_file_name.setText(file.getName());

			InputReader reader = new InputReader(file.getAbsolutePath());

			input = reader.read();

			/*
			 * specify domain by hand for now later we could get from a combo box
			 */

			text_area_sentence.setText(input.getSentences().get(0));

			btn_next_sentence.setEnabled(true);

			initTriplets();
			// text_area_triplet.setText("press start to parse this sentence");

		} else {

		}
	}// GEN-LAST:event_btn_browseActionPerformed

	private void btn_startActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_startActionPerformed
		ParsingThread thread = new ParsingThread(input, this);
		thread.start();

	}// GEN-LAST:event_btn_startActionPerformed

	private void btn_next_sentenceActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_next_sentenceActionPerformed
		btn_validate.setBackground(Color.lightGray);
		counter_sentence++;

		if (counter_sentence > 0) {
			btn_previous_sentence.setEnabled(true);
		}
		if (counter_sentence == input.getSentences().size() - 1) {
			btn_next_sentence.setEnabled(false);

		}
		if (counter_sentence < input.getSentences().size()) {
			text_area_sentence.setText(input.getSentences().get(counter_sentence));
			if (triplets.get(counter_sentence) != null) {
				// text_area_triplet.setText(triplets.get(counter_sentence).toString());
				btn_validate.setEnabled(true);
				if (triplets.get(counter_sentence).isValidated())
					btn_validate.setBackground(Color.GREEN);
			} else {
				// text_area_triplet.setText("press start to parse this sentence");
			}
		}

	}// GEN-LAST:event_btn_next_sentenceActionPerformed

	private void btn_previous_sentenceActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_previous_sentenceActionPerformed

		btn_validate.setBackground(Color.lightGray);
		counter_sentence--;
		if (counter_sentence == 0) {
			btn_previous_sentence.setEnabled(false);
		}
		if (counter_sentence < input.getSentences().size()) {
			btn_next_sentence.setEnabled(true);
		}
		text_area_sentence.setText(input.getSentences().get(counter_sentence));
		if (triplets.get(counter_sentence) != null) {
			// text_area_triplet.setText(triplets.get(counter_sentence).toString());
			btn_validate.setEnabled(true);
			if (triplets.get(counter_sentence).isValidated())
				btn_validate.setBackground(Color.GREEN);
		} else {
			// text_area_triplet.setText("press start to parse this sentence");
		}

	}

	private void resetValidateButton() {
		btn_validate.setEnabled(true);
		btn_validate.setBackground(Color.lightGray);
	}
	// GEN-LAST:event_btn_previous_sentenceActionPerformed

	private void btn_validateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_validateActionPerformed
		// TODO add your handling code here:

		if (triplets.get(counter_sentence) == null) {
			return;
		}
		triplets.get(counter_sentence).setValidated(true);
		btn_validate.setBackground(Color.green);

		boolean all_validated = true;

		for (RDFTriplet triplet : triplets) {
			if (triplet != null && !triplet.isValidated()) {
				all_validated = false;
				break;
			}
			if (triplet == null) {
				all_validated = false;
				break;
			}
		}
		if (all_validated)
			btn_save_output.setEnabled(true);

		// btn_validate.setEnabled(false);

	}// GEN-LAST:event_btn_validateActionPerformed

	private void btn_next_tripletActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_next_tripletActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_btn_next_tripletActionPerformed

	private void btn_previous_tripletActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_previous_tripletActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_btn_previous_tripletActionPerformed

	private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_saveActionPerformed
		String filename = JOptionPane.showInputDialog("Enter the file name:");
		System.out.println(triplets);

	}// GEN-LAST:event_btn_saveActionPerformed

	/**
	 * @param args the command line arguments
	 */

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btn_browse;
	private javax.swing.JButton btn_next_sentence;
	private javax.swing.JButton btn_previous_sentence;
	private javax.swing.JButton btn_save_output;
	private javax.swing.JButton btn_start;
	private javax.swing.JButton btn_validate;
	private javax.swing.JCheckBox checkbox_open;
	private javax.swing.JCheckBox checkbox_stanford;
	private javax.swing.JComboBox<String> combo_domain;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel7;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JLabel label_file_name;
	private javax.swing.JPanel panel_triplets;
	private javax.swing.JTextArea text_area_sentence;
	private javax.swing.JTextField text_field_outfile_name;
	// End of variables declaration//GEN-END:variables
}
