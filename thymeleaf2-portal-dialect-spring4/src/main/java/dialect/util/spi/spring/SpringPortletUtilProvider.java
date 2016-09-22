package dialect.util.spi.spring;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.portlet.context.PortletRequestAttributes;
import org.thymeleaf.Arguments;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.context.SpringWebContext;

import dialect.util.spi.PortletUtilProvider;

class SpringPortletUtilProvider implements PortletUtilProvider {


	
	public PortletRequest getPortletRequest(final Arguments arguments) {
		// TODO is it already set, try to evaluate
		
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		if (attrs instanceof PortletRequestAttributes){
		    PortletRequest request = ((PortletRequestAttributes) attrs).getRequest();
		    return request;
		} else {
			return null;
		}
	}
	
	public PortletResponse getPortletResponse(final Arguments arguments) {
		// TODO is it already set, try to evaluate
		
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		if (attrs instanceof PortletRequestAttributes){
			PortletResponse response = ((PortletRequestAttributes) attrs).getResponse();
		    return response;
		} else {
			return null;
		}
	}
	
	@Override
	public void setExpressionRootVariable(final String name, final Object value, final Arguments arguments) {
		final IContext ctx = arguments.getContext();
		if (ctx instanceof SpringWebContext) {
			SpringWebContext swc = (SpringWebContext)ctx;
			swc.getVariables().put(name, value);
		} else {
			throw new UnsupportedOperationException("IContext is not SpringWebContext");
		}
	}

}
