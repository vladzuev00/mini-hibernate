package by.bsu.minihibernate._runner;

import by.bsu.minihibernate._classfortest.SimplePerson;
import by.bsu.minihibernate.databaseconnectionpool.DataBaseConnectionPool;
import by.bsu.minihibernate.frameworkconfiguration.FrameworkConfiguration;
import by.bsu.minihibernate.frameworkconfiguration.exception.FrameworkConfigurationException;
import by.bsu.minihibernate.session.Session;

import java.sql.Connection;
import java.util.Collection;

public final class Runner
{
    public Runner()
    {
        super();
    }

    public static void main(final String... arg)
            throws Exception
    {
        /*final FrameworkConfiguration frameworkConfiguration = FrameworkConfiguration.createFrameworkConfiguration();
        frameworkConfiguration.addAnnotatedClass(SimplePerson.class);
        frameworkConfiguration.configure();*/

        /*Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SimplePerson.class);
        enhancer.setCallback(new PersistedObjectMethodInterceptor());
        SimplePerson simplePerson = (SimplePerson)enhancer.create();
        simplePerson.setAge(1);*/

        /*
        final DataBaseConnectionPool dataBaseConnectionPool = DataBaseConnectionPool.createDataBaseConnectionPool();
        final Connection connection = dataBaseConnectionPool.findAvailableConnection();
        try(final Session session = new Session(connection, dataBaseConnectionPool))
        {
            final SimplePerson person = new SimplePerson(-1, "gg", "gg", "gg", "gg", 5);
            session.addEntity(person);
            System.out.println(person);
        }
        */

/*
        final DataBaseConnectionPool dataBaseConnectionPool = DataBaseConnectionPool.createDataBaseConnectionPool();
        final Connection connection = dataBaseConnectionPool.findAvailableConnection();
        try(final Session session = new Session(connection, dataBaseConnectionPool))
        {
            final Collection<SimplePerson> foundPersons = session.findAllEntities(SimplePerson.class);
            System.out.println(foundPersons);
            foundPersons.forEach(x -> x.setName("new name 2"));
        }
 */

       /* final DataBaseConnectionPool dataBaseConnectionPool = DataBaseConnectionPool.createDataBaseConnectionPool();
        final Connection connection = dataBaseConnectionPool.findAvailableConnection();
        try(final Session session = new Session(connection, dataBaseConnectionPool))
        {
            final SimplePerson simplePerson = session.findEntityById(5, SimplePerson.class).get();
            simplePerson.setName("added");
            System.out.println(simplePerson);
        }*/


        /*final DataBaseConnectionPool dataBaseConnectionPool = DataBaseConnectionPool.createDataBaseConnectionPool();
        final Connection connection = dataBaseConnectionPool.findAvailableConnection();
        try(final Session session = new Session(connection, dataBaseConnectionPool))
        {
            session.beginTransaction();
            session.deleteEntityById(10, SimplePerson.class);
            session.deleteEntityById(11, SimplePerson.class);
            session.deleteEntityById(12, SimplePerson.class);
            session.findEntityById(5, SimplePerson.class).get().setName("third new name");
            session.commit();
        }*/


    }
}
