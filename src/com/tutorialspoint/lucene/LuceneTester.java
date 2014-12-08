package com.tutorialspoint.lucene;

import java.awt.TextField;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class LuceneTester {
	 
   String indexDir = "D:\\Lucene\\Index";
   String dataDir = "D:\\Lucene\\Data";
   Indexer indexer;

   public void createIndex() throws IOException{
      indexer = new Indexer(indexDir);
      int numIndexed;
      long startTime = System.currentTimeMillis();	
      numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
      long endTime = System.currentTimeMillis();
      indexer.close();
      System.out.println(numIndexed+" File indexed, time taken: "
         +(endTime-startTime)+" ms");		
   }

   public TopDocs search(String searchQuery) throws IOException, ParseException{
      Searcher searcher = new Searcher(indexDir);
      TopDocs hits = searcher.search(searchQuery);
      for(ScoreDoc scoreDoc : hits.scoreDocs) {
	         Document doc = searcher.getDocument(scoreDoc);
	            System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH));
	      }
	searcher.close();
      return hits;
   }   
}