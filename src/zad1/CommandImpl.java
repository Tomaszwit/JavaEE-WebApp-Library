package zad1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommandImpl implements Command, Serializable {
	
	private Map parameterMap = new HashMap();
	private List resultList = new ArrayList();

	private int statusCode;
	
	public CommandImpl(){}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setParameter(String name, Object value) {
		parameterMap.put(name, value);

	}

	@Override
	public Object getParameter(String name) {
		return parameterMap.get(name);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public List getResults() {
		return resultList;
	}
	
	public void addResult(Object o)
	{
		resultList.add(o);
	}
	
	public void addResult(String s)
	{
		addResult(new Object[] { s } );
	}
	
	public void clearResult()
	{
		resultList.clear();
	}

	@Override
	public void setStatusCode(int code) {
		statusCode = code;
	}

	@Override
	public int getStatusCode() {
		return statusCode;
	}

}

