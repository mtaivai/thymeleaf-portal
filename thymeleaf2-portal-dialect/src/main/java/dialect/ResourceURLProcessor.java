package dialect;

import javax.portlet.BaseURL;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

public class ResourceURLProcessor extends AbstractBaseURLProcessor {
	private static final Logger log = LoggerFactory.getLogger(ResourceURLProcessor.class);

	private static final String ATTR_CACHEABILITY = "cacheability";
	private static final String ATTR_RESOURCE_ID = "id";
	
	protected ResourceURLProcessor() {
		super("resource-url");
	}

	@Override
	protected BaseURL createBaseURL(final RenderResponse renderResponse, final Arguments arguments, final Element element) {
		final ResourceURL url = renderResponse.createResourceURL();
		forAttributeValue(element, ATTR_CACHEABILITY, (val) -> {
			// FULL, PORTLET, PAGE
			final String cacheLevel = val.trim().toUpperCase();
			url.setCacheability(cacheLevel);
			
		});
		
		forAttributeValue(element, ATTR_RESOURCE_ID, (val) -> {
			final String resourceID = val.trim();
			url.setResourceID(resourceID);
			
		});
		
		return url;
		
	}
	

}
