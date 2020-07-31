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
 * lucene 6.x���Ժ�Tokenizer�Ĺ��캯������֧��Reader ��Ϊ�������
 * ����Ϊ��{@link Analyzer#tokenStream(String, java.io.Reader)}���룩
 * IKTokenizer���Luence 4��д�������Ҫ�ع���
<<<<<<< HEAD
 * ���÷�Χ��6.x-2012
=======
 *   ���Գ�ͻ
>>>>>>> 4837770... made conficting for testing merge
 * @author zqw
 *
 */
public class IKTokenizer6x extends Tokenizer {
	//IK�ִ���ʵ��
	 private IKSegmenter _IKImplement;
	// ��Ԫ�ı�����
	  private final CharTermAttribute termAtt;
	  // ��Ԫλ������
	  private final OffsetAttribute offsetAtt;
	  // ��Ԫ�������ԣ�(�����Բο�org.wltea.analyzer.core.Lexeme�еķ��ೣ��)
	  private final TypeAttribute typeAtt;
	  // ��¼���һ����Ԫ�Ľ���λ��
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
		clearAttributes(); //�����Ԫ����
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
