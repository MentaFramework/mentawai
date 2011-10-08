<%@tag import="org.mentawai.template.*" pageEncoding="UTF-8"%>
<%@attribute name="id" required="true"%>

<%
  Page page = (Page)request.getAttribute(TemplateServlet.PAGE_ATTR);
  Page block = page.getBlock(id);
  String view = "";
  if (block == null) {
	  throw new ServletException("Block " + id + " doesn't exists");
  }
  request.setAttribute(TemplateServlet.PAGE_ATTR, block);
  view = block.getView();
  TemplateServlet.executeListener(block, request, response, application);
  
  String oldView = (String)request.getAttribute("_view");
  request.setAttribute("_view", "/" + view);
%>

<jsp:include page="${_view}" flush="true"/>

<%
  request.setAttribute("_view", oldView);
  request.setAttribute(TemplateServlet.PAGE_ATTR, page);
%>