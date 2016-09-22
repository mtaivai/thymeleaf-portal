package dialect;

//import java.util.function.BiConsumer;

import javax.portlet.ActionRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

public class ParamProcessor extends AbstractElementProcessor {
	private static final Logger log = LoggerFactory.getLogger(ParamProcessor.class);
	
	public static interface ParameterTarget {
		void setParameter(String name, Object value);
	}
	
	protected ParamProcessor() {
		super("param");
	}



	@Override
	public int getPrecedence() {
		return 10000;
	}


	@Override
	public ProcessorResult processElement(final Arguments arguments, final Element element) {

		final Object paramTarget = arguments.getLocalVariable("paramTarget");
		if (paramTarget == null) {
			throw new RuntimeException("No 'paramTarget' found in the context");
		} else if (!(paramTarget instanceof ParameterTarget)) {
			throw new RuntimeException(
					"Expected 'paramTarget' of class ParamProcessor.ParameterTarget but got " + paramTarget.getClass().getName());
		}
		if (!element.hasAttribute("name")) {
			throw new IllegalArgumentException("Attribute 'name' is required on param element");
		}
		if (!element.hasAttribute("value")) {
			throw new IllegalArgumentException("Attribute 'value' is required on param element");
		}
		
		String name = element.getAttributeValue("name").trim();
		if (name.equalsIgnoreCase("action")) {
			name = ActionRequest.ACTION_NAME;
		}
		String value = element.getAttributeValue("value");
		
		((ParameterTarget)paramTarget).setParameter(name, value);
//		final BiConsumer<String, String> c = (BiConsumer<String, String>)paramTarget;
//		c.accept(name, value);
		
		return ProcessorResult.OK;
        
    }
}
