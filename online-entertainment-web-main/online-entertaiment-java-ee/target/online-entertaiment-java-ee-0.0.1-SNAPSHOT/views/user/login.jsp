<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đăng nhập</title>
    <%@include file="/common/head.jsp" %>
</head>

<body>
    <%@include file="/common/header.jsp" %>
    <div class="container-fluid">
        <div class="row py-3">
            <div class="col-12 mb-5">
                <h2 class="tm-text-primary mb-3 text-uppercase"><fmt:message key='index.login'/></h2>
                <form id="login-form" action="login" method="POST" style="margin: 75px 0"
                    class="tm-contact-form mx-auto p-4 border border-success rounded bg-light bg-opacity-25">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger py-1" id="login-message" role="alert">
                            ${errorMessage}
                        </div>
                    </c:if>
                    <div class="form-floating mb-3">
                        <input type="text" class="form-control" name="username" id="username" placeholder="Username"
                            value="${username}" />
                        <label for="username"><fmt:message key='index.username'/></label>
                        <div class="form-text form-message text-danger"></div>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="password" class="form-control" name="password" id="password" placeholder="Password"
                            value="${password}" />
                        <label for="password"><fmt:message key='index.password'/></label>
                        <div class="form-text form-message text-danger"></div>
                    </div>
                    <div class="d-flex justify-content-between">
                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="remember-me" name="remember"
                                ${rememberChecked} />
                            <label class="form-check-label" for="remember-me"><fmt:message key='index.rememberMe'/></label>
                        </div>
                        <a data-bs-toggle="modal" href="#forgot-password-modal" class="link-success"><fmt:message key='index.forgotPassword'/>?</a>
                    </div>
                    <button type="submit" id="btn-login" class="btn btn-success w-100"><fmt:message key='index.login'/></button>
                </form>
            </div>
        </div>
    </div>
    <!-- container-fluid, tm-container-content -->
    <%@include file="/common/footer.jsp" %>
</body>

</html>