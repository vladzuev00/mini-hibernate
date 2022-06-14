package by.bsu.minihibernate._classfortest;

import by.bsu.minihibernate.annotation.Column;
import by.bsu.minihibernate.annotation.Table;
import by.bsu.minihibernate.annotation.AutoIncrement;
import by.bsu.minihibernate.annotation.PrimaryKey;
import by.bsu.minihibernate.sqltype.SQLType;

@Table(name = "persons")
public class PersonWithAutoIncrementPrimaryKey
{
    @PrimaryKey
    @AutoIncrement
    @Column(name = "id", type = SQLType.INTEGER)
    private int id;

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
}
