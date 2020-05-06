package app.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class authCheckFilter implements Filter {
    List<String> ignoreUrl = List.of("/auth", "/registration", "/password", "logout", "/", "/resources");
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getServletPath();
        if (!"/resources".equals(path)){
            //response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
        }
        if ( ignoreUrl.contains(path)){
            filterChain.doFilter(request, response);
            return;
        }
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("/auth");
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
