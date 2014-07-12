<%-- 
    Document   : userInfo
    Created on : 2014/07/09, 22:33:34
    Author     : Yuichiro
--%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <!-- Style fragment -->
        <%@include file="/WEB-INF/jspf/style.jspf" %>
        <title><spring:message code="app.name" /> ${id}</title>
    </head>
    <body>
        <!-- Header fragment -->
        <%@include file="/WEB-INF/jspf/header.jspf" %>
        
        <div id="main">
            <h2><spring:message code="userupdate.title" /></h2>
            <form action="/archelon/userupdate" method="post">
                <p><spring:message code="user.profile" /></p>
                <textarea wrap="off" type="text" name="user_profile" id="discussion_field"></textarea>
                <table>
                    <c:forEach var="item" items="${inputform_list}">
                        <tr>
                            <td><c:out value="${item.field}"/></td>
                            <td><input type="text" name="<c:out value="${item.field}"/>" size="30" value="<c:out value="${item.entity}"/>"></td>
                        </tr>
                    </c:forEach>
                </table>
                <input type="submit" value="submit" name="submit">
            </form>
        </div>

        <!-- Footer fragment -->
        <%@include file="/WEB-INF/jspf/footer.jspf" %>
    </body>
</html>