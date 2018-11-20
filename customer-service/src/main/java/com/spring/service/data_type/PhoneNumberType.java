package com.spring.service.data_type;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.StringTokenizer;


public class PhoneNumberType implements UserType {

    /**
     * What column types to map,data type of the column
     */
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

    /**
     * Class  details of object which is going to be used
     */
    public Class returnedClass() {
        return PhoneNumber.class;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        return ObjectUtils.nullSafeEquals(x, y);
    }

    public int hashCode(Object x) throws HibernateException {
        if (x != null)
            return x.hashCode();
        else
            return 0;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        /*
          Creates the custom object from the data returned by resultset
         */
        PhoneNumber phone = null;
        String nameVal = resultSet.getString(strings[0]);
        if (nameVal != null) {
            phone = new PhoneNumber();

            StringTokenizer tokenizer = new StringTokenizer(nameVal, "-");
            phone.setAreaCode(tokenizer.nextToken());
            phone.setAreaPhoneNumber(tokenizer.nextToken());

        }
        return phone;
    }


    /**
     * Converts custom object into value which needs to be passed to prepared statement
     */

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {

        if (value == null) {
            preparedStatement.setNull(index, Types.VARCHAR);
        } else {
            PhoneNumber convertedPhoneNumber = (PhoneNumber) value;
            preparedStatement.setString(index, convertedPhoneNumber.toString());
        }

    }

    /**
     * Returns deep copy of object
     */
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null)
            return null;
        else {
            PhoneNumber newObj = new PhoneNumber();
            PhoneNumber existObj = (PhoneNumber) value;

            newObj.setAreaCode(existObj.getAreaCode());
            newObj.setAreaPhoneNumber(existObj.getAreaPhoneNumber());

            return newObj;
        }

    }

    public boolean isMutable() {
        return false;
    }

    /**
     *
     */
    public Serializable disassemble(Object value) throws HibernateException {
        Object deepCopy = deepCopy(value);

        if (!(deepCopy instanceof Serializable)) {
            return (Serializable) deepCopy;
        }

        return null;
    }

    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return deepCopy(cached);
        //return cached;
    }

    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return deepCopy(original);
    }

}
