package by.bsu.minihibernate._classfortest;

import by.bsu.minihibernate.annotation.*;
import by.bsu.minihibernate.sqltype.SQLType;

import java.util.Objects;

@Entity(name = "simplePerson")
@Table(name = "persons")
public class SimplePerson
{
    @Id
    @AutoIncrement
    @Column(name = "id", type = SQLType.BIG_INTEGER)
    private long id;

    @Column(name = "name", type = SQLType.VARCHAR, isNullable = false)
    private String name;

    @Column(name = "surname", type = SQLType.VARCHAR, isNullable = false)
    private String surname;

    @Column(name = "patronymic", type = SQLType.VARCHAR, isNullable = false)
    private String patronymic;

    @Column(name = "email", type = SQLType.VARCHAR, isNullable = false)
    private String email;

    @Column(name = "age", type = SQLType.INTEGER, isNullable = false)
    private int age;

    public SimplePerson()
    {
        super();
    }

    public SimplePerson(final long id, final String name, final String surname, final String patronymic,
                        final String email, final int age)
    {
        super();
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.email = email;
        this.age = age;
    }

    public void setId(final long id)
    {
        this.id = id;
    }

    public long getId()
    {
        return this.id;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    /*public String getName()
    {
        return this.name;
    }*/

    public void setSurname(final String surname)
    {
        this.surname = surname;
    }

    public String getSurname()
    {
        return this.surname;
    }

    public void setPatronymic(final String patronymic)
    {
        this.patronymic = patronymic;
    }

    public String getPatronymic()
    {
        return this.patronymic;
    }

    public void setEmail(final String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setAge(final int age)
    {
        this.age = age;
    }

    public int getAge()
    {
        return this.age;
    }

    @Override
    public final boolean equals(final Object otherObject)
    {
        if(this == otherObject)
        {
            return true;
        }
        if(otherObject == null)
        {
            return false;
        }
        if(this.getClass() != otherObject.getClass())
        {
            return false;
        }
        final SimplePerson other = (SimplePerson)otherObject;
        return     this.id == other.id
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.surname, other.surname)
                && Objects.equals(this.patronymic, other.patronymic)
                && Objects.equals(this.email, other.email)
                && Objects.equals(this.age, other.age);
    }

    @Override
    public final int hashCode()
    {
        return Objects.hash(this.id, this.name, this.surname, this.patronymic, this.email, this.age);
    }

    @Override
    public final String toString()
    {
        return this.getClass().getName() + "[id = " + this.id + ", name = " + this.name + ", surname = " + this.surname
                + ", patronymic = " + this.patronymic + ", email = " + this.email + ", age = " + age + "]";
    }
}

