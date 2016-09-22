package dialect;

import javax.portlet.BaseURL;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

abstract class AbstractPortletURLProcessor extends AbstractBaseURLProcessor {
	static final String ATTR_WINDOW_STATE = "windowState";
	static final String ATTR_PORTLET_MODE = "portletMode";
	
	
	
	protected AbstractPortletURLProcessor(final String elementName) {
		super(elementName);
	}
	
	protected abstract PortletURL createPortletURL(final RenderResponse renderResponse, final Arguments arguments, final Element element);
	
	
	@Override
	protected final BaseURL createBaseURL(final RenderResponse renderResponse, final Arguments arguments, final Element element) {
		PortletURL url = createPortletURL(renderResponse, arguments, element);
		initPortletURL(url, arguments, element);
		return url;
	}
	
	
	private final void initPortletURL(final PortletURL url, final Arguments arguments, final Element element) {
		forAttributeValue(element, ATTR_WINDOW_STATE, (val) -> {
			final String windowStateName = val.trim().toLowerCase();
			final WindowState windowState = new WindowState(windowStateName);
			try {
				url.setWindowState(windowState);
			} catch (WindowStateException e) {
				throw new RuntimeException(e);
			}
		});
		
		forAttributeValue(element, ATTR_PORTLET_MODE, (val) -> {
			final PortletMode portletMode = new PortletMode(val.trim().toLowerCase());
			try {
				url.setPortletMode(portletMode);
			} catch (PortletModeException e) {
				throw new RuntimeException(e);
			}
		});
		
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
	
}