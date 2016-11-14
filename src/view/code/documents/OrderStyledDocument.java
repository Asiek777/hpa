package view.code.documents;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import core.Parser;
import view.code.NumHeader;

public class OrderStyledDocument extends ColorStyledDocument {
	private static final long serialVersionUID = 1L;
	
	private AttributeSet defStyle, labelStyle, orderStyle, paramStyle, invStyle;
	private AttributeSet defBG, highBG;
	public OrderStyledDocument(NumHeader numHeader) {
		super(numHeader);
		StyleContext cont = StyleContext.getDefaultStyleContext();
		defStyle = cont.addAttribute(cont.addAttribute(cont.getEmptySet(), StyleConstants.FontFamily, "Monospaced"), StyleConstants.FontSize, 18);
		labelStyle = cont.addAttribute(cont.addAttribute(defStyle, StyleConstants.Italic, true), StyleConstants.Foreground, Color.ORANGE);
		orderStyle = cont.addAttribute(cont.addAttribute(defStyle, StyleConstants.Bold, true), StyleConstants.Foreground, Color.CYAN);
		paramStyle = cont.addAttribute(defStyle, StyleConstants.Bold, true);
		invStyle = cont.addAttribute(defStyle, StyleConstants.Foreground, Color.red);
		defBG = cont.addAttribute(cont.getEmptySet(), StyleConstants.Background, Color.white);
		highBG = cont.addAttribute(cont.getEmptySet(), StyleConstants.Background, Color.cyan);
		dFont = cont.getFont(defStyle);
		numHeader.updateHeader(dFont);
	}
	
	public void recalculateStyles(){
		try{
			super.setCharacterAttributes(0, super.getLength(), defStyle, true);

			int[][] indexes = Parser.split(super.getText(0, super.getLength()), false);

			for(int i = 0; i < indexes[0].length; i++){
				super.setCharacterAttributes(indexes[0][i], indexes[0][++i], labelStyle, true);
			}
			for(int i = 0; i < indexes[1].length; i++){
				super.setCharacterAttributes(indexes[1][i], indexes[1][++i], orderStyle, true);
			}
			for(int i = 0; i < indexes[2].length; i++){
				super.setCharacterAttributes(indexes[2][i], indexes[2][++i], paramStyle, true);
			}
			for(int i = 0; i < indexes[3].length; i++){
				super.setCharacterAttributes(indexes[3][i], indexes[3][++i], invStyle, true);
			}
			
		}catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void highlightLine(int index){
		try {
			int startI = 0, endI = 0;
			char[] chars = super.getText(0, super.getLength()).toCharArray();
			for(int i = 0, j = 0; j < chars.length && i < index; j++){
				if(chars[j] == '\n'){
					i++;
					if(i == index){
						endI = j;
					}else{
						startI = j+1;
					}
				}
			}
			super.setCharacterAttributes(0, super.getLength(), defBG, false);
			super.setCharacterAttributes(startI, endI-startI, highBG, false);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
