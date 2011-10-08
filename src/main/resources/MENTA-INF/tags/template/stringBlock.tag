<%@tag import="org.mentawai.template.*" pageEncoding="UTF-8"%>
<%@attribute name="id" required="true"%>

<%
  Page page = (Page)request.getAttribute(TemplateServlet.PAGE_ATTR);
  String stringBlock = page.getStringBlock(id);
%>
<%=stringBlock%>


