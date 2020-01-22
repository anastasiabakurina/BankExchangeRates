package bankExchangeRates.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY;
    public HibernateUtil() {
    }

    static {
        try {
            Configuration configuration = new Configuration().configure();
            SESSION_FACTORY = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return SESSION_FACTORY.openSession();
    }
}
