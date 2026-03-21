package dev.dov.tasklist.web.security.expression;

import dev.dov.tasklist.service.UserService;
import org.aopalliance.intercept.MethodInvocation;
import org.jspecify.annotations.Nullable;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class CustomSecurityExceptionHandler extends DefaultMethodSecurityExpressionHandler {

    private ApplicationContext applicationContext;
    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();



    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(@Nullable Authentication authentication,
                                                                              MethodInvocation invocation) {

        CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication);
        root.setTrustResolver(trustResolver);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setRoleHierarchy(getRoleHierarchy());
        root.setUserService(this.applicationContext.getBean(UserService.class));
        return root;

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        this.applicationContext = applicationContext;
    }
}
