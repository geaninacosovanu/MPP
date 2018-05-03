package util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtils {
    private static SessionFactory sessionFactory;

    private HibernateUtils() {
    }
    private static void initialize(String conf) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure(conf)
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.out.println(e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }

    }

    public static SessionFactory getSessionFactory(String conf){
        if(sessionFactory ==null)
            initialize(conf);
        return sessionFactory;
    }
}
