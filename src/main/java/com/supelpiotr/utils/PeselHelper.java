package com.supelpiotr.utils;

public class PeselHelper {

    private PeselHelper() {
    }

    public static Long getBirthYear(String pesel) {

        long year;
        long month;

        if (!isPeselValid(pesel))
            return null;

        Long num = Long.parseLong(pesel);

        year = 10 * getDigitFromPos(num, 0);
        year += getDigitFromPos(num, 1);
        month = 10 * getDigitFromPos(num, 2);
        month += getDigitFromPos(num, 3);

        if (month > 80 && month < 93) {
            year += 1800;
        } else if (month > 0 && month < 13) {
            year += 1900;
        } else if (month > 20 && month < 33) {
            year += 2000;
        } else if (month > 40 && month < 53) {
            year += 2100;
        } else if (month > 60 && month < 73) {
            year += 2200;
        }
        return year;
    }

    public static Integer getDigitFromPos(Integer number, Integer pos) {
        return Integer.parseInt(number.toString().substring(pos,
                pos + 1));
    }
    public static Long getDigitFromPos(Long number, Integer pos) {
        return Long.parseLong(number.toString().substring(pos, pos + 1));
    }

    public static Long getChecksum(String pesel) {
        long num = Long.parseLong(pesel);
        long[] tab = new long[11];
        long[] weights = { 1, 3, 7, 9, 1, 3, 7, 9, 1, 3, 1 };

        for (int i = 0; i < 11; i++) {
            tab[i] = getDigitFromPos(num, i);
        }
        long sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += weights[i] * tab[i];
        }
        sum = 10 - (sum % 10);
        return sum % 10;
    }

    public static boolean isPeselValid(String pesel) {

        if (pesel == null)
            return false;

        if (pesel.length() != 11)
            return false;

        return getDigitFromPos(Long.parseLong(pesel), 10).equals(
                getChecksum(pesel));
    }

}
