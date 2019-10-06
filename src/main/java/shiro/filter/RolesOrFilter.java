package shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * 实现自定义的Filter
 * 只要传入的角色其中一个被满足了就给与授权
 */
public class RolesOrFilter extends AuthorizationFilter {

    /**
     * @param servletRequest
     * @param servletResponse
     * @param o  就是传入的require角色数组
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, Object o) throws Exception {
//        父类中有getSubject方法
        Subject subject = getSubject(servletRequest, servletResponse);
        String[] roles = (String[]) o;
//        没有要求直接返回true
        if (roles==null||roles.length==0){
            return true;
        }
//        任意一个满足了就返回true
        for(String role:roles){
            if(subject.hasRole(role)){
                return true;
            }
        }
        return false;
    }
}
