package io.codense.thymeleaf.portal.dialect.util.spi;

import java.util.List;

public interface PortletUtilProviderFactory {
	
	/**
	 * Names (or id's) of providers this factory may support.
	 * @return list of providers
	 */
	List<String> getProviderIds();
	
	/**
	 * Is the specified provider supported in the current runtime configuration.
	 * @param providerId
	 * @return true if the provider is supported
	 */
	boolean isSupported(String providerId);
	
	/**
	 * Precedence of the specified provider, bigger number = greater priority
	 * @param providerId
	 * @return precedence
	 */
	int getPrecedence(String providerId);
	
	
	/**
	 * Creates a {@link PortletUtilProvider} instance.
	 * @param providerId id of the provider to create
	 * @return the newly created instance
	 * @throws UnsupportedOperationException if the provider is not supported
	 * @see #isSupported(String)
	 */
	PortletUtilProvider createPortletUtilProvider(String providerId);
}
