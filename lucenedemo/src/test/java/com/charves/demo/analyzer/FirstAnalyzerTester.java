package com.charves.demo.analyzer;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.junit.Test;

public class FirstAnalyzerTester {
	
	private static String strCh = "�л����񹲺͹�����й���Chinese������һ����13���˿ڵĹ���" ;
	private static String strEn = "Dogs can not achieve a place" ;
	
	/**
	 * ���Ա�׼�ִ���
	 * @throws IOException 
	 */
	@Test
	public void testStdAnalyzerForCn() throws IOException {
		/**
		 * ����API�ĵ�����Pa ckage org.apache.lucene.analysis ��������
		 *   Analyzer�Ƿ������Ĺ����������������ı�����������������CharFilter(s),Tokenizer(s)��TokenFilter(s)
		 *   ������Reader��Tokenizer
		 */
		Analyzer stdAnalyzer = new StandardAnalyzer();
		TokenStream ts = stdAnalyzer.tokenStream("cn", new StringReader(strCh));
		/*
		 * ����{@link TokenStream} API˵����
		 *    ����������{@link Tokenizer}-�ֽ������ı�Ϊtoken���к�{@link TokenFilter}-�޸�token���к���������
		 *    
		 *    ��չ��AttributeSource����ˣ�����һ����;���Ƕ�tokens���н����о١�
		 *    
		 *    �������ж��{@link AttributeImpl}s,����ÿ�� AttributeImplֻ�ᴴ��һ��ʵ������Ϊÿ��token���á�
		 *    
		 *    Ϊȷ���п��õ�Attributes������ʵ���������ǰ���Attribute
		 */
		
		//���{@link AttributeSource }�Ƿ���ڶ�ӦAttribute���ʵ����������ֱ�ӷ��أ��������򴴽�һ���µĲ�����AttributeSource
		OffsetAttribute att1 = ts.addAttribute(OffsetAttribute.class) ;
		//���Զ����ͬ���͵�Attribute
		CharTermAttribute att2 = ts.addAttribute(CharTermAttribute.class) ;
		String result1 = "";
		String result2 = "";
		try {
			
			ts.reset(); //incrementToken()֮ǰ�������
			
			while(ts.incrementToken()) {
				//��֤AttributeImplֻ����һ��ʵ��
				assertTrue(ts.getAttribute(OffsetAttribute.class) == att1);
				result1+= att1.toString()+"|";
				System.out.println("token:"+ts.reflectAsString(true));
				System.out.println("token start offset:" + att1.startOffset());
				System.out.println("token end offset:" + att1.endOffset());
				//��֤AttributeImplֻ����һ��ʵ��
				assertTrue(ts.getAttribute(CharTermAttribute.class) == att2);
				result2 += att2.toString() + "|";
			}
			System.out.println("���ķִʽ����");
			System.out.println("result1:"+result1);
			System.out.println("result2:"+result2);
			//�����һ��token�����ʣ���incrementToken()����false�󣬽������ø÷���ִ��ĳЩend-of-stream��������ĳЩ�������������ģ� �μ�API˵����
			ts.end();
		}finally {
			ts.close();
			stdAnalyzer.close();
		}
		
	}
	
	@Test
	public void testStdAnalyzerForEn() throws IOException {
		
		try(Analyzer stdAnalyzer = new StandardAnalyzer();
				TokenStream ts = stdAnalyzer.tokenStream("en", new StringReader(strEn))	) {
			CharTermAttribute attr = ts.addAttribute(CharTermAttribute.class);
			ts.reset();
			System.out.println("Ӣ�ķִʽ����");
			while(ts.incrementToken()) {
				System.out.print(attr.toString()+"|");
			}
			ts.end();
		}
	}
	
	/**
	 * ���Ը��ֳ����ķ�����
	 * @throws IOException 
	 */
	@Test
	public void testAalyzers() throws IOException {
		
		//��׼�ִ������ո�ͷ��ŷִ�(�磺��㣬���ŵ�)��֧��ͣ�ôʣ�����������֣���ĸ��EMAIL��IP�������ַ��ķ�������
		try(Analyzer analyzer = new StandardAnalyzer()) {
			System.out.println("��׼�ִ�����" + analyzer.getClass());
			TestUtils.printAnalyzer(analyzer,strCh);
		}
		
		//�ո�ִʣ�ʹ�ÿո���ɷִʣ������ʻ���˺�תСд
		try(Analyzer analyzer = new WhitespaceAnalyzer()){
			System.out.println("�ո�ִ�����" + analyzer.getClass());
			TestUtils.printAnalyzer(analyzer,strCh);
		}
		//�߱����������Ĵʻ�����ķִ������Է���ĸΪ�ָ���ţ������ʻ����
		try(Analyzer analyzer = new SimpleAnalyzer()){
			System.out.println("�򵥷ִ�����" + analyzer.getClass());
			TestUtils.printAnalyzer(analyzer,strCh);
		}
		
		try(Analyzer analyzer = new CJKAnalyzer()){
			System.out.println("���ִַ�����" + analyzer.getClass());
			TestUtils.printAnalyzer(analyzer,strCh);
		}
		
		try(Analyzer analyzer = new KeywordAnalyzer()){
			System.out.println("�ؼ��ִַ�����" + analyzer.getClass());
			TestUtils.printAnalyzer(analyzer,strCh);
		}
		
		try(Analyzer analyzer = new SmartChineseAnalyzer()){
			System.out.println("�������ķִ�����" + analyzer.getClass());
			TestUtils.printAnalyzer(analyzer,strCh);
		}
		
	}
	
	

}
