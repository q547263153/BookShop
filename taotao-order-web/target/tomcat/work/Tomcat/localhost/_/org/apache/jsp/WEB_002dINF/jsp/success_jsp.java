/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2020-03-12 03:29:44 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class success_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
      out.write("  <head>\r\n");
      out.write("   <meta http-equiv=\"pragma\" content=\"no-cache\" />\r\n");
      out.write("   <meta http-equiv=\"cache-control\" content=\"no-cache\" />\r\n");
      out.write("   <meta http-equiv=\"expires\" content=\"0\" /> \r\n");
      out.write("   <meta name=\"format-detection\" content=\"telephone=no\" />  \r\n");
      out.write("   <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\" /> \r\n");
      out.write("   <meta name=\"format-detection\" content=\"telephone=no\" />\r\n");
      out.write("   <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n");
      out.write("   <link type=\"text/css\" rel=\"stylesheet\" href=\"/css/base.css\" />\r\n");
      out.write("   <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/purchase.base.2012.css\" />\r\n");
      out.write("   <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/purchase.sop.css\" />\r\n");
      out.write("   <title>订单成功页面 - 淘淘商城</title>\r\n");
      out.write("   <script type=\"text/javascript\" src=\"/js/jquery-1.6.4.js\"></script>\r\n");
      out.write("   <script type=\"text/javascript\" src=\"/js/base-2011.js\" charset=\"utf-8\"></script>\r\n");
      out.write("   <script type=\"text/javascript\" src=\"/js/jquery.cookie.js\" charset=\"utf-8\"></script>\r\n");
      out.write("   <script type=\"text/javascript\" src=\"/js/taotao.js\" charset=\"utf-8\"></script>\r\n");
      out.write("</head> <body id=\"mainframe\">\r\n");
      out.write("<!--shortcut start-->\r\n");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "commons/shortcut.jsp", out, false);
      out.write("<!--shortcut end-->\r\n");
      out.write("<div class=\"w\" id=\"headers\">\r\n");
      out.write("\t\t<div id=\"logo\"><a href=\"/\"><img alt=\"淘淘商城\" src=\"/images/taotao-logo.gif\"></a></div>\r\n");
      out.write("\t\t<ul class=\"step\" id=\"step3\">\r\n");
      out.write("\t\t\t<li class=\"fore1\">1.我的购物车<b></b></li>\r\n");
      out.write("\t\t\t<li class=\"fore2\">2.填写核对订单信息<b></b></li>\r\n");
      out.write("\t\t\t<li class=\"fore3\">3.成功提交订单</li>\r\n");
      out.write("\t\t</ul>\r\n");
      out.write("\t\t<div class=\"clr\"></div>\r\n");
      out.write("</div>\r\n");
      out.write("<div class=\"w\" id=\"safeinfo\"></div><!--父订单的ID-->\r\n");
      out.write("<div class=\"w main\">\r\n");
      out.write("\t<div class=\"m m3 msop\">\r\n");
      out.write("        <div class=\"mt\" id=\"success_tittle\"><s class=\"icon-succ02\"></s><h3 class=\"ftx-02\">感谢您，订单提交成功！</h3>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<div class=\"mc\" id=\"success_detail\">\t\r\n");
      out.write("\t\t    <ul class=\"list-order\">\r\n");
      out.write("\t\t\t    <li class=\"li-st\">\r\n");
      out.write("\t\t\t\t\t<div class=\"fore1\">订单号：<a href=\"javascript:void(0)\">");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${orderId }", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("</a></div>\r\n");
      out.write("\t\t\t\t\t<!-- 货到付款 -->\r\n");
      out.write("\t\t\t\t\t<div class=\"fore2\">货到付款：<strong class=\"ftx-01\">");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${payment}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write("元</strong></div>\r\n");
      out.write("\t\t\t\t\t<div class=\"fore3\">\r\n");
      out.write("\t\t\t\t\t   \t淘淘快递 &nbsp; 送货时间: 预计 ");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${date}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null, false));
      out.write(" 送达&nbsp;\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t</li>\r\n");
      out.write("\t\t\t</ul>\r\n");
      out.write("\t\t<!-- 在线支付按钮  -->\r\n");
      out.write("\t\t\t\t<div id=\"bookDiv\"></div>\r\n");
      out.write(" \t\t\t\t\t<p class=\"i-tips01\">\r\n");
      out.write("\t\t\t\t            \t您的订单已经在处理中，发货后订单内容会显示承运人联系方式，如有必要您可以联系对方\r\n");
      out.write("             \t\t</p>\r\n");
      out.write("\t\t </div>\r\n");
      out.write("\t</div>\r\n");
      out.write("</div>\r\n");
      out.write("  <div class=\"w\">\r\n");
      out.write("\t<!-- links start -->\r\n");
      out.write("    ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "commons/footer.jsp", out, false);
      out.write("<!-- links end -->\r\n");
      out.write("</div><!-- footer end -->\r\n");
      out.write("     </body> \r\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
