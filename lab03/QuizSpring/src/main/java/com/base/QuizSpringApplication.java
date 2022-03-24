package com.base;

import java.awt.EventQueue;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import com.gui.QuizWindow;

@SpringBootApplication
public class QuizSpringApplication {
	public static void main(String[] args) {
		//SpringApplication.run(QuizSpringApplication.class, args);

		/* Send request with GET method and default Headers.
		var url = "https://api.teleport.org/api/countries/iso_alpha2%3APL/admin1_divisions/";
		*/
		
		demo();
	}
	
	private static class DemoMediator implements QuizWindow.Mediator {
		private static final String LANGUAGE_KEY = "language";
		private static final String BUNDLE_NAME = "com.bundle.MyBundle";//z klasy
		//private static final String BUNDLE_NAME = "MyBundle";//z pliku
		private static final String COUNTRY_LIST_ENDPOINT = "https://api.teleport.org/api/countries";
		private static final String ADMIN_DIVS_ENDPOINT = "https://api.teleport.org/api/countries/iso_alpha2:%s/admin1_divisions/";
		
		private ResourceBundle currBundle;
		private CountryList currCountryList;
		
		{
			setLanguage(this.getPrefLanguage());
		}
		
		@Override
		public List<String> getCountries() {
			currCountryList = new RestTemplate().getForObject(COUNTRY_LIST_ENDPOINT, CountryList.class);
			return currCountryList.getCountries();
		}

		@Override
		public void setLanguage(String lng) {
			var prefs = Preferences.userRoot().node(this.getClass().getName());
			prefs.put(LANGUAGE_KEY, lng);
			currBundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(lng));
		}

		@Override
		public String[] getLanguages() {
			return new String[] {"pl", "en"};
		}

		@Override
		public String getPrefLanguage() {
			var prefs = Preferences.userRoot().node(this.getClass().getName());
			return prefs.get(LANGUAGE_KEY, "pl");
		}
		
		@Override
		public String getValue(String key) {
			try {
				return this.currBundle.getString(key);
			} catch (Exception e) {//brak mapowania
				e.printStackTrace();
				return key;
			}
		}

		@Override
		public boolean checkAnswer(int answer, int countryIndex) throws IllegalArgumentException {
			var url = String.format(ADMIN_DIVS_ENDPOINT, currCountryList.getCodes().get(countryIndex));
			var divCounter = new RestTemplate().getForObject(url, DivCounter.class);
			return (divCounter.getDivCount() == answer)? true : false;
		}
		
		
	}
	
	private static void demo() {
		EventQueue.invokeLater(() -> {
			new QuizWindow(new DemoMediator());
		});
	}

}
