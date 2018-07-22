package com.spring.webapp.data_type;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User type class converting area code and phone number into one column in database
 * and vice versa.
 * In the UI area code and phone number have seperate place holders
 * But in database it wraps into one column.
 *
 * @author Kunaal A Trehan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumber {

    private String areaCode;

    private String areaPhoneNumber;

    public String toString() {
        return areaCode + "-" + areaPhoneNumber;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((areaCode == null) ? 0 : areaCode.hashCode());
        result = prime * result
                + ((areaPhoneNumber == null) ? 0 : areaPhoneNumber.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PhoneNumber other = (PhoneNumber) obj;
        if (areaCode == null) {
            if (other.areaCode != null)
                return false;
        } else if (!areaCode.equals(other.areaCode))
            return false;
        if (areaPhoneNumber == null) {
            return other.areaPhoneNumber == null;
        } else return areaPhoneNumber.equals(other.areaPhoneNumber);
    }

}