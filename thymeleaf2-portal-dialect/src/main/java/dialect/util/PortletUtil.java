package dialect.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;

import dialect.util.spi.PortletUtilProvider;
import dialect.util.spi.PortletUtilProviderFactory;

public class PortletUtil {
	private static final Logger log = LoggerFactory.getLogger(PortletUtil.class);

	private static PortletUtil INSTANCE;

	static PortletUtil getInstance() {
		synchronized(PortletUtil.class) {
			if (INSTANCE == null) {
				INSTANCE = new PortletUtil();
//				INSTANCE.initialize();
			}
			return INSTANCE;
		}
	}
	
	List<PortletUtilProvider> providers;
	
	private PortletUtil() {
		initialize();
	}
	
	static class ProviderWithPrecedence {
		final int precedence;
		final PortletUtilProvider provider;
		final String id;
		ProviderWithPrecedence(String id, PortletUtilProvider provider, int precedence) {
			this.id = id;
			this.provider = provider;
			this.precedence = precedence;
		}
	}
	void initialize() {
		log.info("Initializing; finding PortletUtilProviderFactory objects");
		
		final ServiceLoader<PortletUtilProviderFactory> loader = 
				ServiceLoader.load(PortletUtilProviderFactory.class);
		final Iterator<PortletUtilProviderFactory> it = loader.iterator();
		
		final List<ProviderWithPrecedence> supportedProviders = new ArrayList<>();
		
		while (it.hasNext()) {
			final PortletUtilProviderFactory f = it.next();
			final List<String> providerIds = f.getProviderIds();
			log.info("Found PortletUtilProviderFactory: {}; supported provider ids: {}", 
					f.getClass().getName(), providerIds);
			
			for (String providerId: providerIds) {
				log.info("Provider '{}' is supported: {}", providerId, f.isSupported(providerId));
				if (f.isSupported(providerId)) {
					final PortletUtilProvider p = f.createPortletUtilProvider(providerId);
					final int precedence = f.getPrecedence(providerId);
					supportedProviders.add(new ProviderWithPrecedence(providerId, p, precedence));
				}
			}
		}
		
		// Sort by precedence (higher number = higher priority)
		supportedProviders.sort(new Comparator<ProviderWithPrecedence>() {
			@Override
			public int compare(ProviderWithPrecedence o1, ProviderWithPrecedence o2) {
				return o2.precedence - o1.precedence;
			}
		});

		providers = new ArrayList<>();
		for (ProviderWithPrecedence p: supportedProviders) {
			providers.add(p.provider);
			log.info("PortletUtilProvider: precedence={}; id={}", p.precedence, p.id);
		}
		
	}
	
	
	
	public static PortletRequest getPortletRequest(final Arguments arguments) {
		final PortletUtil that = getInstance();
		for (PortletUtilProvider p: that.providers) {
			final PortletRequest req = p.getPortletRequest(arguments);
			if (req != null) {
				return req;
			}
		}
		return null;
	}
	
	public static PortletResponse getPortletResponse(final Arguments arguments) {
		final PortletUtil that = getInstance();
		for (PortletUtilProvider p: that.providers) {
			final PortletResponse resp = p.getPortletResponse(arguments);
			if (resp != null) {
				return resp;
			}
		}
		return null;
	}
	
	public static void setExpressionRootVariable(final String name, final Object value, final Arguments arguments) {
		final PortletUtil that = getInstance();
		boolean unsupported = false;
		for (PortletUtilProvider p: that.providers) {
			try {
				p.setExpressionRootVariable(name, value, arguments);
				return;
			} catch (UnsupportedOperationException e) {
				// Continue with next!
				unsupported = true;
				continue;
			}
		}
		if (unsupported) {
			throw new UnsupportedOperationException("setExpressionRootVariable is not supported by any of found providers");
		}
	}
	
}
