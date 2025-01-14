package net.wazari.dao.jpa.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(JPAPerson.class)
public abstract class JPAPerson_ {
    public static volatile SingularAttribute<JPAPerson, Integer> id;
    public static volatile SingularAttribute<JPAPerson, JPATag> tag;
    public static volatile SingularAttribute<JPAPerson, String> birthdate;
    public static volatile SingularAttribute<JPAPerson, String> contact;
}

