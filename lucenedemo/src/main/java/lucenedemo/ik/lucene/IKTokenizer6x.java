package lucenedemo.ik.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

/**
 * lucene 6.x及以后，Tokenizer的构造函数不再支持Reader 作为入参数（
 * 更改为在{@link Analyzer#tokenStream(String, java.io.Reader)}传入）
 * IKTokenizer针对Luence 4编写，因此需要重构。
<<<<<<< HEAD
 * 适用范围：6.x-2012
=======
 *   测试冲突
>>>>>>> 4837770... made conficting for testing merge
 * @author zqw
 *
 */
public class IKTokenizer6x extends Tokenizer {
	//IK分词器实现
	 private IKSegmenter _IKImplement;
	// 词元文本属性
	  private final CharTermAttribute termAtt;
	  // 词元位移属性
	  private final OffsetAttribute offsetAtt;
	  // 词元分类属性，(该属性参考org.wltea.analyzer.core.Lexeme中的分类常量)
	  private final TypeAttribute typeAtt;
	  // 记录最后一个词元的结束位置
	  private int endPosition;
	  
	public IKTokenizer6x(boolean useSmart) {
		super();
		termAtt = addAttribute(CharTermAttribute.class);
		offsetAtt = addAttribute(OffsetAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);
		_IKImplement = new IKSegmenter(input, useSmart);
	}

	@Override
	public void reset() throws IOException {
		super.reset();
		_IKImplement.reset(input);
	}

	@Override
	public void end() throws IOException {
//		super.end();
		int finalOffset = correctOffset(endPosition);
		offsetAtt.setOffset(finalOffset, finalOffset);
	}

	@Override
	public boolean incrementToken() throws IOException {
		clearAttributes(); //清除词元属性
		Lexeme nextLexeme = _IKImplement.next();
		
		if(nextLexeme != null) {
			termAtt.append(nextLexeme.getLexemeText());
			termAtt.setLength(nextLexeme.getLength());
			offsetAtt.setOffset(nextLexeme.getBeginPosition(), 
					nextLexeme.getEndPosition());
			endPosition = nextLexeme.getEndPosition();
			typeAtt.setType(nextLexeme.getLexemeTypeString());
			return true;
		}
		return false;
	}

}
