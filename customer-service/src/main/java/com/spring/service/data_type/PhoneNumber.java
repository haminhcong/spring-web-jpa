package com.spring.service.data_type;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumber {

    private String areaCode;

    private String areaPhoneNumber;

    public String toString() {
        return areaCode + "-" + areaPhoneNumber;
    }

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