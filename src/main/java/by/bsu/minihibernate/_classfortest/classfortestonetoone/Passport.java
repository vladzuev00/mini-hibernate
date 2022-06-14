package by.bsu.minihibernate._classfortest.classfortestonetoone;

import by.bsu.minihibernate.annotation.Column;
import by.bsu.minihibernate.annotation.Table;
import by.bsu.minihibernate.annotation.AutoIncrement;
import by.bsu.minihibernate.annotation.PrimaryKey;
import by.bsu.minihibernate.sqltype.SQLType;

@Table(name = "passports")
public class Passport
{
    @PrimaryKey
    @AutoIncrement
    @Column(name = "id", type = SQLType.INTEGER)
    private int id;

    @Column(name = "number", type = SQLType.VARCHAR, isNullable = false)
    private String number;
}
