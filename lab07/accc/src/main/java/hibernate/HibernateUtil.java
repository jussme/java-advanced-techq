package hibernate;

import java.io.File;
import java.time.LocalDateTime;
import java.util.EnumSet;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaExport.Action;
import org.hibernate.tool.schema.TargetType;

import hibernate.entities.Event;
import hibernate.entities.Installment;
import hibernate.entities.Payment;
import hibernate.entities.Person;


//z przykladu na stronie howtodoinjava
public class HibernateUtil {
	private static SessionFactory sessionFactory = buildSessionFactory();
	private static ServiceRegistry registry;
	
	private static SessionFactory buildSessionFactory()
	{
		try 
		{
			registry = new StandardServiceRegistryBuilder()
					.configure("hibernate.cfg.xml").build();

			Metadata metaData = new MetadataSources(registry)
					.getMetadataBuilder()
					.build();
			
			return metaData.getSessionFactoryBuilder().build();
		} catch (Throwable ex) {
			return noDBMetaData("src/main/resources/hibernate.cfg.xml");
		}
	}
	
	private static SessionFactory noDBMetaData(String configFilepath) {
		File f = new File(configFilepath);
		Configuration configuration = new Configuration().configure(f);
		return configuration.buildSessionFactory();
	}

	public static Session getSession() {
		if(sessionFactory == null) {
			sessionFactory = noDBMetaData("src/main/resources/hibernate.cfg.xml");
		}
		return sessionFactory.openSession();
	}

	public static void shutdown() {
		sessionFactory.close();
	}
	
	public static void populateDB() {
		var sf = buildSessionFactory();
		Session s = sf.openSession();
		s.beginTransaction();
		
		Event e = new Event("Piknik", "Szkola", LocalDateTime.of(2022, 4, 14, 12, 0));
		
		Installment ins0 = new Installment(e, 1, LocalDateTime.of(2022, 4, 4, 12, 0), (long) 114);
		Installment ins1 = new Installment(e, 2, LocalDateTime.of(2022, 5, 4, 12, 0), (long) 102.5);
		Installment ins2 = new Installment(e, 3, LocalDateTime.of(2022, 6, 4, 12, 0), (long) 124.75);
		
		Person p1 = new Person("Stefan", "Kowalski");
		Person p2 = new Person("Jan", "Nowak");
		
		Payment pay1 = new Payment(p1, e, LocalDateTime.of(2022, 4, 2, 16, 34), (long) 114, 1);
	
		s.persist(e);
		s.persist(ins0);
		s.persist(ins1);
		s.persist(ins2);
		s.persist(p1);
		s.persist(p2);
		s.persist(pay1);
		
		s.getTransaction().commit();
		s.close();
	}
}