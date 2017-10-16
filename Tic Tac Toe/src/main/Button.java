package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class Button {

    private final String text;
    private Font font;
    private Color c,c2;

	private int x, y, width, height;
    private int mx=0, my=0;

    private boolean hover = false, clicked = false, init = false;

    public Button(String text, int x, int y, int size) {
        this.text = text;
        font = new Font("TimesNewRoman",0,size);
        c = Color.gray;
        this.x = x;
        this.y = y;
        c2=Color.black;
    }
    
    public Button(String text, int x, int y,Color c,Color c2,int size) {
        this.text = text;
        font = new Font("TimesNewRoman",0,size);
        this.c = c;
        this.c2 = c2;
        this.x = x;
        this.y = y;
    }

    public void update(Boolean mleft, int mx, int my) {
    	updateInputs(mleft, mx, my);
        if (!init) return;
        if(mx>x&&mx<x+width&&my<y&&my>y-height/2){
            hover=true;
        }else{
            hover = false;
        }
    }
    
    public void updateInputs(Boolean mleft, int mx, int my){
    	if(hover && mleft){
        	clicked = true;
        }else{
        	clicked = false;
        }
    }
    
    public void draw(Graphics2D g) {
        if (!init) {
            FontMetrics fm = g.getFontMetrics(font);
            width = fm.stringWidth(text);
            height = fm.getHeight();
            init = true;
        }
        g.setFont(font);
        g.setColor(c);
        if(hover)g.setColor(c2);
        g.drawString(text, x, y);
    }
    
    public boolean isClicked(){
        return clicked;
    }
    
    public String getText(){
    	return text;
    }
    
}
