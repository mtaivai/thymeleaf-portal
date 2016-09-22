package dialect;

import javax.portlet.ActionRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.standard.processor.attr.AbstractStandardSingleAttributeModifierAttrProcessor;

import dialect.util.PortletUtil;

public class ActionAttrProcessor extends AbstractStandardSingleAttributeModifierAttrProcessor {

    public static final int ATTR_PRECEDENCE = 1000;
    public static final String ATTR_NAME = "action";

    public ActionAttrProcessor() {
        super(ATTR_NAME);
    }
    
    @Override
    public int getPrecedence() {
        return ATTR_PRECEDENCE;
    }

    @Override
    protected String getTargetAttributeName(
            final Arguments arguments, final Element element, final String attributeName) {
        return ATTR_NAME;
    }

    
    @Override
    protected ModificationType getModificationType(
            final Arguments arguments, final Element element, final String attributeName, final String newAttributeName) {
        return ModificationType.SUBSTITUTION;
    }

    @Override
    protected boolean removeAttributeIfEmpty(
            final Arguments arguments, final Element element, final String attributeName, final String newAttributeName) {
        return false;
    }

    private static PortletResponse getPortletResponse(final Arguments arguments, final Element element) {
		// TODO is it already set, try to evaluate
		return PortletUtil.getPortletResponse(arguments);
	}
    
    @Override
    protected String getTargetAttributeValue(
            final Arguments arguments, final Element element, final String attributeName) {

    	final String attributeValue = element.getAttributeValue(attributeName);

    	final RenderResponse rr = (RenderResponse)getPortletResponse(arguments, element);
    	final PortletURL url = rr.createActionURL();
    	url.setParameter(ActionRequest.ACTION_NAME, attributeValue);
    	
        return url.toString();
    }

    
}