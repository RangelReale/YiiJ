package com.yiij.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.yiij.base.interfaces.IContext;

public class HttpRequest extends WebApplicationComponent
{
	private String _hostInfo = null;
	private String _baseUrl = null;
	private Map<String, String> _params = new Hashtable<String, String>();
	
	public HttpRequest(IContext context)
	{
		super(context);
	}

	public String getParam(String name)
	{
		return getParam(name, null);
	}
	
	public String getParam(String name, String defaultValue)
	{
		if (_params.containsKey(name))
			return _params.get(name);
		
		String ret = webApp().getServletRequest().getParameter(name);
		if (ret == null)
			ret = defaultValue;
		return ret;
	}

	public void setParam(String name, String value)
	{
		if (value == null)
		{
			if (_params.containsKey(name))
				_params.remove(name);
		}
		else
			_params.put(name, value);
	}

	/*
	public String getParamMap()
	{
		Map<String, String[]> ret = webApp().getServletRequest().getParameterMap();
		
		return ret;
	}
	*/
	
	public String getQuery(String name)
	{
		return getParam(name, null);
	}
	
	public String getQuery(String name, String defaultValue)
	{
		return getParam(name, defaultValue);
	}
	
	public String getPost(String name)
	{
		return getParam(name, null);
	}
	
	public String getPost(String name, String defaultValue)
	{
		return getParam(name, defaultValue);
	}
	
	public String getUrl()
	{
		return webApp().getServletRequest().getRequestURL().toString();
	}

	public String getHostInfo()
	{
		return getHostInfo("");
	}
	
	public String getHostInfo(String schema)
	{
		if (_hostInfo == null)
		{
			boolean secure = getIsSecureConnection();
			String http;
			if(secure)
				http="https";
			else
				http="http";
			if(!webApp().getServletRequest().getLocalName().equals(""))
				_hostInfo=http+"://"+webApp().getServletRequest().getLocalName();
			else
			{
				_hostInfo=http+"://"+webApp().getServletRequest().getServerName();
				int port=secure ? getSecurePort() : getPort();
				if((port!=80 && !secure) || (port!=443 && secure))
					_hostInfo+=':'+port;
			}
			
		}
		
		
		if (!schema.equals(""))
		{
			boolean secure = getIsSecureConnection();
			if(secure && schema.equals("https") || !secure && schema.equals("http"))
				return _hostInfo;

			int port = schema.equals("https") ? getSecurePort() : getPort();
			String portStr;
			if(port!=80 && schema.equals("http") || port!=443 && schema.equals("https"))
				portStr=":"+port;
			else
				portStr="";

			int pos = _hostInfo.indexOf(":");
			return schema+_hostInfo.substring(pos, _hostInfo.indexOf(":", pos+1))+portStr;
			
		}
		else
			return _hostInfo;
	}
	
	public void setHostInfo(String value)
	{
		_hostInfo = StringUtils.stripEnd(value, "/");
	}
	
	public String getBasePath()
	{
		return webApp().getServletConfig().getServletContext().getRealPath("/");
	}
	
	/**
	 * Returns the relative URL for the application.
	 * This is similar to {@link getScriptUrl scriptUrl} except that
	 * it does not have the script file name, and the ending slashes are stripped off.
	 * @return String the relative URL for the application
	 */
	public String getBaseUrl()
	{
		return getBaseUrl(false);
	}
	
	/**
	 * Returns the relative URL for the application.
	 * This is similar to {@link getScriptUrl scriptUrl} except that
	 * it does not have the script file name, and the ending slashes are stripped off.
	 * @param boolean $absolute whether to return an absolute URL. Defaults to false, meaning returning a relative one.
	 * @return String the relative URL for the application
	 */
	public String getBaseUrl(boolean absolute)
	{
		if(_baseUrl == null)
			_baseUrl = webApp().getServletRequest().getContextPath(); //StringUtils.stripEnd(dirname($this->getScriptUrl()),"\\/");
		return absolute ? getHostInfo() + _baseUrl : _baseUrl;
	}
	
	public void setBaseUrl(String value)
	{
		_baseUrl = value;
	}
	
	public String getPathInfo()
	{
		return webApp().getServletRequest().getPathInfo();
	}
	
	public String urldecode(String value)
	{
		try
		{
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return value;
		}
	}
	
	public String getRequestUri()
	{
		return webApp().getServletRequest().getRequestURI();
	}
	
	public String getQueryString()
	{
		return webApp().getServletRequest().getQueryString();
	}
	
	public boolean getIsSecureConnection()
	{
		return webApp().getServletRequest().isSecure();
	}
	
	public String getRequestType()
	{
		return webApp().getServletRequest().getMethod();
	}
	
	public boolean getIsPostRequest()
	{
		return webApp().getServletRequest().getMethod().equals("POST");
	}
	
	public String getServerName()
	{
		return webApp().getServletRequest().getServerName();
	}
	
	public int getServerPort()
	{
		return webApp().getServletRequest().getServerPort();
	}
	
	public String getUrlReferrer()
	{
		return webApp().getServletRequest().getHeader("Referer");
	}
	
	public String getUserAgent()
	{
		return webApp().getServletRequest().getHeader("User-Agent");
	}
	
	public String getUserHostAddress()
	{
		return webApp().getServletRequest().getRemoteAddr();
	}
	
	public String getUserHost()
	{
		return webApp().getServletRequest().getRemoteHost();
	}
	
	public String getAcceptTypes()
	{
		return webApp().getServletRequest().getHeader("Accept");
	}
	
	private Integer _port = null;
	
	public int getPort()
	{
		if (_port == null)
			_port = !getIsSecureConnection() ? webApp().getServletRequest().getServerPort() : 80;
		return _port;
	}
	
	public void setPort(int value)
	{
		_port = value;
		_hostInfo = null;
	}
	
	private Integer _securePort = null;
	
	public int getSecurePort()
	{
		if (_securePort == null)
			_securePort = getIsSecureConnection() ? webApp().getServletRequest().getServerPort() : 443;
		return _securePort;
	}
	
	public void setSecurePort(int value)
	{
		_securePort = value;
		_hostInfo = null;
	}
	
    /**
    *
    * Returns the value of the specified request header
    * as a <code>String</code>. If the request did not include a header
    * of the specified name, this method returns <code>null</code>.
    * If there are multiple headers with the same name, this method
    * returns the first head in the request.
    * The header name is case insensitive. You can use
    * this method with any request header.
    *
    * @param name		a <code>String</code> specifying the
    *				header name
    *
    * @return			a <code>String</code> containing the
    *				value of the requested
    *				header, or <code>null</code>
    *				if the request does not
    *				have a header of that name
    */			
	public String getHeader(String name)
	{
		return webApp().getServletRequest().getHeader(name);
	}
	
    /**
    *
    * Returns an enumeration of all the header names
    * this request contains. If the request has no
    * headers, this method returns an empty enumeration.
    *
    * <p>Some servlet containers do not allow
    * servlets to access headers using this method, in
    * which case this method returns <code>null</code>
    *
    * @return			an enumeration of all the
    *				header names sent with this
    *				request; if the request has
    *				no headers, an empty enumeration;
    *				if the servlet container does not
    *				allow servlets to use this method,
    *				<code>null</code>
    *				
    *
    */
   @SuppressWarnings("rawtypes")
   public Enumeration getHeaderNames()
   {
		return webApp().getServletRequest().getHeaderNames();
   }
	
}
