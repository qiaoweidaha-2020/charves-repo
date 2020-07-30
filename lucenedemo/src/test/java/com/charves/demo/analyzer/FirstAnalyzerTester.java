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
	
	private static String strCh = "中华人民共和国简称中国（Chinese），是一个有13亿人口的国家" ;
	private static String strEn = "Dogs can not achieve a place" ;
	
	/**
	 * 测试标准分词器
	 * @throws IOException 
	 */
	@Test
	public void testStdAnalyzerForCn() throws IOException {
		/**
		 * 参照API文档对于Pa ckage org.apache.lucene.analysis 的描述：
		 *   Analyzer是分析链的工厂，本身并不处理文本，它构建分析链：CharFilter(s),Tokenizer(s)及TokenFilter(s)
		 *   并传递Reader给Tokenizer
		 */
		Analyzer stdAnalyzer = new StandardAnalyzer();
		TokenStream ts = stdAnalyzer.tokenStream("cn", new StringReader(strCh));
		/*
		 * 对照{@link TokenStream} API说明：
		 *    有两个子类{@link Tokenizer}-分解输入文本为token序列和{@link TokenFilter}-修改token序列和它的内容
		 *    
		 *    扩展自AttributeSource，因此，其另一种用途就是对tokens序列进行列举。
		 *    
		 *    它可以有多个{@link AttributeImpl}s,但是每个 AttributeImpl只会创建一个实例，并为每个token复用。
		 *    
		 *    为确保有可用的Attributes，请在实例化或访问前添加Attribute
		 */
		
		//检查{@link AttributeSource }是否存在对应Attribute类的实例，存在则直接返回，不存在则创建一个新的并加入AttributeSource
		OffsetAttribute att1 = ts.addAttribute(OffsetAttribute.class) ;
		//可以多个不同类型的Attribute
		CharTermAttribute att2 = ts.addAttribute(CharTermAttribute.class) ;
		String result1 = "";
		String result2 = "";
		try {
			
			ts.reset(); //incrementToken()之前必须调用
			
			while(ts.incrementToken()) {
				//验证AttributeImpl只会有一个实例
				assertTrue(ts.getAttribute(OffsetAttribute.class) == att1);
				result1+= att1.toString()+"|";
				System.out.println("token:"+ts.reflectAsString(true));
				System.out.println("token start offset:" + att1.startOffset());
				System.out.println("token end offset:" + att1.endOffset());
				//验证AttributeImpl只会有一个实例
				assertTrue(ts.getAttribute(CharTermAttribute.class) == att2);
				result2 += att2.toString() + "|";
			}
			System.out.println("中文分词结果：");
			System.out.println("result1:"+result1);
			System.out.println("result2:"+result2);
			//当最后一个token被访问，即incrementToken()返回false后，建议启用该方法执行某些end-of-stream操作，在某些情况下是有意义的， 参见API说明。
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
			System.out.println("英文分词结果：");
			while(ts.incrementToken()) {
				System.out.print(attr.toString()+"|");
			}
			ts.end();
		}
	}
	
	/**
	 * 测试各种常见的分析器
	 * @throws IOException 
	 */
	@Test
	public void testAalyzers() throws IOException {
		
		//标准分词器：空格和符号分词(如：标点，括号等)，支持停用词，可以完成数字，字母、EMAIL、IP及中文字符的分析处理
		try(Analyzer analyzer = new StandardAnalyzer()) {
			System.out.println("标准分词器：" + analyzer.getClass());
			TestUtils.printAnalyzer(analyzer,strCh);
		}
		
		//空格分词，使用空格完成分词，不做词汇过滤和转小写
		try(Analyzer analyzer = new WhitespaceAnalyzer()){
			System.out.println("空格分词器：" + analyzer.getClass());
			TestUtils.printAnalyzer(analyzer,strCh);
		}
		//具备基本的西文词汇分析的分词器，以非字母为分割符号，不做词汇过滤
		try(Analyzer analyzer = new SimpleAnalyzer()){
			System.out.println("简单分词器：" + analyzer.getClass());
			TestUtils.printAnalyzer(analyzer,strCh);
		}
		
		try(Analyzer analyzer = new CJKAnalyzer()){
			System.out.println("二分分词器：" + analyzer.getClass());
			TestUtils.printAnalyzer(analyzer,strCh);
		}
		
		try(Analyzer analyzer = new KeywordAnalyzer()){
			System.out.println("关键字分词器：" + analyzer.getClass());
			TestUtils.printAnalyzer(analyzer,strCh);
		}
		
		try(Analyzer analyzer = new SmartChineseAnalyzer()){
			System.out.println("智能中文分词器：" + analyzer.getClass());
			TestUtils.printAnalyzer(analyzer,strCh);
		}
		
	}
	
	

}
