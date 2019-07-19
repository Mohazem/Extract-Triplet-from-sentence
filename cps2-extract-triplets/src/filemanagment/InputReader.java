package filemanagment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.ETInput;

public class InputReader {
	private String FILE_NAME = "";
	private static final int MIN_THRESHOLD = 1;  //ä¸‹çº¿ä¸´ç•Œå€¼
	private static final int MAX_THRESHOLD = 100;  //ä¸Šçº¿ä¸´ç•Œå€¼
	private static ArrayList<String>  list = new ArrayList<String>();
	
	public InputReader(String file_name) {
		// TODO Auto-generated constructor stub
		this.FILE_NAME = file_name;
	}
	
	// function to read sentences from a given file name
	public ETInput read() {
		ETInput et_input = null;
		/*
		try {
			Scanner in_scanner = new Scanner(new File(FILE_NAME));
			//ArrayList<String> sentences = new ArrayList<>();
			while(in_scanner.hasNextLine()) {
				String str = null;
				try {
		*/
		String str = null;
		try {
 
			InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(FILE_NAME)),Charset.defaultCharset());
			BufferedReader br = new BufferedReader(isr);
 
			StringBuffer sb = new StringBuffer(MAX_THRESHOLD);
 
			while ((str=br.readLine())!=null) {
				//String [] substrs = str.split("ã€‚|\\ï¼Ÿ|\\ï¼�|\\?|\\.|!");

				String regEx="[;?.!]";  //æ­£åˆ™è¡¨è¾¾å¼�.è‹±æ–‡çš„ç»“æ�Ÿç¬¦
				Pattern p =Pattern.compile(regEx);
				Matcher m = p.matcher(str);
 
				String[] substrs = p.split(str);  //æŒ‰ç…§ç»“æ�Ÿç¬¦æ�¥åˆ†å‰²å�¥å­�
 
				//System.out.println(substrs);
	            //å�¥å­�ç»“æ�Ÿç¬¦ä»�ç„¶åœ¨ç›¸åº”çš„æ©˜å­�å�Žä¿�ç•™ç�€
				if(substrs.length > 0)
				{
				    int count = 0;
				    while(count < substrs.length)  
				    {
				        if(m.find())
				        {
				        	substrs[count] += m.group();
				        }
				        count++;
				    }
				}
//	              //String [] substrs = str.split("[ã€‚ï¼Ÿï¼�?.!]");
 				for (int i=0;i<substrs.length;i++) {
 
					if (substrs[i].length()<MIN_THRESHOLD) {	//è¯­å�¥å°�äºŽè¦�æ±‚çš„åˆ†å‰²é•¿åº¦è¦�æ±‚
						sb.append(substrs[i]);
						//sb.append("||");
						if (sb.length()>MIN_THRESHOLD) {
							//System.out.println("A New TU: " + sb.toString());
							list.add(sb.toString());
							sb.delete(0, sb.length());
						}
					}
					else {	//è¯­å�¥æ»¡è¶³è¦�æ±‚çš„åˆ†å‰²ç²’åº¦
							if(sb.length()!=0)	//æ­¤æ—¶å¦‚æžœç¼“å­˜æœ‰å†…å®¹åˆ™åº”è¯¥å…ˆå°†ç¼“å­˜å­˜å…¥å†�å­˜substrs[i]çš„å†…å®¹  ä»¥ä¿�è¯�åŽŸæ–‡é¡ºåº�
							{
								list.add(sb.toString());
								//System.out.println("A New Tu:"+sb.toString());
								sb.delete(0, sb.length());
							}
								list.add(substrs[i]);
								//System.out.println("A New Tu:"+substrs[i]);
					}
				}
			}
			br.close();
			isr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		et_input = new ETInput(list);
	/*
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	*/
		return et_input;
	}	
}
