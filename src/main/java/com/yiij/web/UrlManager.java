package com.yiij.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yiij.base.ApplicationComponent;
import com.yiij.base.Exception;
import com.yiij.base.interfaces.IContext;
import com.yiij.base.interfaces.IWebApplication;

public class UrlManager extends ApplicationComponent
{
	public static final String GET_FORMAT="get";
	public static final String PATH_FORMAT="path";
	
	public String routeVar = "r";
	public boolean caseSensitive = true; 
	
	private IWebApplication _webApplication;
	private String _urlFormat = UrlManager.GET_FORMAT;
	private String _baseUrl;
	
	public UrlManager(IContext context)
	{
		super(context);
		if (!(context.getApplication() instanceof IWebApplication))
			throw new Exception("UrlManager only works with IWebApplication");
		_webApplication = (IWebApplication)context.getApplication();
	}
	
	@Override
	public void init()
	{
		super.init();
		//processRules();
	}

	/**
	 * Parses the user request.
	 * @param CHttpRequest $request the request application component
	 * @return string the route (controllerID/actionID) and perhaps GET parameters in path format.
	 */
	public String parseUrl(HttpRequest request)
	{
		if (getUrlFormat() == UrlManager.PATH_FORMAT)
		{
			return "";
		}
		else if (request.getParam(routeVar, null) != null)
		{
			return request.getParam(routeVar);
		}
		else
			return "";
	}	
	
	public void parsePathInfo(String pathInfo) throws java.lang.Exception
	{
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher m;
		
		if(pathInfo.equals(""))
			return;
		String []segs = (pathInfo+"/").split("/");
		int n = segs.length;
		for(int i=0; i < n-1; i += 2)
		{
			String key = segs[i];
			if(key.equals("")) continue;
			String value = segs[i+1];
			m=pattern.matcher(key);
			int pos;
			if((pos = key.indexOf("["))!=-1 && m.find())
			{
				// TODO array params
				/*
				String name = key.substring(0, pos+1); // substr($key,0,$pos);
				for(int j = m.groupCount() -1 ; j >= 0; --j)
				{
					if($matches[1][$j]==='')
						$value=array($value);
					else
						$value=array($matches[1][$j]=>$value);
				}
				if(isset($_GET[$name]) && is_array($_GET[$name]))
					$value=CMap::mergeArray($_GET[$name],$value);
				$_REQUEST[$name]=$_GET[$name]=$value;
				*/
			}
			else
				((WebApplication)context().getApplication()).getRequest().setParam(key, value);
		}		
	}
	
	public String getUrlFormat()
	{
		return _urlFormat;
	}
	
	public void setUrlFormat(String value)
	{
		if(value.equals(UrlManager.PATH_FORMAT) || value.equals(UrlManager.GET_FORMAT))
			_urlFormat = value;
		else
			throw new com.yiij.base.Exception("CUrlManager.UrlFormat must be either 'path' or 'get'.");
	}
}
