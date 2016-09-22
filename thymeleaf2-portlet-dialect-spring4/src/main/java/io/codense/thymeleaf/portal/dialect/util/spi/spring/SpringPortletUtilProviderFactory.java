package io.codense.thymeleaf.portal.dialect.util.spi.spring;

import io.codense.thymeleaf.portal.dialect.util.spi.AbstractPortletUtilProviderFactory;


public class SpringPortletUtilProviderFactory extends AbstractPortletUtilProviderFactory {

	@Override
	protected int getBasePrecedence() {
		return 2000;
	}
	
	@Override
	protected String[] getProviderClassNames() {
		final String pkgName = SpringPortletUtilProviderFactory.class.getPackage().getName();
		return new String[] {
				pkgName + ".SpringPortletUtilProvider"
		};
	}

	

	@Override
	protected boolean supports(String className) {
		try {
			Class.forName("org.springframework.web.context.request.RequestContextHolder");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected <T> T createProviderInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}

	
	
}

