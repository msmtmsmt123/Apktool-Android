package per.pqy.apktool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;
import android.os.Bundle;

public class XMLHelper {

	public class XMLFile {
		public File XML = null;
	}
	
	public class XMLTags {
		public String mName="";
		public String mValue="";
		private List<XMLTags> childTags= new ArrayList<XMLTags>();
		private XMLTags fatherTag=null;
		private Bundle attributes=new Bundle();
		
		public XMLTags(String name){
			mName = name;
		}
		
		public void setAttribute(String name,String value){
			if(attributes.keySet().contains(name)){
				attributes.remove(name);
			}
			attributes.putString(name,value);
		}
		
		public String getAttribute(String name){
			return attributes.getString(name);
		}
		
		public void addChildTag(XMLTags tag){
			if(tag.fatherTag!=null){
				tag.fatherTag.removeChildTag(tag);		
			}
			if(!childTags.contains(tag)){
				childTags.add(tag);
			}
			tag.setFatherTag(this);
		}
		
		public void removeChildTag(XMLTags tag){
			if(childTags.remove(tag)){
				tag.setFatherTag(null);
			}
		}
		
		public void setFatherTag(XMLTags fathertag){
			fatherTag=fathertag;
		}
		
	}

	public class XMLTag {
		private int pointer = 0;
		private List<String> tags = null;

		public XMLTag() {
			tags = new LinkedList<String>();
			pointer = 0;
		}

		public XMLTag(String url) {
			tags = new LinkedList<String>();
			pointer = 0;
		}

		public final boolean hasNext() {
			return (pointer < tags.size() - 1) && tags.size() > 0;
		}

		public final boolean hasForward() {
			return pointer > 0 && tags.size() > 0;
		}

		public final String getRoot() {
			pointer = 0;
			return tags.get(0);
		}

		public final String getNext() {
			pointer += 1;
			return tags.get(pointer);
		}

		public final String getForward() {
			pointer -= 1;
			return tags.get(pointer);
		}

		public final int getLocation() {
			return pointer;
		}

		public final void add(String tag) {
			tags.add(tag);
		}

		public final void add(String tag, int loc) {
			tags.add(loc, tag);
		}

		public final void set(String tag, int loc) {
			tags.set(loc, tag);
		}

	}

	public XMLFile openXML(String path) {
		XMLFile xmlF = new XMLFile();
		File xml = new File(path);
		xmlF.XML = xml;
		boolean success = true;
		if (!xml.isFile())
			success = false;
		return success ? xmlF : null;
	}

	public XMLFile createXML(String path) {
		XMLFile xmlF = new XMLFile();
		File xml = new File("/mnt/sdcard","aa.xml");
		xmlF.XML = xml;
		OutputStream outStream = null;
		boolean success = true;
		try {
			xml.createNewFile();
			//xml.setWritable(true);
			outStream = new BufferedOutputStream(new FileOutputStream(xml,false));
			XmlSerializer serializer = Xml.newSerializer();
			serializer.setOutput(outStream, "UTF-8");
			serializer.startDocument("UTF-8", true);
			serializer.endDocument();
			outStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} finally {
			if (outStream != null) {
				try {
					outStream.close();
				} catch (Exception e) {
					success = false;
				}
			}
		}
		return success ? xmlF : null;
	}

	public boolean readXML(XMLFile xml) {
		return true;
	}
	
	private void writeTag(XmlSerializer serializer, XMLTags tag)throws Exception{
		serializer.startTag(null, tag.mName);
		for(int i=0;i<tag.attributes.size();i++){
			String key =(String)tag.attributes.keySet().toArray()[i];
			serializer.attribute(null,key,tag.getAttribute(key));
		}
		serializer.text(tag.mValue);
		for(XMLTags t :tag.childTags){
			writeTag(serializer,t);
		}
		serializer.endTag(null,tag.mName);
	}
	
	public boolean writeXML(XMLFile xml){
		OutputStream outStream = null;
		//boolean success = true;
		try{
			outStream=new BufferedOutputStream(new FileOutputStream(xml.XML));
			XmlSerializer serializer = Xml.newSerializer();
			serializer.setOutput(outStream, "UTF-8");
			serializer.startDocument("UTF-8", true);
			writeTag(serializer, generateXML());
			serializer.endDocument();
			outStream.flush();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				outStream.close();	
			}catch(Exception ex){
			}
		}
		return true;
	}
	
	public XMLTags generateXML() {
		return null;
	}

}
