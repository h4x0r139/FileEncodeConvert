package org.h4x0r.util;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class CharDetector {
    
//    public static void main(String[] args) {
//        String charset = null;
//        charset = CharDetector.detectFileEncode("E:\\test\\new\\MyTaoBao\\src\\cn\\android\\mytaobao\\activity\\MainActivity.java");
//        System.out.println("charset="+charset);
//        charset = CharDetector.detectFileEncode("E:\\test\\MyTaoBao\\src\\cn\\android\\mytaobao\\activity\\MainActivity.java");
//        System.out.println("charset="+charset);
//    }
    
    /** 
     * 利用第三方开源包cpdetector获取文件编码格式. 
     * 
     * @param filePath 
     * @return 
     */ 
   public static String detectFileEncode(String filePath) { 
     /** 
      * <pre> 
      * 1、cpDetector内置了一些常用的探测实现类,这些探测实现类的实例可以通过add方法加进来,如:ParsingDetector、 JChardetFacade、ASCIIDetector、UnicodeDetector. 
      * 2、detector按照“谁最先返回非空的探测结果,就以该结果为准”的原则. 
      * 3、cpDetector是基于统计学原理的,不保证完全正确. 
      * </pre> 
      */ 
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        detector.add(new ParsingDetector(false));
        detector.add(JChardetFacade.getInstance());// 需要第三方JAR包:antlr.jar、chardet.jar.
        detector.add(ASCIIDetector.getInstance());
        detector.add(UnicodeDetector.getInstance());
        Charset charset = null;
        File file = new File(filePath);
        try {
            charset = detector.detectCodepage(file.toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String charsetName = "GBK";
        if (charset != null) {
            if (charset.name().equals("US-ASCII")) {
                charsetName = "ISO_8859_1";
            } else if (charset.name().startsWith("UTF")) {
                charsetName = charset.name();// 例如:UTF-8,UTF-16BE.
            }
        }
        return charsetName;
   } 

    @Deprecated
    //有问题
    public static String detectGBK_UTF8(String filePath) throws IOException {
        
        File file = new File(filePath);  
        InputStream in= new java.io.FileInputStream(file);  
        byte[] b = new byte[3];  
        in.read(b);  
        in.close();  
        if (b[0] == -17 && b[1] == -69 && b[2] == -65)  {//如果UTF-8 是无BOM格式，则没有这个字节
            System.out.println(file.getName() + "：编码为UTF-8");  
            return "UTF-8";
        } else {
            System.out.println(file.getName() + "：可能是GBK，也可能是其他编码");  
            return "GBK";
        }
    }
    
    
    
   
 
}
