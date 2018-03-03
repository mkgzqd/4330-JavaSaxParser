/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javasaxparser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author gambi
 */
public class XMLStackParser {
    private static XMLNode root=null;
    private static Stack<XMLNode> stack = new Stack<>();
    private static XMLNode currentNode;
    
    private static String CurrentElementName="";
    
    public static XMLNode parse(File file) throws Exception{
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            
            DefaultHandler handler = new DefaultHandler(){
               
                 @Override
                 public void startElement(String namespaceURI,String localName,String qName, Attributes atts)throws SAXException {
                    XMLNode node = new XMLNode();
                    node.name = qName;
                    
                    CurrentElementName+=node.name + "\n";
                    
                    node.attributes = new HashMap();
                    
                    for(int i=0; i <atts.getLength(); i++){
                        node.attributes.put(atts.getQName(i), atts.getValue(i));
                    }
                    
                    stack.push(node);
                    
                    if(currentNode!=null){
                        if(currentNode.properties!=null){
                            currentNode.properties.add(node);
                        }
                    }
                    
                    currentNode = node;
                }
                
                public void endElement(String namespaceURI, String qName) throws SAXException{
                    XMLNode popped = stack.pop();
                    
                  if(stack.empty()){
                      root=popped;
                      currentNode=null;
                  }else{
                      currentNode = stack.lastElement();
                  }
                }
                
                 @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    currentNode.content = new String(ch, start, length);  
                    CurrentElementName += currentNode.content + "\n";
                }
            };
            
            saxParser.parse(file.getAbsoluteFile(), handler);
            
        }catch(Exception e){
            throw e;
        }
        return root;
    }
    
    public static String createText(){
        return XMLStackParser.CurrentElementName;
        //return "Hello";
    }
    
}
