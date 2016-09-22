package dialect;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

public class RenderURLProcessor extends AbstractPortletURLProcessor {
	private static final Logger log = LoggerFactory.getLogger(RenderURLProcessor.class);

	protected RenderURLProcessor() {
		super("render-url");
	}

	@Override
	protected PortletURL createPortletURL(RenderResponse renderResponse, Arguments arguments, Element element) {
		return renderResponse.createRenderURL();
	}
	

}
