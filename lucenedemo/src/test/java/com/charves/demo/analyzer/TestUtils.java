package com.charves.demo.analyzer;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class TestUtils {
	
	public static void printAnalyzer(Analyzer analyzer,String str) throws IOException {
		
		TokenStream ts = analyzer.tokenStream("cn", new StringReader(str));
		
		try {
			
			CharTermAttribute attr = ts.addAttribute(CharTermAttribute.class);
			ts.reset();
			while(ts.incrementToken()) {
				System.out.print(attr.toString()+"|");
			}
			System.out.println();
			ts.end();
		}finally {
			ts.close();
		}
		
	}
}
