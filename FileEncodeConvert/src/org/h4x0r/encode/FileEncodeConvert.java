package org.h4x0r.encode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.h4x0r.data.ConvertConfig;
import org.h4x0r.util.CharDetector;
import org.h4x0r.view.EncodeCovertView;


// 文件编码格式转换
//todo 自动检测文件类型
public class FileEncodeConvert {
    

    private ConvertConfig convertConfig;
    public ConvertConfig getConvertConfig() {
        return convertConfig;
    }
    public void setConvertConfig(ConvertConfig convertConfig) {
        this.convertConfig = convertConfig;
    }
    public FileEncodeConvert() {
       super();
    }
    public FileEncodeConvert(ConvertConfig config) {
        this.convertConfig = config;
    }

    public void doConvert() {

         List<String> files = new ArrayList<String>();
         fetchFileList(convertConfig.getSourceFileUrl(), files, convertConfig.getFilter());
         for(String fileName : files){
             String destFileUrl = getDestFileUrl(fileName, convertConfig.getSourceFileUrl(), 
                     convertConfig.getDestFileUrl());
             convert(fileName, convertConfig.getSourceEncode(), destFileUrl, convertConfig.getDestEncode());
         }
         EncodeCovertView.writeResult("转换成功！");
        
    }
    
    public static String getDestFileUrl(String fileAbsoluteUlr, String srcUlr, String destUrl) {
        if (destUrl == null || destUrl.trim().equals("")) {
            return fileAbsoluteUlr;
        }
        //计算目标路径
        StringBuffer sBuffer = new StringBuffer(destUrl);
//        sBuffer.append(File.separator);
        //Windows盘符转换为大写，或者去掉
        fileAbsoluteUlr = fileAbsoluteUlr.substring(fileAbsoluteUlr.lastIndexOf(":")+1);
        srcUlr = srcUlr.substring(srcUlr.lastIndexOf(":")+1);
        System.out.println("srcUlr="+srcUlr+"\nfileAbsoluteUlr="+fileAbsoluteUlr);
        if(fileAbsoluteUlr.startsWith(srcUlr)) {//截取相对当前扫描路径的相对路径
            sBuffer.append(srcUlr.substring(srcUlr.lastIndexOf("\\")));
            sBuffer.append(fileAbsoluteUlr.substring(srcUlr.length()));
        }
        return sBuffer.toString();
    }


    public static void changeEncoding(File sourceFile, String sourceEncode, File destFile, String destEncode) {
        // 输入输出流
        FileInputStream fins = null;
        FileOutputStream fous = null;
        try {
            fins = new FileInputStream(sourceFile);
            fous = new FileOutputStream(destFile);
            FileChannel fcin = fins.getChannel();
            FileChannel fcou = fous.getChannel();
            int flag = 0;
            // 设置缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            while ((flag = fcin.read(buffer)) != -1) {// 需要将源ByteBuffer
                                                      // —GBK(2字节)—》目标
                                                      // CharBuffer：—UTF-8(3字节)—》ByteBuffer
            // fcou.write(ByteBuffer.wrap("这是一句中文".getBytes("GBK")));
                fcou.write(ByteBuffer
                        .wrap(Charset.forName(sourceEncode).decode(buffer).toString().getBytes(destEncode)));
                buffer.clear();
            }
            fcou.close();
            fcin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fous != null) {
                try {
                    fous.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fins != null) {
                try {
                    fins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void convert(String oldFile, String oldCharset, String newFlie, String newCharset) {
        BufferedReader bin;
        FileOutputStream fos;
        StringBuffer content = new StringBuffer();
        try {
            System.out.println(oldFile);
            bin = new BufferedReader(new InputStreamReader(new FileInputStream(oldFile), 
                    CharDetector.detectFileEncode(oldFile)));
            String line = null;
            while ((line = bin.readLine()) != null) {
                // System.out.println("content:" + content);
                content.append(line);
                content.append(System.getProperty("line.separator"));
            }
            bin.close();
            File dir = new File(newFlie.substring(0, newFlie.lastIndexOf("\\")));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fos = new FileOutputStream(newFlie);
            Writer out = new OutputStreamWriter(fos, newCharset);
            out.write(content.toString());
            out.close();
            fos.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        EncodeCovertView.writeResult(newFlie);
    }

    public static void fetchFileList(String strPath, List<String> filelist, final String regex) {
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        Pattern p = null;
        if (regex == null || regex.trim().equals("")) {
            p = Pattern.compile(regex);
        }
        if (files == null)
            return;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                fetchFileList(files[i].getAbsolutePath(), filelist, regex);
            } else {
                String strFileName = files[i].getAbsolutePath();
                if (p == null) {
                    filelist.add(strFileName);
                } else {
                    Matcher m = p.matcher(strFileName);
                    if (m.find()) {
                        filelist.add(strFileName);
                    }  
                }
                
            }
        }
    }
    
//    public static void main(String[] args) {
//        System.out.println(FileEncodeConvert.getDestFileUrl("E:\\test\\MyTaoBao\\src\\cn\\android\\mytaobao\\activity\\MainActivity.java", 
//                "e:\\test\\MyTaoBao", "E:\\test\\new"));
//    }

}
