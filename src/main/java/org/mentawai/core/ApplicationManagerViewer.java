package org.mentawai.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

/**
 * Show application manager stats
 * @author Fernando Boaglio
 *
 */
public class ApplicationManagerViewer {

	public static String STATS_PAGE_NAME= "stats";

	private String STATS_VERSION= "1.1";

	private ApplicationManager applicationManager = ApplicationManager.getInstance();

	public void buildApplicationManagerStats(HttpServletResponse res) throws IOException {

        res.setContentType("text/html");

        PrintWriter out = res.getWriter();

        Set<Filter> allFilters = applicationManager.getAllFilters();
        List<Filter> allGlobalFilters = applicationManager.getGlobalFilters();
        Map<String, ActionConfig> actions = applicationManager.getActions();

        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        out.println("<head>");
        out.println("	<meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-1\"/>");
        out.println("	<title>ApplicationManager Statistics "+STATS_VERSION+"</title>");
        out.println("	<link rel=\"stylesheet\" href=\"dyntags/stats/jquery.css\" />");
        out.println("	<link rel=\"stylesheet\" href=\"dyntags/stats/screen.css\" />");
        out.println("	<script src=\"dyntags/stats/jquery.js\" type=\"text/javascript\"></script>");
        out.println("	<script src=\"dyntags/stats/jquery_treeview.js\" type=\"text/javascript\"></script>");
        out.println("	<script type=\"text/javascript\">");
        out.println("	$(document).ready(function(){");
        out.println("		$(\"#browser\").treeview({");
        out.println("			toggle: function() {");
        out.println("				console.log(\"%s was toggled.\", $(this).find(\">span\").text());");
        out.println("			}");
        out.println("		});");
        out.println("	});");
        out.println("	</script>");
        out.println("	</head>");
        out.println("	<body>");
        out.println("	<h1 id=\"banner\">Mentawai Web Framework</h1>");
        out.println("	<div id=\"main\">");
        out.println("	<b>ApplicationManager Statistics "+STATS_VERSION+"</b>");
        out.println("	<ul id=\"browser\" class=\"filetree treeview-famfamfam\">");
        out.println("		<li><span class=\"folder\">Filters ["+applicationManager.getAllFilters().size()+"]</span>");
        out.println("			<ul>");

                for(Filter filter :  allFilters) {
                    out.println("<li><span class=\"file\">"+filter + "</span></li>");
                   }

        out.println("			</ul>");
        out.println("		</li>");
        out.println("		<li><span class=\"folder\">Global Filters ["+applicationManager.getGlobalFilters().size()+"]</span>");
        out.println("			<ul>");

                for(Filter filter :  allGlobalFilters) {
                	out.println("<li><span class=\"file\">"+filter + "</span></li>");
                  }

        out.println("			</ul>");
        out.println("		</li>");
        out.println("		<li><span class=\"folder\">Actions ["+applicationManager.getActions().size()+"]</span>");
        out.println("			<ul>");

                for(String action:  actions.keySet()) {
                    out.println("<li><span class=\"file\">"+action  + "</span></li>");
                   }

                out.println("	        </ul>");
                out.println("		</li>");
        out.println("	</ul>");
        out.println("</div>");
        out.println("</body></html>");

	}


}
