package com.tutorialspoint.lucene;
import javax.swing.*;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.JComponent;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.queryParser.ParseException;

import java.io.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.sun.corba.se.impl.encoding.CodeSetConversion.BTCConverter;
//import com.sun.glass.events.KeyEvent;


import com.sun.javafx.scene.text.HitInfo;

import java.awt.event.KeyAdapter;

public class GlavniProzor extends JFrame {
	private JTextField txtPretraga;
	public static LuceneTester tester;
	private JTextField txtFolderPretrage;
	public static Searcher searcher;
	
	GlavniProzor ()
	{
		//kreiranje prozora
		setTitle("PRETRAZIVAC");
		setSize(500,500);
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(desktopPane);
		
		txtPretraga = new JTextField();
		txtPretraga.addKeyListener(new KeyAdapter() {
			
			public void keyPressed(KeyEvent n) {
				if (n.getKeyCode()==KeyEvent.VK_ENTER){
					IzvrsiPretragu();
				}					
			}
		});
		txtPretraga.setBounds(197, 113, 151, 20);
		desktopPane.add(txtPretraga);
		txtPretraga.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Unesite trazenu rec:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(77, 115, 114, 14);
		desktopPane.add(lblNewLabel);
		
		//akcija koja se poziva prilikom pritiska na dugme pretrazi
		JButton btnPretrazi = new JButton("Pretrazi");
		btnPretrazi.addActionListener(new ActionListener() {
						
			public void actionPerformed(ActionEvent arg0) {
				IzvrsiPretragu();
			}
		});
		//kreiranje izgleda dugmeta pretrazi
		btnPretrazi.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnPretrazi.setBounds(227, 144, 89, 23);
		desktopPane.add(btnPretrazi);
		
		// akcija koja se poziva pritiskom na dugme IZABERI i njegovo kreiranje
		JButton btnIzaberi = new JButton("Izaberi");
		btnIzaberi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnIzaberi.setBounds(363, 53, 78, 23);
		btnIzaberi.addActionListener(new ActionListener() {
		
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
		txtFolderPretrage.addKeyListener(new KeyAdapter() {
		
			public void keyPressed(KeyEvent n) {
					if (n.getKeyCode()==KeyEvent.VK_ENTER){
						IzvrsiPretragu();
					}						
				}			
		});
		txtFolderPretrage.setBounds(195, 54, 158, 20);
		desktopPane.add(txtFolderPretrage);
		txtFolderPretrage.setColumns(10);
		
		JLabel lblIzaberiteFolderZa = new JLabel("Izaberite folder za pretragu:");
		lblIzaberiteFolderZa.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblIzaberiteFolderZa.setBounds(33, 56, 158, 14);
		desktopPane.add(lblIzaberiteFolderZa);		
	}
	
	public static void main(String[] args){		//main funkcija koja se poziva
		
		GlavniProzor glavniProzor = new GlavniProzor();
		glavniProzor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		glavniProzor.setVisible(true);
	}
	
	private String getPutanja(TopDocs hits){
		  String putanja = "" ;
		 
		  try{
		    for(ScoreDoc scoreDoc : hits.scoreDocs) {
		      Document doc = tester.searcher.getDocument(scoreDoc);
		      putanja += doc.get(LuceneConstants.FILE_PATH);
		    }
		  } catch (IOException e) {
		    e.printStackTrace();
		  }
		 
		  return putanja;
		 
		}
		
	public void IzvrsiPretragu(){ 	//metoda za izvrsenje pretrage
			//provera da li je prvo polje prazno
		if (txtFolderPretrage.getText() == null || txtFolderPretrage.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Molimo izaberite folder za pretragu!");
			return;
		}
			//provera da li je drugo polje prazno
		else if (txtPretraga.getText() == null || txtPretraga.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, "Molimo unesite rec za pretragu!");			
			return;
		}
		try {	 
	    	tester = new LuceneTester();
	    	tester.dataDir = txtFolderPretrage.getText();		//pristup folderu iz kojeg pretrazujemo
	    	tester.createIndex();		//kreiranje indexa, onoga sto smestamo u folder index radi brze pretrage
	    } catch (IOException e) {
		    e.printStackTrace();
	    } 		
		try{
			//racunanje pogodaka i brzine vremena pretrage
			long startTime = System.currentTimeMillis(); 			
			TopDocs hits = tester.search(txtPretraga.getText());
			long endTime = System.currentTimeMillis();
			String putanja = getPutanja(hits);
			JOptionPane.showMessageDialog(null, "Ukupno pronadjeno: " + hits.totalHits + ".\n Ukupno vreme pretrage: " + (endTime - startTime) + " ms." + ".\n Putanja: " + putanja );
		} catch(IOException e){
			e.printStackTrace();
		} catch(ParseException e){
			e.printStackTrace();
		}	
		
		}
}	

