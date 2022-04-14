<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:url value="/assets/user" var="assets"></c:url>
<c:url value="/video" var="urlVideo"></c:url>
<c:url value="/admin" var="urlAdmin"></c:url>
<c:url value="/assets/admin" var="assetsAdmin" ></c:url>

<fmt:setLocale value="${sessionScope.lang}" scope="request"/>
<fmt:setBundle basename="global" scope="request"/>
