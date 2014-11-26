package com.tutorialspoint.lucene;
import javax.swing.*;

import org.apache.lucene.search.TopDocs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import org.apache.lucene.queryParser.ParseException;

import java.io.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

public class GlavniProzor extends JFrame {
	private JTextField txtPretraga;
	public static LuceneTester tester;
	private JTextField txtFolderPretrage;
	
	GlavniProzor ()
	{
		
		setTitle("PRETRAZIVAC");
		setSize(500,500);
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(desktopPane);
		
		txtPretraga = new JTextField();
		txtPretraga.setBounds(197, 113, 151, 20);
		desktopPane.add(txtPretraga);
		txtPretraga.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Unesite trazenu rec:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(77, 115, 114, 14);
		desktopPane.add(lblNewLabel);
		
		JButton btnPretrazi = new JButton("Pretrazi");
		btnPretrazi.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				if (txtFolderPretrage.getText() == null || txtFolderPretrage.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Molimo izaberite folder za pretragu!");
					return;
				}
				else if (txtPretraga.getText() == null || txtPretraga.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Molimo unesite rec za pretragu!");
					return;
				}
				try {	 
			    	tester = new LuceneTester();
			    	tester.dataDir = txtFolderPretrage.getText();
			    	tester.createIndex();
			    } catch (IOException e) {
				    e.printStackTrace();
			    } 
				
				try{
					long startTime = System.currentTimeMillis(); 
					
					TopDocs hits = tester.search(txtPretraga.getText());
					long endTime = System.currentTimeMillis();
					JOptionPane.showMessageDialog(null, "Ukupno pronadjeno: " + hits.totalHits + ".\n Ukupno vreme pretrage: " + (endTime - startTime) + " ms.");
				} catch(IOException e){
					e.printStackTrace();
				} catch(ParseException e){
					e.printStackTrace();
				}
										
			}
		});
		btnPretrazi.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnPretrazi.setBounds(227, 144, 89, 23);
		desktopPane.add(btnPretrazi);
		
		JButton btnIzaberi = new JButton("Izaberi");
		btnIzaberi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnIzaberi.setBounds(363, 53, 78, 23);
		btnIzaberi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("."));
				fc.setDialogTitle("Izaberi");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				//fc.setMultiSelectionEnabled(true);
				
				if (fc.showOpenDialog(GlavniProzor.this) ==JFileChooser.APPROVE_OPTION) {
					txtFolderPretrage.setText(fc.getSelectedFile().getAbsolutePath());
					
				}
										
			}
		});
		desktopPane.add(btnIzaberi);
		
		txtFolderPretrage = new JTextField();
		txtFolderPretrage.setBounds(195, 54, 158, 20);
		desktopPane.add(txtFolderPretrage);
		txtFolderPretrage.setColumns(10);
		
		JLabel lblIzaberiteFolderZa = new JLabel("Izaberite folder za pretragu:");
		lblIzaberiteFolderZa.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblIzaberiteFolderZa.setBounds(33, 56, 158, 14);
		desktopPane.add(lblIzaberiteFolderZa);
		
		
		
	}
	
	public static void main(String[] args){
		
	  
		
		GlavniProzor glavniProzor = new GlavniProzor();
		glavniProzor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		glavniProzor.setVisible(true);
	}
}
