package zad1;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

public class BundleInfo {
	
	static private String[] commandParamNames;
	static private String[] commandParamDescr;
	static private String[] statusMsg;
	static private String[] headers;
	static private String[] footers;
	static private String[] resultDescr;
	static private String charset;
	static private String submitMsg;
	
	static void generateInfo(ResourceBundle rb)
	{
		synchronized(BundleInfo.class)
		{
			List cpn = new ArrayList();
			List cpv = new ArrayList();
			Enumeration keys = rb.getKeys();
			while(keys.hasMoreElements())
			{
				String key = (String) keys.nextElement();
				if(key.startsWith("param_")){
					cpn.add(key.substring(6));
					cpv.add(rb.getString(key));
				}
				else if (key.equals("header")) headers = rb.getStringArray(key);
		        else if (key.equals("footer")) footers = rb.getStringArray(key);
		        else if (key.equals("resCode")) statusMsg = rb.getStringArray(key);
		        else if (key.equals("resDescr")) resultDescr = rb.getStringArray(key);
		        else if (key.equals("charset")) charset = rb.getString(key);
		        else if (key.equals("submit")) submitMsg = rb.getString(key);
			}
			
			commandParamNames = (String[]) cpn.toArray(new String[0]);
		    commandParamDescr = (String[]) cpv.toArray(new String[0]);	    
		}	
	}
	
	public static String getCharset() {
		return charset;
	}

	public static String getSubmitMsg() {
		return submitMsg;
	}

	public static String[] getCommandParamNames() {
		return commandParamNames;
	}

	public static String[] getCommandParamDescr() {
		return commandParamDescr;
	}

	public static String[] getStatusMsg() {
		return statusMsg;
	}

	public static String[] getHeaders() {
		return headers;
	}

	public static String[] getFooters() {
		return footers;
	}

	public static String[] getResultDescr() {
		return resultDescr;
	}
}
