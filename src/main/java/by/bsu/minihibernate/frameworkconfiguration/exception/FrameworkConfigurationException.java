package by.bsu.minihibernate.frameworkconfiguration.exception;

public final class FrameworkConfigurationException extends Exception
{
    public FrameworkConfigurationException()
    {
        super();
    }

    public FrameworkConfigurationException(final String description)
    {
        super(description);
    }

    public FrameworkConfigurationException(final Exception cause)
    {
        super(cause);
    }

    public FrameworkConfigurationException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
