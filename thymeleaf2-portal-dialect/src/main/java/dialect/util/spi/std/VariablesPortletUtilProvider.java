package dialect.util.spi.std;

import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.thymeleaf.Arguments;

import dialect.util.spi.PortletUtilProvider;

class VariablesPortletUtilProvider implements PortletUtilProvider {

	static boolean instanceOf(final Object obj, final Class<?> clazz) {
		return (obj != null && clazz.isAssignableFrom(obj.getClass()));
	}

	static <T> T getObject(final String name, final Class<T> requiredClass, final Arguments arguments) {
		Object obj = arguments.getLocalVariable(name);
		if (instanceOf(obj, requiredClass)) {
			return requiredClass.cast(obj);
		} else {
			@SuppressWarnings("unchecked")
			Map<String, Object> root = (Map<String, Object>)arguments.getExpressionEvaluationRoot();
			obj = root.get(name);
			if (instanceOf(obj, requiredClass)) {
				return requiredClass.cast(obj);
			}
		}
		return null;
	}
	
	public PortletRequest getPortletRequest(final Arguments arguments) {
		return getObject("portletRequest", PortletRequest.class, arguments);
	}
	
	public PortletResponse getPortletResponse(final Arguments arguments) {
		return getObject("portletResponse", PortletResponse.class, arguments);
	}

	@Override
	public void setExpressionRootVariable(final String name, final Object value, final Arguments arguments) {
		//final IContext ctx = arguments.getContext();
		@SuppressWarnings("unchecked")
		Map<String, Object> root = (Map<String,Object>)arguments.getExpressionEvaluationRoot();
		root.put(name, value);
	}

}
