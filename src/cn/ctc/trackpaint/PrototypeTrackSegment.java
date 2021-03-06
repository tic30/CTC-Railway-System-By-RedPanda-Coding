package cn.ctc.trackpaint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

class PrototypeTrackSegment 
{
         boolean hasTrain=false,hasStation=false;
         int x1,length,x2,y1,y2,limit,grade; 
         int timeLit, t, currPass;
         String segName, stationName, nextStop;
        

	public PrototypeTrackSegment(int one,int two,int three, int four,int segLength, int l, int g)
	{
                x1=one; length=segLength;
                x2=two;
                y1=three;
                y2=four;
                limit=l;
                grade=g;
                timeLit= length/limit; //time= # miles /(speed miles/hr)
	}
        
        public void addStation(String s) 
        {
            hasStation=true;
            stationName=s;
        }
        
        public void giveNextStop(String s2) 
        {
            nextStop=s2;
        }
        
        public int getTimeMag() 
        {
            return timeLit;
        }
        
        public String getName() 
        {
            return stationName;
        }
	
	public void draw(Graphics2D g2)
	{
            g2.setStroke(new BasicStroke(4));
            if (!hasTrain)
            {
                g2.setColor(Color.red);
                g2.draw(new Line2D.Float(x1, y1, x2, y2));
            }
            else{
                g2.setColor(Color.blue);
                g2.draw(new Line2D.Float(x1, y1, x2, y2));
            }
            //g2.drawString(segmentName,end-50, 120);
            //g2.setFont(new Font("Times New Roman",Font.PLAIN,15));
            //g2.drawString(new String(limit + "mi/hr"),end-50, 140);
            //g2.drawString(new String(length + "mi"),end-50, 160);
            if (hasStation)
            {
                g2.setFont(new Font("Times New Roman",Font.BOLD,15));
                //g2.drawString(new String(segmentName),end-50, 180);
                g2.setFont(new Font("Times New Roman",Font.PLAIN,15));
            }
            g2.setStroke(new BasicStroke());
	}
}





