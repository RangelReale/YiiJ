package com.yiij.samples.yiij_sample_webapp.admin;

import java.io.IOException;

import com.yiij.base.interfaces.IContext;
import com.yiij.web.Controller;
import com.yiij.web.interfaces.IWebModule;

public class DefaultController extends Controller
{
	public DefaultController(IContext context, String id, IWebModule module)
	{
		super(context, id, module);
	}

	public void actionIndex() throws IOException
	{
		setPageTitle("Admin Index");
		render("index", "sent by DefaultController on AdminModule");
	}
}
