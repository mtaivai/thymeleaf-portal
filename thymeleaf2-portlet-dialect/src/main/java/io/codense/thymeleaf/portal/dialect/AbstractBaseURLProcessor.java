package io.codense.thymeleaf.portal.dialect;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.portlet.BaseURL;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderResponse;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import io.codense.thymeleaf.portal.dialect.util.PortletUtil;

abstract class AbstractBaseURLProcessor extends AbstractElementProcessor {
	static final String ATTR_VAR = "var";
	static final String ATTR_SECURE = "secure";
	
	private final String elementName;
	
	static class BaseURLParameterTarget implements ParamProcessor.ParameterTarget {
		private final BaseURL url;
		BaseURLParameterTarget(final BaseURL url) {
			this.url = url;
		} 
		@Override
		public void setParameter(String name, Object value) {
			url.setParameter(name, null != value ? value.toString() : null);
		}
		
	}
	
	protected AbstractBaseURLProcessor(final String elementName) {
		super(elementName);
		this.elementName = elementName;
	}
	
	@Override
	public int getPrecedence() {
		return 10000;
	}
	
	private static void setVariable(final Arguments arguments, final String name, final Object value) {
		
		PortletUtil.setExpressionRootVariable(name, value, arguments);
	}
	
	private static PortletRequest getPortletRequest(final Arguments arguments, final Element element) {
		// TODO is it already set, try to evaluate
		return PortletUtil.getPortletRequest(arguments);
	}
	
	private static PortletResponse getPortletResponse(final Arguments arguments, final Element element) {
		// TODO is it already set, try to evaluate
		return PortletUtil.getPortletResponse(arguments);
	}
	
	protected abstract BaseURL createBaseURL(final RenderResponse renderResponse, final Arguments arguments, final Element element);
	
	final void initBaseURL(final BaseURL url, final Arguments arguments, final Element element) {
		
		forAttributeValue(element, ATTR_SECURE, (val) -> {
			val = val.trim().toLowerCase();
			boolean secure = val.equals("true") || val.equals("yes");
			try {
				url.setSecure(secure);
			} catch (PortletSecurityException e) {
				throw new RuntimeException(e);
			}
		});
	}
	
	@Override
	public final ProcessorResult processElement(final Arguments arguments, final Element element) {

		final String var = element.getAttributeValue(ATTR_VAR);
		if (var == null || var.length() == 0) {
			throw new IllegalArgumentException(
					elementName + " attribute '" + ATTR_VAR + "' is not specified");
		}
		
		final Map<String, Object> newLocalVariables = new HashMap<>();
		
		// TODO Portlet defineObjects
		final PortletResponse portletResponse = getPortletResponse(arguments, element);
		if (portletResponse != null && portletResponse instanceof RenderResponse) {
			final RenderResponse rr = (RenderResponse)portletResponse;
			final BaseURL url = createBaseURL(rr, arguments, element);
			initBaseURL(url, arguments, element);
			
			// TODO PARAMETERS!!!!
			
			setVariable(arguments, var, url);

			// Add target as local variable for nested portlet:param elements
//			final BiConsumer<String, String> paramTarget = url::setParameter;
			final BaseURLParameterTarget paramTarget = new BaseURLParameterTarget(url);
			newLocalVariables.put("paramTarget", paramTarget);
			
			// We may remove the host element, in which case all would lose our
			// local variables too. Therefore we copy 'paramTarget' to all
			// of our children too:
			element.getElementChildren().forEach((e) -> {
				e.addAllNonExistingNodeLocalVariables(newLocalVariables);
			});
			
		}
		
//        final boolean removeHostElement = true;
//        
//        if (removeHostElement) {
		// Remove this element
            element.getParent().extractChild(element);
//        }
        
        return ProcessorResult.setLocalVariables(newLocalVariables);
        
    }

	protected static void forAttributeValue(final Element element, final String attributeName, Consumer<String> valueConsumer) {
		if (element.hasAttribute(attributeName)) {
			final String value = 
					element.getAttributeValue(attributeName);
			valueConsumer.accept(value);
		}
	}
}