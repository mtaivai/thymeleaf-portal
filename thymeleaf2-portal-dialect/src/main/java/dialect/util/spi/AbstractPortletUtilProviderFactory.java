package dialect.util.spi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractPortletUtilProviderFactory implements PortletUtilProviderFactory {

	
	private final List<String> providerClassNames;
	private final int basePrecedence;
	
	
	protected AbstractPortletUtilProviderFactory() {
		basePrecedence = getBasePrecedence();
		providerClassNames = Arrays.asList(getProviderClassNames());
	}
	
	protected abstract String[] getProviderClassNames();
	protected abstract int getBasePrecedence();
	protected abstract boolean supports(String className);
	
	@Override
	public boolean isSupported(String providerId) {
		if (!providerClassNames.contains(providerId)) {
			return false;
		}
		return supports(providerId);
	}


	@Override
	public List<String> getProviderIds() {
		return Collections.unmodifiableList(providerClassNames);
	}


	@Override
	public int getPrecedence(String providerId) {
		return basePrecedence + providerClassNames.size() - providerClassNames.indexOf(providerId) - 1;
	}

	protected abstract <T> T createProviderInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException;
	
	@Override
	public PortletUtilProvider createPortletUtilProvider(String providerId) {
		if (!isSupported(providerId)) {
			throw new UnsupportedOperationException("Provider not supported: " + providerId);
		}
		Class<?> clazz;
		try {
			clazz = Class.forName(providerId);
			return PortletUtilProvider.class.cast(createProviderInstance(clazz));
		} catch (ClassNotFoundException e) {
			throw new UnsupportedOperationException("Provider class not found: " + providerId);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new UnsupportedOperationException("Failed to create instance of provider " + providerId, e);
		}
		
	}
}