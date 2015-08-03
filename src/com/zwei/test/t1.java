package com.zwei.test;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;


public class t1 {
	


	/**
	 * @param args
	 * @throws org.apache.lucene.queryparser.classic.ParseException 
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws org.apache.lucene.queryparser.classic.ParseException {
		// TODO Auto-generated method stub
		
		long startLoadDict = System.currentTimeMillis();
		Dictionary.initial(DefaultConfig.getInstance());
		long endLoadDict = System.currentTimeMillis();
		
		String fieldName = "text";
		String text = "一个结合词典分词和文法分词的中文分词开源工具包。它使用了全新的正向迭代最细粒度切分算法";
		
		Analyzer analyzer = new IKAnalyzer(true);
		
		Directory directory = null;
		IndexWriter iwriter = null;
		IndexReader iReader = null;
		IndexSearcher iSearcher = null;
		
		try {
			directory = new RAMDirectory();
			
			IndexWriterConfig iWriterConfig = new IndexWriterConfig(org.apache.lucene.util.Version.LUCENE_46, analyzer);
			iWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			iwriter = new IndexWriter(directory,iWriterConfig);
			
			Document document = new Document();
			document.add(new Field("ID","10000",Field.Store.YES,Field.Index.NOT_ANALYZED));
			document.add(new Field(fieldName,text ,Field.Store.YES,Field.Index.ANALYZED));
			iwriter.addDocument(document);
			iwriter.close();
			
			iReader = IndexReader.open(directory);
			iSearcher = new IndexSearcher(iReader);
			
			String keyword = "中文分词工具包";
			
			QueryParser qp = new QueryParser(Version.LUCENE_46,fieldName,analyzer);
			qp.setDefaultOperator(qp.AND_OPERATOR);
			Query query = qp.parse(keyword);
			
			TopDocs topDocs = iSearcher.search(query, 5);
			System.out.println("命中:"+topDocs.totalHits);
			
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for(int i =0;i <topDocs.totalHits;i++){
				Document targetDocument = iSearcher.doc(scoreDocs[i].doc);
				System.out.println("内容:"+targetDocument.toString());
				System.out.println();
			}
			System.err.println();
		}catch (CorruptIndexException e) {
			e.printStackTrace();
			// TODO: handle exception
		}catch (LockObtainFailedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}catch (ParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(iReader != null){
				try{
					iReader.close();
				}catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
			if(directory!=null){
				try{
				directory.close();
			}catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		} 
		}
	}

}
