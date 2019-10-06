package shiro.session.manager;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;


/**
 * 对Session进行管理
 */
public class CustomSessionManager extends DefaultWebSessionManager {

    /**
     * 第一次取到了Session就将它放入Request中
     * 否则会出现多次访问Redis造成效率低下
     * 先从request中取得session取不到再从redis中取
     *
     * @param sessionKey
     * @return
     * @throws UnknownSessionException
     */
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
//        SessionKey是否来自Web，如果来自Web就把Session存入request
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        if (request != null && sessionId != null) {
            Session session = (Session) request.getAttribute(sessionId.toString());
            if (session != null) {
                return session;
            }
        }
        Session session = super.retrieveSession(sessionKey);
        if (request != null && sessionId != null) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }

    /**
     * 避免将Session转换为ShiroHttpSession
     * @return
     */
    @Override
    public boolean isServletContainerSessions() {
        return true;
    }
}
