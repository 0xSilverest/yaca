package com.ensate.chatapp.client.view;

import java.io.File;

import com.ensate.chatapp.client.App;
import com.ensate.chatapp.client.model.FileMessage;
import com.ensate.chatapp.client.model.UserMessage;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Hyperlink;

public class Bubble extends Group {

    private int p = 14;
    private int s = 2;
    private int pm = 10;
    private int sm = 2;
    private Font textFont = Font.font("Arial", 14);
    private Paint textColor = Color.WHITE;
    private Font metaFont = Font.font("Arial", 10);
    private Paint metaColor = Color.LIGHTGRAY;

    private Rectangle r;

    private Paint bubbleColor = Color.rgb(30, 33, 39);

    private int edgeRadius = 30;

    public Bubble(UserMessage msg) {
        super();
        init(0, 0, msg.getMessage(), msg.getTime().getHour() + ":" + msg.getTime().getMinute());
    
        if (msg instanceof FileMessage) {
            FileMessage flMsg = (FileMessage) msg;
            Hyperlink hl = new Hyperlink(flMsg.getFileName()); 
            hl.setOnAction(e -> saveFile(flMsg));
            getChildren().add(hl);
            checkType(flMsg);
        }
    }

    private void init(int x, int y, String text, String meta) {
        // temp for text
        Text temp = new Text(text);
        temp.setFont(textFont);
        int textW = (int) temp.getLayoutBounds().getWidth();
        int textH = (int) temp.getLayoutBounds().getHeight();
        int w = textW + p * 2 + s * 2;
        int h = textH + p * 2;
        temp = null;

        // tmp for meta
        Text tmp = new Text(meta);
        tmp.setFont(metaFont);
        int metaW = (int) tmp.getLayoutBounds().getWidth();
        int metaH = (int) tmp.getLayoutBounds().getHeight();
        h += metaH;
        temp = null;

        // label text
        Label l = new Label(text);
        l.setFont(textFont);
        l.setTextFill(textColor);
        l.setTranslateX(x + p + s);
        l.setTranslateY(y + p);

        // label meta
        Label m = new Label(meta);
        m.setFont(metaFont);
        m.setTextFill(metaColor);
        m.setTranslateX(x + (w - (metaW + pm + sm)));
        m.setTranslateY(y + textH + pm * 2);

        // bubble
        r = new Rectangle();
        r.setTranslateX(x);
        r.setTranslateY(y);

        r.setWidth(w);
        r.setHeight(h);

        r.setArcHeight(this.edgeRadius);
        r.setArcWidth(this.edgeRadius);

        r.setFill(bubbleColor);

        getChildren().addAll(r, l, m);
    }


    public void setWidth(double width) {
        r.setWidth(width);
    }

    public void setHeight(double height) {
        r.setWidth(height);
    }

    public void setQuadraticSize(double size) {
        r.setWidth(size);
        r.setHeight(size);
    }

    public void setEdgeRadius(int radius) {
        this.edgeRadius = radius;
    }

    public int getEdgeRadius() {
        return this.edgeRadius;
    }

    public int getTextPadding() {
        return p;
    }

    public void setTextPadding(int textPadding) {
        this.p = textPadding;
    }

    public int getTextSidespace() {
        return s;
    }

    public void setTextSidespace(int textSidespace) {
        this.s = textSidespace;
    }

    public int getMetaPadding() {
        return pm;
    }

    public void setMetaPadding(int metaPadding) {
        this.pm = metaPadding;
    }

    public int getMetaSidespace() {
        return sm;
    }

    public void setMetaSidespace(int metaSidespace) {
        this.sm = metaSidespace;
    }

    public Font getTextFont() {
        return textFont;
    }

    public void setTextFont(Font textFont) {
        this.textFont = textFont;
    }

    public Paint getTextColor() {
        return textColor;
    }

    public void setTextColor(Paint textColor) {
        this.textColor = textColor;
    }

    public Font getMetaFont() {
        return metaFont;
    }

    public void setMetaFont(Font metaFont) {
        this.metaFont = metaFont;
    }

    public Paint getMetaColor() {
        return metaColor;
    }

    public void setMetaColor(Paint metaColor) {
        this.metaColor = metaColor;
    }

    public Paint getBubbleColor() {
        return bubbleColor;
    }

    public void setBubbleColor(Paint bubbleColor) {
        this.bubbleColor = bubbleColor;
    }

    private void checkType (FileMessage flMsg) {
        switch(flMsg.getFileType()) {
            case IMAGE:
                flMsg.makeFile("/home/silverest/tmp");
                Image img = new Image("file:///home/silverest/tmp/"+flMsg.getFileName());
                ImageView imgVw = new ImageView(img); 
                imgVw.maxWidth(400);
                imgVw.setPreserveRatio(true);
                imgVw.setCache(true);
                getChildren().add(imgVw);
                break;

            case VIDEO:
                break;
            
            case DOC:
                break;
            
            case AUDIO:
                break;

            case OTHER:
                break;
        }
    }

    private void saveFile (FileMessage flMsg) {
        File path = App.callDirChooser();

        if (path != null) {
            flMsg.makeFile(path.getAbsolutePath());
        }
    }

}
