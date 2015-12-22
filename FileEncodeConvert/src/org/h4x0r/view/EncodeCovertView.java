package org.h4x0r.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.h4x0r.data.ConvertConfig;
import org.h4x0r.encode.FileEncodeConvert;


public class EncodeCovertView extends JFrame{

	JFrame jframe;
	

	JButton openSrcBt, openDestBt, convertBtn;
	JTextField srcUrlTF, destUrlTF, filterFileTF;
	
	JLabel label1, label2, label3, label4, label5,label6;
	//搜索区，为3行2列网格布局
	JPanel panelNorth;
//	JPanel panelSouth;
	
	//编码格式下拉列表
	JComboBox<String> srcEncode, destEncode;
	
	JScrollPane scrollPaneSouth;
	
	static JTextArea outResult;
	
	//打开文件监听
	JFileChooser srcFileChoose, destFileChoose;
	
	public EncodeCovertView() {
		jframe = new JFrame();
		
		
		openSrcBt = new JButton("选择文件");
		openDestBt = new JButton("选择文件");
		convertBtn = new JButton("转换");
		srcUrlTF = new JTextField(30);
		destUrlTF = new JTextField(30);
//		JPanel convertBtnPanel = new JPanel(null);
//		convertBtnPanel.setSize(80, 60);
//		convertBtnPanel.add(convertBtn);
		convertBtn.setPreferredSize(new Dimension(80, 60));

		filterFileTF = new JTextField();
		//结果
		outResult = new JTextArea(20,50);//行，列
		outResult.setLineWrap(true);
		outResult.setWrapStyleWord(true);//换行方式：单词setWrapStyleWord
		scrollPaneSouth = new JScrollPane(outResult);
		
		srcFileChoose = new JFileChooser();
		destFileChoose = new JFileChooser();
		srcFileChoose.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		destFileChoose.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		
		//格式
		srcEncode = new JComboBox<String>(new String[]{"自动检测","UTF-8", "GBK"});
	    destEncode = new JComboBox<String>(new String[]{"UTF-8", "GBK"});
	    srcEncode.setPreferredSize(new Dimension(80,25));
	    destEncode.setPreferredSize(new Dimension(80,25));

		label1 = new JLabel("文件路径");
	    label2 = new JLabel("保存路径");
		label3 = new JLabel("编码格式");
		label4 = new JLabel("");
		label5 = new JLabel("文件类型");
		
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 20, 20);
		jframe.setLayout(flowLayout);
//		panelNorth = new JPanel(flowLayout);
		jframe.setBackground(Color.gray);
		jframe.add(label1);
		jframe.add(srcUrlTF);
		jframe.add(openSrcBt);
	    jframe.add(label3);
	    jframe.add(srcEncode);

		
		jframe.add(label2);
		jframe.add(destUrlTF);
		jframe.add(openDestBt);
	    jframe.add(new JLabel("编码格式"));
	    jframe.add(destEncode);


		
		//事件
	    convertBtn.addActionListener(new ConvertListener());
		OpenFileListener openFileListener = new OpenFileListener();
		openSrcBt.addActionListener(openFileListener);
		openDestBt.addActionListener(openFileListener);
		
//		jframe.add(panelNorth);
		jframe.add(scrollPaneSouth);
	    jframe.add(convertBtn);

		
		jframe.setTitle("文件编码格式转换器 V0.1");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setLocation(400, 300);
		jframe.setSize(730, 600);
		jframe.setResizable(true);//窗口大小是否可改变
		jframe.setVisible(true);

	}
	
	//转换
	class ConvertListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ConvertConfig config = new ConvertConfig();
            config.setSourceFileUrl(srcUrlTF.getText().trim());
            config.setSourceEncode(srcEncode.getSelectedItem().toString());
            config.setDestFileUrl(destUrlTF.getText().trim());
            config.setDestEncode(destEncode.getSelectedItem().toString());
            config.setFilter("");
            FileEncodeConvert convert = new FileEncodeConvert(config);
            convert.doConvert();
        }
	    
	}
	
	//监听打开文件
	class OpenFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (JFileChooser.APPROVE_OPTION == srcFileChoose.showOpenDialog(jframe)) {//选择确认（yes、ok）后返回该值。 
                File file = srcFileChoose.getSelectedFile();
                if (file != null && file.exists()) {
                    JButton jButton = (JButton)e.getSource();
                    if (jButton == openSrcBt) {
                        srcUrlTF.setText(file.getAbsolutePath());
                    } else if (jButton == openDestBt) {
                        destUrlTF.setText(file.getAbsolutePath());
                    } 
                }
            }
        }
	    
	}
	
	//搜索结果写入
    public static  void writeResult(String str) {
        outResult.append(str+"\n");
    }

	/**  
	 * @创建人: yinxm
	 * @时间 : 2015-12-21 下午8:43:20  
	 * @功能 : TODO  
	 * @param args    
	 */
	public static void main(String[] args) {
	    EncodeCovertView view = new EncodeCovertView();
	}

}
