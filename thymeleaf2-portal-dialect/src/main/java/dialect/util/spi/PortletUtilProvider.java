package dialect.util.spi;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.thymeleaf.Arguments;

public interface PortletUtilProvider {

	PortletRequest getPortletRequest(final Arguments arguments);
	PortletResponse getPortletResponse(final Arguments arguments);
	
	void setExpressionRootVariable(final String name, final Object value, final Arguments arguments);
}
