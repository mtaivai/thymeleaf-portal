package dialect;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

public class ActionURLProcessor extends AbstractPortletURLProcessor {
	private static final Logger log = LoggerFactory.getLogger(ActionURLProcessor.class);

	protected ActionURLProcessor() {
		super("action-url");
	}

	@Override
	protected PortletURL createPortletURL(RenderResponse renderResponse, Arguments arguments, Element element) {
		return renderResponse.createActionURL();
	}
	
	

}
