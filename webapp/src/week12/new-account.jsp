<%@page import="week12.app.User"%>
<%@page import="week12.app.Account"%>
<%@page import="week12.data.DataAccess"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>ATM Project</title>
    <%
	User curUser = null;
	//request available inside .jsp
	//check if this session set 'user' attribute already (from LoginServlet)
	Object obj = request.getSession().getAttribute("user");
	if( obj instanceof User)
	{
		//with this set we can connect to db later and get this users info
		//so we can display it in html
		curUser = (User)obj;
	}
	else 
	{
		curUser = new User(-1, -1, "", "");
	}
	
	%>
    <style>
        .atmcenter
        {
            text-align: center;
        }
        
        .header
        {
            background-color: lightblue;
            font-size: larger;
            font-style: italic;
            font-family: Verdana;
        }
    </style>
</head>
<body style="height: 100%;">
    <!-- Header -->
    <div class="atmcenter header">
        ATM Project
    </div>

    <!-- Content -->
    <form id="newAccount" method="post" action="NewAccountServlet" style="text-align: center;">
    <hr />
    <div class="atmcenter" >
    <table style="width: 100%; text-align: center;">
        <tr>
            <td class="atmcenter">
                <table style="width: 100%; text-align: center;">
                     <tr class="atmcenter">
                        <td style="text-align: right;width:48%;">
                           Account User:
                        </td>
                        <td style="text-align: left;">
				                   <%
				                        out.print("<select name='userId'>");
				                        out.print("<option value='"+ curUser.getUserId()+"'>"+curUser.getFirstName() + " " + curUser.getLastName()+"</option>");				                        				                
				                        out.print("</select>");
				                    %>
                            <br />
                        </td>
                    </tr>
                    <tr class="atmcenter">
                        <td style="text-align: right;width:48%;">
                            Account Name:
                        </td>
                        <td style="text-align: left;">
                            <input id="accountname" type="text" title="Account name" name="accountname" /><br />
                        </td>
                    </tr>
                    <tr">
                        <td style="text-align: right;width:48%;">
                            Balance:
                        </td>
                        <td style="text-align: left;">
                            <input id="accountbalance" type="text" title="Balance" name="accountbalance" /><br />
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
                <input id="submitme" type="submit" title="Submit" value="Create Account" />
    </div>
    </form>

    <!-- Footer -->
    <div class="atmcenter">
        <hr />
        <span style="font-family: Times New Roman; font-size: small;">Copyright &copy; nsweeney</span>
    </div>
</body>
</html>
