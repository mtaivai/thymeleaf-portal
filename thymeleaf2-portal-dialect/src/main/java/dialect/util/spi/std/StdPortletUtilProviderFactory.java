package dialect.util.spi.std;

import dialect.util.spi.AbstractPortletUtilProviderFactory;

public class StdPortletUtilProviderFactory extends AbstractPortletUtilProviderFactory {

	@Override
	protected String[] getProviderClassNames() {
		return new String[] {
				VariablesPortletUtilProvider.class.getName()
		};
	}

	@Override
	protected int getBasePrecedence() {
		return 1000;
	}

	@Override
	protected boolean supports(String className) {
		return true;
	}

	@Override
	protected <T> T createProviderInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}

	
	
}
