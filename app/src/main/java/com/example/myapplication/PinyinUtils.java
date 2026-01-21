package com.example.myapplication;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtils {
    // 获取汉字的拼音首字母（大写）
    public static String getFirstLetter(String chinese) {
        if (chinese == null || chinese.isEmpty()) {
            return "#"; // 非汉字或空字符用#代替
        }
        char c = chinese.charAt(0);
        // 如果是字母，直接返回大写
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            return String.valueOf(c).toUpperCase();
        }
        // 处理汉字
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        try {
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
            if (pinyinArray != null && pinyinArray.length > 0) {
                return String.valueOf(pinyinArray[0].charAt(0));
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return "#"; // 无法转换的字符用#代替
    }
}