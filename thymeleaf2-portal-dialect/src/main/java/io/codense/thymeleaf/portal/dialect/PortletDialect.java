package io.codense.thymeleaf.portal.dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

public class PortletDialect extends AbstractDialect {

	@Override
	public String getPrefix() {
		return "portlet";
	}

	@Override
	public Set<IProcessor> getProcessors() {
		Set<IProcessor> processors = new HashSet<>();
		processors.add(new ActionURLProcessor());
		processors.add(new RenderURLProcessor());
		processors.add(new ResourceURLProcessor());
		processors.add(new ParamProcessor());
		
		processors.add(new ActionAttrProcessor());
		
		return processors;
	}

	
}
