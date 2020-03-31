package mtgscrub;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (null != sessionFactory) return sessionFactory;

        Configuration configuration = new Configuration();
        String jdbcUrl = "jdbc:mysql://"
                + System.getenv("RDS_HOSTNAME")
                + "/"
                + System.getenv("RDS_DB_NAME");
        String username = "" + System.getenv("RDS_USERNAME");
        String password = "" + System.getenv("RDS_PASSWORD");

        configuration.setProperty("hibernate.connection.url", jdbcUrl)
                .setProperty("hibernate.connection.username", username)
                .setProperty("hibernate.connection.password", password);

        configuration.addAnnotatedClass(CardEntry.class);

        configuration.configure("hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

        try {
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (HibernateException e) {
            System.err.println("Initial SessionFactory creation failed. " + e);
            throw new ExceptionInInitializerError();
        }
        return sessionFactory;
    }
}
